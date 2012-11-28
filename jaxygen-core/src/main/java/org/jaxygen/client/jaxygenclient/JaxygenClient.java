package org.jaxygen.client.jaxygenclient;

import com.google.gson.Gson;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Reader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jaxygen.converters.json.JsonMultipartRequestConverter;
import org.jaxygen.converters.sjo.SJOResponseConverter;
import org.jaxygen.dto.Response;

/**
 * Class enables Java applications for accessing Jaxygen web interface using
 * synchronous call.
 *
 */
public class JaxygenClient {

  String servicePath;
  String url;
  Gson gson = new Gson();
  Charset charset = Charset.forName("UTF-8");

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

    private String urlBase;

    public Handler(String urlBase) {
      this.urlBase = urlBase;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      final String methodUrl = urlBase + "/" + method.getName();
      HttpPost post = new HttpPost(methodUrl);
      MultipartEntity mp = new MultipartEntity();
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

      ObjectInputStream osi = null;
      final StringBuffer sb = new StringBuffer();
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
        try {
          osi = new ObjectInputStream(inputProxy);
          Response wrappedResponse = (Response) osi.readObject();
          return wrappedResponse.getDto().getResponseObject();
        } catch (Throwable ex) {
          throw new InvocationTargetException(ex, "Unexpected server response: " + sb.toString());
        }
      } finally {
        if (osi != null) {
          osi.close();
        }
      }
    }
  };

  public JaxygenClient(final String homeURL) {
    this.url = homeURL;
  }

  public Object lookup(final String className, Class<?> remoteInterface) {
    Class<?> interfaces[] = {remoteInterface};
    return java.lang.reflect.Proxy.newProxyInstance(
            remoteInterface.getClassLoader(),
            interfaces,
            new Handler(url + "/" + className));
  }
}
