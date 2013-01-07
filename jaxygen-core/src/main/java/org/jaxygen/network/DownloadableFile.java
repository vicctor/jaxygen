/*
 * Copyright 2012 Artur Keska.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jaxygen.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.jaxygen.dto.Downloadable;
import org.jaxygen.mime.MimeTypeAnalyser;

/**
 *
 * @author Artur Keska
 */
public class DownloadableFile implements Downloadable {

  private String contentType = "application/data";
  private File file;
  private ContentDisposition disposition = ContentDisposition.inline;
  private Charset charset = Charset.forName("UTF-8");
  private InputStream stream;

  public DownloadableFile(File file) {
    this.file = file;
    this.contentType = MimeTypeAnalyser.getMimeForExtension(file);
  }

  public DownloadableFile(File file, ContentDisposition disposition) {
    this.file = file;
    this.disposition = disposition;
    this.contentType = MimeTypeAnalyser.getMimeForExtension(file);
  }

  public DownloadableFile(File file, ContentDisposition disposition, final String contentType) {
    this.file = file;
    this.contentType = contentType;
    this.disposition = disposition;
  }

  public String getContentType() {
    return this.contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public ContentDisposition getDispositon() {
    return this.disposition;
  }

  public void setDisposition(ContentDisposition disposition) {
    this.disposition = disposition;
  }

  public Charset getCharset() {
    return this.charset;
  }

  public void setCharset(Charset charset) {
    this.charset = charset;
  }

  public InputStream getStream() throws IOException {
    dispose();
    this.stream = new FileInputStream(this.file);
    return this.stream;
  }

  public void dispose() throws IOException {
    if (this.stream != null) {
      stream.close();
    }
  }

    public String getFileName() {
        return file.getName();
    }
  
  
}
