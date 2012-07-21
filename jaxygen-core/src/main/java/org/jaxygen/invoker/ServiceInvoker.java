package org.jaxygen.invoker;

import org.jaxygen.security.annotations.SecurityContext;
import org.jaxygen.security.annotations.LoginMethod;
import org.jaxygen.security.annotations.LogoutMethod;
import org.jaxygen.security.annotations.Secured;
import org.jaxygen.annotations.SessionContext;
import org.jaxygen.security.exceptions.NotAlowed;
import org.jaxygen.exceptions.ParametersError;
import com.google.gson.Gson;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.jaxygen.dto.ExceptionResponse;
import org.jaxygen.dto.Response;
import org.jaxygen.util.BeanUtil;
import org.jaxygen.http.HttpRequestParams;
import org.jaxygen.http.HttpRequestParser;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.Validable;
import org.jaxygen.converters.ConvertersFactory;
import org.jaxygen.converters.RequestConverter;
import org.jaxygen.exceptions.InvalidPropertyFormat;
import org.jaxygen.security.SecurityProfile;

public class ServiceInvoker extends HttpServlet {

 private static final long serialVersionUID = 566338505269576162L;
 private static final Logger log = Logger.getLogger(ServiceInvoker.class.getCanonicalName());
 private Gson gson;

 @Override
 protected void doGet(HttpServletRequest request,
         HttpServletResponse response) throws ServletException, IOException {

  HttpRequestParams params = null;
  HttpSession session = request.getSession(true);
  try {
   params = new HttpRequestParser(request);
  } catch (Exception ex) {
   throwError(response, "Could nor parse properties", ex);
  }
  final String beensPath = getServletContext().getInitParameter("servicePath");
  final String resourcePath = request.getPathInfo();
  final String queryString = request.getQueryString();


  final String inputFormat = params.getAsString("inputType", 0, 32, "");

  String query = "";

  if (queryString != null) {
   query = URLDecoder.decode(queryString, "UTF-8");
  }

  log("Requesting resource" + resourcePath);
//  System.out.println("Requesting resource" + resourcePath);
//  System.out.println("Query" + request.getQueryString());
  String[] chunks = resourcePath.split("/");
  if (chunks.length < 2) {
   Logger.getLogger(ServiceInvoker.class.getName()).log(Level.SEVERE, "Invalid request, must be in format class/method");
   throw new ServletException("Invalid '" + resourcePath + "' request, must be in format class/method");
  }
  final String methodName = chunks[chunks.length - 1];
  final String className = beensPath + "." + chunks[chunks.length - 2];
  gson = new Gson();

  ClassLoader cl = Thread.currentThread().getContextClassLoader();
  Method[] methods;
  try {
   Class clazz = cl.loadClass(className);
   if (clazz != null) {
    boolean methodFound = false;
    methods = clazz.getMethods();
    for (Method m : methods) {
     if (m.isAnnotationPresent(NetAPI.class)
             && m.getName().equals(methodName)) {
      try {
       checkMethodAllowed(session, clazz.getCanonicalName(), m);
       methodFound = true;
       final Class<?>[] parameterTypes = m.getParameterTypes();
       Object[] parameters = parseParameters(parameterTypes, inputFormat, params, query);       
       Object been = clazz.newInstance();
       validate(parameters);
       try {
        injectSecutityProfile(been, session);
        Object o = m.invoke(been, parameters);
        Response responseWraper = new Response(o);
        response.getWriter().append(gson.toJson(responseWraper));
        if (m.isAnnotationPresent(LoginMethod.class)) {
         if (!(o instanceof SecurityProfile)) {
          throwError(response, "Incompatible interface", "Method " + clazz + "." + methodName + " is annotated with @Login but does not return " + SecurityProfile.class.getCanonicalName());
         }
         attachSecurityContextToSession(session, (SecurityProfile) o);
        }
        if (m.isAnnotationPresent(LogoutMethod.class)) {
         detachSecurityContext(session);
        }
       } catch (InvocationTargetException ex) {
        throwError(response, "Call to bean failed : " + ex.getTargetException().getMessage(), ex.getTargetException());
       } catch (Exception ex) {
        throwError(response, "Call to bean failed : " + ex.getMessage(), ex);
       }
      } catch (Exception ex) {
       throwError(response, "Cann not insitnitate  class " + clazz.getCanonicalName(), ex);
      }

     }
    }
    if (!methodFound) {
     throwError(response, "InvalidRequest", "Method " + className + "." + methodName + " not found");
    }
   } else {
    throwError(response, "InternalError", "Class '" + className + "' not fount");
   }

  } catch (ClassNotFoundException ex) {
   throwError(response, "Class '" + className + "' not fount", ex);

  } finally {
      if (params != null) {
          params.dispose();
      }
  }



 }

 private Object[] parseParameters(final Class<?>[] parameterTypes, final String inputFormat, HttpRequestParams params, String query) throws ParametersError {
  Object parameters[] = new Object[parameterTypes.length];
  int i = 0;
  for (Class<?> p : parameterTypes) {
   try {
     RequestConverter converter = ConvertersFactory.getRequestConverter(inputFormat);
     if (converter != null) {
       parameters[i] = converter.deserialise(params, p);
     } else {
       log.log(Level.WARNING, "Could not find converter for name ''{0}''", inputFormat);
     }    
   } catch (Exception ex) {
    throw new ParametersError("Cann not parse parameters for parameters class " + p.getCanonicalName(), ex);
   }
   i++;
  }
  return parameters;
 }

 private static void callSetter(Field f, Object been, Object sp) throws SecurityException, IllegalArgumentException, IllegalAccessException {
  boolean accessibility = f.isAccessible();
  f.setAccessible(true);
  f.set(been, sp);
  f.setAccessible(accessibility);
 }

 private void throwError(HttpServletResponse response, String string, Throwable ex) throws ServletException, IOException {
  log.log(Level.SEVERE, string, ex);
  ExceptionResponse resp = new ExceptionResponse(ex, string);
  response.getWriter().write(gson.toJson(resp));
 }

 private void throwError(HttpServletResponse response, final String codeName, String message) throws ServletException, IOException {
  log.log(Level.SEVERE, message);
  ExceptionResponse resp = new ExceptionResponse(codeName, message);
  response.getWriter().write(gson.toJson(resp));
 }

 @Override
 protected void doPost(HttpServletRequest request,
         HttpServletResponse response) throws ServletException, IOException {
  System.out.println("POST");
  doGet(request, response);
 }

 private void attachSecurityContextToSession(HttpSession session, SecurityProfile securityProvider) {
  session.setAttribute(SecurityProfile.class.getCanonicalName(), securityProvider);
 }

 private void checkMethodAllowed(HttpSession session, final String clazz, Method method) throws NotAlowed {
  SecurityProfile sp = (SecurityProfile) session.getAttribute(SecurityProfile.class.getCanonicalName());
  if (method.isAnnotationPresent(Secured.class) && (sp == null || sp.isAllowed(clazz, method.getName()) == null)) {
   throw new NotAlowed(clazz, method.getName());
  }
 }

 private void detachSecurityContext(HttpSession session) {
  session.setAttribute(SecurityProfile.class.getCanonicalName(), null);
 }

 //Inject security profile attribute if been contains field annotated by SecurityContext attribute
 private void injectSecutityProfile(Object been, HttpSession session) throws IllegalArgumentException, IllegalAccessException {
  for (Field f : been.getClass().getDeclaredFields()) {
   SecurityProfile sp = (SecurityProfile) session.getAttribute(SecurityProfile.class.getCanonicalName());
   {
    SecurityContext sc = f.getAnnotation(SecurityContext.class);
    if (sc != null) {
     callSetter(f, been, sp);
    }
   }
   {
    SessionContext sc = f.getAnnotation(SessionContext.class);
    if (sc != null) {
     callSetter(f, been, session);
    }
   }
  }
 }

 private void validate(Object[] parameters) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InvalidPropertyFormat {
  for (Object o : parameters) {
   System.out.println("check for validation " + parameters.getClass().getName());
   if (o.getClass().isAnnotationPresent(Validable.class)) {
    BeanUtil.validateBean(o);    
   }
  }
 }
}
