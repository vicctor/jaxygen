/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sandbok;

import java.util.Calendar;

/**
 *
 * @author artur
 */
public class Sandbok {

 /**
  * @param args the command line arguments
  */
 public static void main(String[] args) {
  Calendar c0 = Calendar.getInstance();
  StringBuilder out = new StringBuilder();
  for (int i = 0; i < 100000; i++) {
   out.append("string");
  }
  Calendar c1 = Calendar.getInstance();
  System.out.println("Tie = "+ (c1.getTimeInMillis() - c0.getTimeInMillis()));
 }
}
