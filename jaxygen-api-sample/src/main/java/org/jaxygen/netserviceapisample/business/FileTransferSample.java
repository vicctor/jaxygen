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
package org.jaxygen.netserviceapisample.business;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.dto.UploadedFile;
import org.jaxygen.netserviceapisample.business.dto.AddImageRequestDTO;

/**Class demonstrates usage of the UploadedFile structure.
 *
 * @author Artur Keska
 */
public class FileTransferSample {
    private final static String TMP = System.getProperty("java.io.tmpdir");
    private final static String SHARED_FILE_NAME = "FileTransferSample.tmp";
    private final static File   SHARED_FILE = new File(TMP, SHARED_FILE_NAME);
    
    @NetAPI(description="Function demonstrates usage of the UploadedFile class usage")
    public UploadedFile uploadFile(AddImageRequestDTO request) throws IOException {
        // Get reference to the uploaded file descriptor
        UploadedFile uf = request.getFile();
        
        // Get reference to the phisical temporary file
        // Note that it's not guaranteed that the file exists
        // after this call exited.
        File f = uf.getFile();
        
        // Copy file to the SESSION_FILE
        // OK. It's not session related, but it's just a demo
        FileUtils.copyFile(f, SHARED_FILE);
        
        
        return uf;
    }
    
    @NetAPI(description="Function demonstrates how to make the file downloadable")
    public File downloadFile() {
        return SHARED_FILE;
    }
}
