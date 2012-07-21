/**
 * 
 */
package org.jaxygen.converters.xml;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Artur Keska
 *
 */
public final class XMLDateAdapter extends XmlAdapter<String,Date> {

  public static final DateFormat dateFormater = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss 'GMT'Z");
  
  /**
   * 
   */
  public XMLDateAdapter() {    
  }

  @Override
  public Date unmarshal(String v) throws Exception {
    return dateFormater.parse(v);
  }

  @Override
  public String marshal(Date v) throws Exception {
   return dateFormater.format(v);
  }

  

}
