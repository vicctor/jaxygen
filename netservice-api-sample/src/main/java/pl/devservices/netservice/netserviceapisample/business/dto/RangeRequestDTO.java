/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.devservices.netservice.netserviceapisample.business.dto;

import pl.devservices.netservice.annotations.NetAPI;
import pl.devservices.netservice.annotations.NumberPropertyValidator;
import pl.devservices.netservice.annotations.QueryMessage;
import pl.devservices.netservice.annotations.Validable;

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
