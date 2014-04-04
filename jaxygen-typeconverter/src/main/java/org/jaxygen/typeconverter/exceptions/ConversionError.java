package org.jaxygen.typeconverter.exceptions;

/**
 *
 * @author Artur Keska <artur.keska@xdsnet.pl>
 */
public class ConversionError extends Exception {

    /**
     * Creates a new instance of <code>ConversionError</code> without detail message.
     */
    public ConversionError() {
    }


    /**
     * Constructs an instance of <code>ConversionError</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ConversionError(String msg) {
        super(msg);
    }

  /**
     * Constructs an instance of <code>ConversionError</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ConversionError(String msg, Throwable ex) {
        super(msg, ex);
    }
}
