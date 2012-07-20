package org.jaxygen.exceptions;

/** The file upload has been discarded.
 * @author Artur Keska
 *
 */
public class FileUploadDiscarded extends Exception {
  /**
   */
  private static final long serialVersionUID = -3260421343091366090L;
  /**Create exception.
   */
  FileUploadDiscarded() {
  }
  /** Create exception.
   * @param cause exception info message.
   */
  FileUploadDiscarded(final String cause) {
    super(cause);
  }
}
