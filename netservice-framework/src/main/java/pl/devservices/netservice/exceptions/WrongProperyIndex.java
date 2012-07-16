package pl.devservices.netservice.exceptions;

import pl.devservices.netservice.annotations.NetAPI;

@NetAPI(description="The property provided in the properties array has invalid index")
public class WrongProperyIndex extends Exception {
  private static final long serialVersionUID = 472806478442825141L;
  
  public WrongProperyIndex()
  {
  }
  
  public WrongProperyIndex(final String name) {
    super(name);
  }
}
