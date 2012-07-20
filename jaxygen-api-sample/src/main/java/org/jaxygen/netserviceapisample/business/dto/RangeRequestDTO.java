/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.netserviceapisample.business.dto;

import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.NumberPropertyValidator;
import org.jaxygen.annotations.QueryMessage;
import org.jaxygen.annotations.Validable;

/**
 *
 * @author artur
 */
@Validable
@QueryMessage
public class RangeRequestDTO {
 private int value;

 @NumberPropertyValidator(minValue=10, maxValue=100)
 @NetAPI(description="Pass value in range <10,100>")
 public int getValue() {
  return value;
 }

 public void setValue(int value) {
  this.value = value;
 }
 
}
