package org.jaxygen.exceptions;

import java.rmi.RemoteException;

public class BasicException extends RemoteException {

  /**
   * 
   */
  private static final long serialVersionUID = 1223386249328273236L;
  
  public BasicException()
  {    
  }
  
  public BasicException( String description )
  {    
    super(description);
  }

  public BasicException( String description, Throwable cause  )
  {    
    super( description, cause );
  }
}
