/*
 * Copyright 2012 imfact02.
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
package org.jaxygen.converters.json;

import java.util.Map;
import org.jaxygen.converters.RequestConverter;
import org.jaxygen.converters.exceptions.DeserialisationError;
import org.jaxygen.http.HttpRequestParams;
import org.jaxygen.network.UploadedFile;

/**
 *
 * @author imfact02
 */
public class JsonMultipartRequestConverter implements RequestConverter{
    public final static String NAME="JSON/MULTIPART";
    
    public String getName() {
        return NAME;
    }

    public Object deserialise(HttpRequestParams params, Class<?> beanClass) throws DeserialisationError {
        Map<String, UploadedFile> files = params.getFiles();
        if (files != null) {
            UploadedFile f = files.get(beanClass.getName());
            
        }
        return null;
    }
    
}
