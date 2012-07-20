package org.jaxygen.netservice.html;

import java.util.ArrayList;
import java.util.List;

/**
 * Class represents a HTML table
 *
 * @author Artur Keska
 *
 */
public class HTMLTable extends BasicHTMLElement {


 public static class Cel extends BasicHTMLElement {

  public Cel(final String id) {
   super("TD", id);
  }

  public Cel(final String id, HTMLElement content) {
   super("TD", id);
   getContent().add(content);
  }

  public Cel(HTMLElement content) {
   super("TD");
   getContent().add(content);
  }
 }

 public static class Row extends BasicHTMLElement {

  public Row(final String id) {
   super("TR", id);
  }

  public Row() {
   super("TR");
  }

  public Cel addColumn(final HTMLElement content) {
   Cel cel = new Cel(content);
   getContent().add(cel);
   return cel;
  }
  
  public Cel[] addColumns(final HTMLElement... columns) {
   Cel[] cels = new Cel[columns.length];
   int i = 0;
   for (HTMLElement e : columns) {   
    cels[i] = addColumn(e);
    i++;
   }   
   return cels;
  }

  public void addCel(Cel cel) {
   getContent().add(cel);
  }
 }

 public static class HeadColumn extends BasicHTMLElement {

  public HeadColumn(final String id) {
   super("TH", id);
  }

  public HeadColumn() {
   super("TH");
  }

  public HeadColumn(HTMLElement content) {
   super("TH");
   getContent().add(content);
  }
 }

 public static class Header extends BasicHTMLElement {

  public Header(final String id) {
   super("THEAD", id);
  }

  public Header() {
   super("THEAD");
  }

  public void addColumn(HeadColumn column) {
   getContent().add(column);
  }

  public HeadColumn[] createColumns(final String... captions) {
   HeadColumn[] rc = new HeadColumn[captions.length];
   int i = 0;
   for (final String caption : captions) {
    rc[i] = new HeadColumn(new HTMLLabel(caption));
    addColumn(rc[i]);
    i++;
   }
   return rc;
  }
  
  public HeadColumn[] createColumns(final HTMLElement... captions) {
   HeadColumn[] rc = new HeadColumn[captions.length];
   int i = 0;
   for (final HTMLElement caption : captions) {
    rc[i] = new HeadColumn(caption);
   }
   return rc;
  }
 }
 private Header header = new Header();
 private List<Row> rows = new ArrayList<Row>();

 public HTMLTable() {
  super("TABLE");
 }

 public HTMLTable(final String id) {
  super("TABLE", id);
 }

 @Override
 public String renderContent() {
  StringBuilder output = new StringBuilder();
  if (getHeader() != null) {
   output.append(getHeader().render());
  }
  for (Row row : rows) {
   output.append(row.render());
  }
  return output.toString();
 }

 public void addRow(Row row) {
  getRows().add(row);
 }

 public void addRows(Row... rows) {
  for (Row row: rows) {
   addRow(row);
  }
 }

 
 /**
  * @param header the header to set
  */
 public void setHeader(Header header) {
  this.header = header;
 }

 /**
  * @return the header
  */
 public Header getHeader() {
  return header;
 }

 public Header createHeader() {
  setHeader(new Header());
  return getHeader();
 }

 /**
  * @param rows the rows to set
  */
 public void setRows(List<Row> rows) {
  this.rows = rows;
 }

 /**
  * @return the rows
  */
 public List<Row> getRows() {
  return rows;
 }

 public Row addRow() {
  Row r = new Row();
  addRow(r);
  return r;
 }
}
