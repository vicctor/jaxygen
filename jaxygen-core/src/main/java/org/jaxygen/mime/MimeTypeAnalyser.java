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
package org.jaxygen.mime;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class helpful when required to discover the mime type of file by the
 * file extension.
 *
 * Class uses extension to mime mappin from file !jar:/org/jaxygen/mimetypes.xml
 *
 * @author Artur Keska
 */
public class MimeTypeAnalyser {

  static final Properties mimeTypes = new Properties();

  static {
    InputStream is;
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    is = loader.getResourceAsStream("org/jaxygen/mimetypes.xml");
    if (is != null) {
      try {
        mimeTypes.loadFromXML(new BufferedInputStream(is));
      } catch (IOException ex) {
        Logger.getLogger(MimeTypeAnalyser.class.getName()).log(Level.SEVERE, null, ex);
      } finally {
        try {
          is.close();
        } catch (IOException ex) {
          Logger.getLogger(MimeTypeAnalyser.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
    }
  }

  
  public static String getMimeForExtension(File file) {
    final String name = file.getName();    
    return getMimeForExtension(name);
  }

  public static String getMimeForExtension(final String fileName) {
    String name = fileName;
    int dotPosition = name.lastIndexOf('.');
    if (dotPosition > -1) {
      name = name.substring(dotPosition + 1);
    }
    return mimeTypes.getProperty("." + name, "application/octet-stream");
  }
}
