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
package org.jaxygen.dto;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**Interface to file uploaded to over the HTTP protocol.
 * Once can use the Uploadabel as a field of DTO object.
 * Engine will automatically fill it if found corresponding
 * file in the multipart content.
 *
 * @author Artur Keska
 */
public interface Uploadable extends Serializable{
  /** Get temporary file associated with the request.
   * The file is valid during the request time only.
   * 
   * @return 
   */
  File getFile() throws IOException;
  
  /** Get input stream that reads the content of the file.
   * This method is usefull in case if the file is being small
   * (in memory) and do not need to be stored in the filesystem during 
   * upload.
   */
  InputStream getInputStream() throws IOException;
  /** Get original name of the file
   * 
   * @return 
   */
  String getOriginalName();
  
  /** Get mime type
   * 
   * @return 
   */
  String getMimeType();
}
