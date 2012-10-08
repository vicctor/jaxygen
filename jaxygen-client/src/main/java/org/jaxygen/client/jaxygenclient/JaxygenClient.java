package org.jaxygen.client.jaxygenclient;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.reflect.InvocationHandler;
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

/**
 * Class enables Java applications for accessing Jaxygen web interface using
 * synchronous call.
 *
 */
public class JaxygenClient {

    String servicePath;
    String url;
      Gson gson = new Gson();
    
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
        public int read() throws IOException {
            return ps.read();
        }
        
    }

    private class Handler implements InvocationHandler {
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            HttpPost post = new HttpPost(url);
            post.setEntity(new FileEntity(null, ContentType.TEXT_HTML));
            MultipartEntity mp = new MultipartEntity();
            for (Object o : args) {
              IOProxy p = new IOProxy();
              gson.toJson(o, p);
              mp.addPart(o.getClass().getName(), new InputStreamBody(p, "text/json" , o.getClass().getName()));
            }
            
            mp.addPart(new FormBodyPart("inputType", new StringBody("JSON/MULTIPART")));
            mp.addPart(new FormBodyPart("outputType", new StringBody("JSON")));
            
            HttpResponse response = new DefaultHttpClient().execute(post);
            HttpEntity e = response.getEntity();
            InputStreamReader ir = new InputStreamReader(e.getContent());
            return gson.fromJson(ir, method.getReturnType());
        }
    };

    Object lookup(Class<?> remoteInterface) {
        return java.lang.reflect.Proxy.newProxyInstance(
                remoteInterface.getClassLoader(),
                remoteInterface.getInterfaces(),
                new Handler());
    }
}
