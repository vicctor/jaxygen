/*
 * Copyright 2014 Artur.
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
package org.jaxygen.http;

import java.io.File;
import org.jaxygen.exceptions.FileUploadDiscarded;



/**
 * The file upload handler interface.
 * 
 * This interface is used by the HttpRequestParser class.
 * 
 * @author Artur Keska
 * 
 */
public interface HttpFileUploadHandler {
  /**
     * Method called before upload starts.
     * 
     * @return The temporary files upload path.
     */
  File initUpload();

  /**
     * This method is called every time the new file upload has been detected.
     * 
     * @param size size of the file
     * @param mimeType file mime type received from browser
     * @param fileName original file name
     * @param fieldName name of file item.
     * @return File object pointing to the physical file where the file content
     *         has to be stored.
     * @throws org.jaxygen.exceptions.FileUploadDiscarded Notified when upload is broken by peer.
     */
  File beginUpload(String fieldName, String fileName, String mimeType, long size)
      throws FileUploadDiscarded;

  /**
     * Method called on the end of the file upload.
     * 
     * @param file
     *        the file object pointing to the uploaded file.
     */
  void endOfUpload(File file);
}
