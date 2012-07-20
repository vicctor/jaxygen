/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.netserviceapisample.business.dto;

/**
 *
 * @author artur
 */
public class UserDTO {
  private String name;
  private int    age;
  private Sex    sex;

 public int getAge() {
  return age;
 }

 public void setAge(int age) {
  this.age = age;
 }

 public String getName() {
  return name;
 }

 public void setName(String name) {
  this.name = name;
 }

 public Sex getSex() {
  return sex;
 }

 public void setSex(Sex sex) {
  this.sex = sex;
 }  
}
