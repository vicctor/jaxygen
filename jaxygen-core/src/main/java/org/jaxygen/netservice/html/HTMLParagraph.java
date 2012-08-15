/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jaxygen.netservice.html;

/**
 *
 * @author Artur Keska
 */
public class HTMLParagraph extends BasicHTMLElement {
 public final static String TAG = "P";
 public HTMLParagraph() {
  super(TAG);
 }

 public HTMLParagraph(final String id) {
  super(TAG, id);
 }
}
