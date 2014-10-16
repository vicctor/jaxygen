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
