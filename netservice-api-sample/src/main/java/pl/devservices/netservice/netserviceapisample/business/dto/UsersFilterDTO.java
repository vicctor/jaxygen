/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.devservices.netservice.netserviceapisample.business.dto;

import pl.devservices.netservice.annotations.QueryMessage;

/**
 *
 * @author artur
 */
@QueryMessage
public class UsersFilterDTO {
 private String filter;

 public String getFilter() {
  return filter;
 }

 public void setFilter(String filter) {
  this.filter = filter;
 }
}
