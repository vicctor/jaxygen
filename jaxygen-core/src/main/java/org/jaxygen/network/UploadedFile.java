package org.jaxygen.network;

import java.io.File;
import org.jaxygen.dto.Uploadable;

/**
 * Class represents a file uploaded from network.
 *
 * @author Artur Keska
 *
 */
public class UploadedFile implements Uploadable {

  /**
   *
   */
  private static final long serialVersionUID = -3870740622065257947L;
  private File file;
  private String originalName;
  private String mimeType;

  @Override
  protected void finalize() throws Throwable {
    super.finalize();
    file.delete();
  }

  @Override
  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  @Override
  public String getOriginalName() {
    return originalName;
  }

  public void setOriginalName(String originalName) {
    this.originalName = originalName;
  }

  @Override
  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }
}
