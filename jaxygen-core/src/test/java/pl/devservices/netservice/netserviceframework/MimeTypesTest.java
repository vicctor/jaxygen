package pl.devservices.netservice.netserviceframework;

import java.io.File;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jaxygen.mime.MimeTypeAnalyser;

/**
 * Unit test for simple App.
 */
public class MimeTypesTest
        extends TestCase {

  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public MimeTypesTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(MimeTypesTest.class);
  }

  /**
   */
  public void test_mimeTypeFromFileNameShallMatch() {
    assertEquals("application/postscript", MimeTypeAnalyser.getMimeForExtension(new File("/test/xxx/a.ai")));
    assertEquals("audio/x-au", MimeTypeAnalyser.getMimeForExtension(new File("/test/xxx/.au")));
    assertEquals("video/x-msvideo", MimeTypeAnalyser.getMimeForExtension(new File("/test/xxx/bvs.avi")));
  }

  /**
   */
  public void test_mimeTypeFromStringNameShallMatch() {
    assertEquals("application/postscript", MimeTypeAnalyser.getMimeForExtension("/test/xxx/a.ai"));
    assertEquals("audio/x-au", MimeTypeAnalyser.getMimeForExtension("/test/xxx/.au"));
    assertEquals("video/x-msvideo", MimeTypeAnalyser.getMimeForExtension("/test/xxx/bvs.avi"));
  }

  /**
   */
  public void test_forUnknownExtenstionShallReturnOctetstreamMime() {
    assertEquals("application/octet-stream", MimeTypeAnalyser.getMimeForExtension("/test/xxx/a.54678fghj"));
  }
}
