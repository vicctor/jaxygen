package pl.devservices.netservice.dto;

import java.io.File;
import java.io.Serializable;

/** Class represents a file uploaded from network
 * @author Artur Keska
 *
 */
public class UploadedFile implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3870740622065257947L;
  private File file;
  private String originalName;
  private String mimeType;
  protected void finalize() {
    file.delete();
  }
  public File getFile() {
    return file;
  }
  public void setFile(File file) {
    this.file = file;
  }
  public String getOriginalName() {
    return originalName;
  }
  public void setOriginalName(String originalName) {
    this.originalName = originalName;
  }
  public String getMimeType() {
    return mimeType;
  }
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

}
