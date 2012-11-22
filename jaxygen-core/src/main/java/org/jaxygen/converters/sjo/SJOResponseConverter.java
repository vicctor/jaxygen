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
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaxygen.converters.ResponseConverter;
import org.jaxygen.converters.exceptions.SerializationError;

/**
 *
 * @author imfact02
 */
public class SJOResponseConverter implements ResponseConverter {

    public static final String NAME = "SJO";

    public String getName() {
        return NAME;
    }

    public void serialize(Object object, OutputStream writter) throws SerializationError {
        ObjectOutputStream osi = null;
        try {
            osi = new ObjectOutputStream(writter);
            try {
                osi.writeObject(object);
            } catch (IOException ex) {
                throw new SerializationError("Could not serilize stream", ex);
            }
        } catch (IOException ex) {
            throw new SerializationError("Could open serialization stream", ex);
        } finally {
            try {
                if (osi != null) {
                    osi.close();
                }
            } catch (IOException ex) {
                throw new SerializationError("Error while closing output stream", ex);
            }
        }

    }
}
