/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.netservice.html;

/**
 *
 * @author Artur Keska
 */
public class HTMLHeading extends BasicHTMLElement {

 public enum Level {H1, H2, H3, H4};
 
 public HTMLHeading(Level l, HTMLElement... content) {
  super(l.name());
  append(content);
 }

 public HTMLHeading(Level l, final String id, HTMLElement... content) {
  super(l.name() , id);
  append(content);
 }
}
