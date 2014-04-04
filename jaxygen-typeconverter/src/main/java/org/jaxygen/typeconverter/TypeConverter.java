package org.jaxygen.typeconverter;

import org.jaxygen.typeconverter.exceptions.ConversionError;

/** This is a base class for the type converters
 * The TypeConverter is responsible to convert object of one class class to another.
 * 
 * @author Artur Keska <artur.keska@xdsnet.pl>
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
