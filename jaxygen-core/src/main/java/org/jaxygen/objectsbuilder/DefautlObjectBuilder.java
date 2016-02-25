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
package org.jaxygen.objectsbuilder;

import org.jaxygen.objectsbuilder.exceptions.ObjectCreateError;

/**
 * The default objects builder simply creates and instance of the given class
 * without providing additional information.
 *
 * @author Artur
 */
public class DefautlObjectBuilder implements ObjectBuilder {

    @Override
    public Object create(Class clazz) throws ObjectCreateError {
        try {
            return clazz.newInstance();
        } catch (InstantiationException ex) {
            throw new ObjectCreateError("Could not create instance of the class " + clazz.getCanonicalName(), ex);
        } catch (IllegalAccessException ex) {
            throw new ObjectCreateError("Could not create instance of the class " + clazz.getCanonicalName(), ex);
        }
    }

}
