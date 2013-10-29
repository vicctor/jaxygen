package org.jaxygen.client.jaxygenclient;

import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jaxygen.converters.json.JsonMultipartRequestConverter;
import org.jaxygen.converters.sjo.SJOResponseConverter;
import org.jaxygen.dto.ExceptionResponse;
import org.jaxygen.dto.Response;
import org.jaxygen.dto.security.SecurityProfileDTO;
import org.jaxygen.security.SecurityProfile;
import org.jaxygen.security.basic.SecuredMethodDescriptor;

/**
 * Class enables Java applications for accessing Jaxygen web interface using synchronous call.
 *
 */
public class JaxygenClient {

  private final String url;
  private final Gson gson = new Gson();
  private final Charset charset = Charset.forName("UTF-8");
  private final Session session = new Session();

  private class Session {
    public List<String> cookies = new ArrayList<String>(1);
  };

  private class IOProxy extends InputStream implements Appendable {

    PipedOutputStream po = new PipedOutputStream();
    PipedInputStream ps;

    public IOProxy() throws IOException {
      ps = new PipedInputStream(po);
    }

    public Appendable append(CharSequence csq) throws IOException {
      po.write(csq.toString().getBytes(Charset.forName("UTF-8")));
      return this;
    }

    public Appendable append(CharSequence csq, int start, int end) throws IOException {
      po.write(csq.subSequence(start, end).toString().getBytes(Charset.forName("UTF-8")));
      return this;
    }

    public Appendable append(char c) throws IOException {
      char[] a = {c};
      po.write(new String(a).getBytes(Charset.forName("UTF-8")));
      return this;
    }

    @Override
    public void close() throws IOException {
      ps.close();
      super.close();
    }

    @Override
    public int read() throws IOException {
      return ps.read();
    }
  }

  private class Handler implements InvocationHandler {

    private final String urlBase;
    private final Session session;

    Handler(String urlBase, Session session) {
      this.urlBase = urlBase;
      this.session = session;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      final String methodUrl = urlBase + "/" + method.getName();
      HttpPost post = new HttpPost(methodUrl);
      MultipartEntity mp = new MultipartEntity();

      if (session.cookies != null) {
        StringBuilder cookiesStr = new StringBuilder(256);
        for (String cookie : session.cookies) {
          cookiesStr.append(cookie);
          cookiesStr.append(";");
        }
        post.setHeader("Cookie", cookiesStr.substring(0));
      }

      if (args != null) {
        for (Object o : args) {
          IOProxy p = new IOProxy();
          String json = gson.toJson(o);
          //mp.addPart(o.getClass().getName(), new InputStreamBody(p, "text/json", o.getClass().getName()));
          mp.addPart(o.getClass().getName(), new InputStreamBody(new ByteArrayInputStream(json.getBytes(Charset.forName("UTF-8"))), "text/json", o.getClass().getName()));
        }
      }

      mp.addPart(new FormBodyPart("inputType", new StringBody(JsonMultipartRequestConverter.NAME)));
      mp.addPart(new FormBodyPart("outputType", new StringBody(SJOResponseConverter.NAME)));
      post.setEntity(mp);
      HttpResponse response = new DefaultHttpClient().execute(post);
      final HttpEntity e = response.getEntity();


      Header[] hCookies = response.getHeaders("Set-Cookie");
      if (hCookies != null) {
        for (Header h : hCookies) {
          session.cookies.add(h.getValue());
        }
      }

      ObjectInputStream osi = null;
      final StringBuffer sb = new StringBuffer(256);
      try {
        InputStream inputProxy = new InputStream() {
          @Override
          public int read() throws IOException {
            int b = e.getContent().read();
            if (sb.length() < 1024) {
              sb.append((char) b);
            }
            return b;
          }
        };
        Throwable appException = null;
        try {
          osi = new ObjectInputStream(inputProxy);
          Response wrappedResponse = (Response) osi.readObject();
          if (wrappedResponse instanceof ExceptionResponse) {
            ExceptionResponse exr = (ExceptionResponse) wrappedResponse;
            Class<Throwable> exClass = (Class<Throwable>) getClass().getClassLoader().loadClass(exr.getExceptionData().getExceptionClass());
            appException = exClass.newInstance();
          }
          if (appException == null) {
            if (SecurityProfile.class.equals(method.getReturnType())) {
              return convertDtoToSecurityProfile((SecurityProfileDTO) wrappedResponse.getDto().getResponseObject());
            } else {
              return wrappedResponse.getDto().getResponseObject();
            }
          }
        } catch (Throwable ex) {
          throw new InvocationTargetException(ex, "Unexpected server response: " + sb.toString());
        }
        if (appException != null) {
          throw appException;
        }
      } finally {
        if (osi != null) {
          osi.close();
        }
      }
      return null;
    }

    private SecurityProfile convertDtoToSecurityProfile(final SecurityProfileDTO dto) {

      return new SecurityProfile() {
        public String[] getUserGroups() {
          return dto.getGroups();
        }

        public SecuredMethodDescriptor isAllowed(String className, String methodName) {
          String idt = className + "#" + methodName;
          for (String md : dto.getAllowedMethods()) {
            if (idt.equals(md)) {
              return new SecuredMethodDescriptor(className, methodName);
            }
          }
          return null;
        }

        public String[] getAllowedMethodDescriptors() {
          return dto.getAllowedMethods();
        }
      };
    }
  };

  public JaxygenClient(final String homeURL) {
    this.url = homeURL;
  }

  public <T> T lookup(final String className, Class<T> remoteInterface) {
    Class<?> interfaces[] = {remoteInterface};
    return (T) java.lang.reflect.Proxy.newProxyInstance(
            remoteInterface.getClassLoader(),
            interfaces,
            new Handler(url + "/" + className, session));
  }
}
