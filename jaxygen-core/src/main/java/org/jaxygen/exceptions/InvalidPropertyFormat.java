package org.jaxygen.exceptions;

import org.jaxygen.annotations.NetAPI;

@NetAPI(description="This exception is thrown in case if provided parameter format is not valid.")
public class InvalidPropertyFormat extends BasicException {
  private static final long serialVersionUID = 1757274619471348761L;

  public InvalidPropertyFormat() {
   
  }
  
  public InvalidPropertyFormat( final String message ) {
    super(message);
  }
}
