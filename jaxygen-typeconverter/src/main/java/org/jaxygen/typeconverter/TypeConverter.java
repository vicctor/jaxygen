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
package org.jaxygen.typeconverter;

import org.jaxygen.typeconverter.exceptions.ConversionError;

/** This is a base class for the type converters
 * The TypeConverter is responsible to convert object of one class class to another.
 * 
 * @author Artur
 */
public interface TypeConverter<FROM, TO> {
    /** The convert method creates a new instance of object of the class TO
     * by converting the object from of the class FROM.
     * @param from
     * @return
     * @throws ConversionError 
     */
    TO convert(FROM from) throws ConversionError;
    
    /** Get the class from which is an input for convert method
     * 
     * @return 
     */
    Class<FROM> from();
    
    /** Get the class which is an output of the convert method.
     *
     * @return
     */
    Class<TO> to();
}
