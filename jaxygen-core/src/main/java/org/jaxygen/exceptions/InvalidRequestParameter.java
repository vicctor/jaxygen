package org.jaxygen.exceptions;

import org.jaxygen.annotations.NetAPI;



/** This exception might be thrown in case if one of the parameter
 * passed to the HTTP request handler is not valid.
 * 
 * @author Artur Keska
 */
@NetAPI(description="This exception might be thrown in case if one of the parameter passed to the HTTP request handler is not valid")
public class InvalidRequestParameter extends BasicException {   
  private static final long serialVersionUID = 377249426999361314L;
  
    public InvalidRequestParameter()
    {      
    }
    public InvalidRequestParameter(String parameterName)
    {      
      super("Invalid parameter value: "+parameterName);
    }
}
