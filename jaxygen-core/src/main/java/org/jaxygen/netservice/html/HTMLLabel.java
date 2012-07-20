package org.jaxygen.netservice.html;

/**
 * The class represents a plain text put in the html
 * 
 * @author Artur Keska
 * 
 */
public class HTMLLabel extends BasicHTMLElement implements HTMLElement {

  String caption;

  public HTMLLabel() {
    super("SPAN");
  }

  public HTMLLabel(final String text) {
    super("SPAN");
    caption = text;
  }

  public HTMLLabel(final String id, final String text) {
    super("SPAN", id);
    caption = text;
  }

  @Override
  public String renderContent() {
    if (caption != null) {
      caption = caption.replace("<", "&lt;");
      caption = caption.replace(">", "&gt;");
      caption = caption.replace(" ", "&nbsp;");
    } else {
      caption = "";
    }
    return caption;
  }

}
