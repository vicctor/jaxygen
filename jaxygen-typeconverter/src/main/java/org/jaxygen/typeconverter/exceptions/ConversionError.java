package org.jaxygen.typeconverter.exceptions;

/**
 *
 * @author Artur
 */
public class ConversionError extends RuntimeException {

    /**
     * Creates a new instance of &lt;code&gt;ConversionError&lt;/code&gt; without detail message.
     */
    public ConversionError() {
    }


    /**
     * Constructs an instance of &lt;code&gt;ConversionError&lt;/code&gt; with the specified detail message.
     * @param msg the detail message.
     */
    public ConversionError(final String msg) {
        super(msg);
    }

  /**
     * Constructs an instance of &lt;code&gt;ConversionError&lt;/code&gt; with the specified detail message.
     * @param msg the detail message.
     * @param ex Cause of this exception.
     */
    public ConversionError(final String msg, final Throwable ex) {
        super(msg, ex);
    }
}
