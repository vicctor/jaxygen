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
package org.jaxygen.converters.sjo;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaxygen.converters.RequestConverter;
import org.jaxygen.converters.exceptions.DeserialisationError;
import org.jaxygen.dto.Uploadable;
import org.jaxygen.http.HttpRequestParams;
import org.jaxygen.network.UploadedFile;

/**
 *
 * @author imfact02
 */
public class SJORRequestConverter implements RequestConverter {

    public static final String NAME = "SJO";

    public String getName() {
        return NAME;
    }

    public Object deserialise(HttpRequestParams params, Class<?> beanClass) throws DeserialisationError {
        Map<String, UploadedFile> files = params.getFiles();
        Object rc = null;
        if (files != null) {
            Uploadable u = files.get(beanClass.getName());
            if (u != null) {
                ObjectInputStream osi = null;
                try {
                    osi = new ObjectInputStream(u.getInputStream());
                    try {
                        rc = osi.readObject();
                    } catch (ClassNotFoundException ex) {
                        throw new DeserialisationError("Could not deserialize input stream of object class " + beanClass.getName(), ex);
                    }
                } catch (IOException ex) {
                    throw new DeserialisationError("Could open deserialization stream of object class " + beanClass.getName(), ex);
                } finally {
                    try {
                        osi.close();
                    } catch (IOException ex) {
                        throw new DeserialisationError("Could close input stream of object class " + beanClass.getName(), ex);
                    }
                }
            } else {
                throw new DeserialisationError("Missing object " + beanClass.getName() + " in request", null);
            }
        } else {
            throw new DeserialisationError("Deserialziation error - input objects table is empty", null);
        }
        return rc;
    }
}
