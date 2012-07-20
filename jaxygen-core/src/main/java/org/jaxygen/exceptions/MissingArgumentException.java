package org.jaxygen.exceptions;

import org.jaxygen.annotations.NetAPI;

/**Notify that the required parameter has not been set.
 * @author Artur Keska
 *
 */
@NetAPI(description="Argument of the request is missing")
public class MissingArgumentException extends Exception {
  public MissingArgumentException(String message) {
    super(message);
  }

  private static final long serialVersionUID = 2602260413700160466L;

}
