package org.jaxygen.netservice.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the abstract basic HTML element class. Class holds common HTML
 * elements parameters and properties.
 * 
 * @author Artur Keska
 * 
 */
public abstract class BasicHTMLElement implements HTMLElement {
  private Map<String, Object> attributes = new HashMap<String, Object>();
  private String              tag;
  private List<HTMLElement>   content    = new ArrayList<HTMLElement>();

  BasicHTMLElement(final String tag, final String id) {
    attributes.put("id", id);
    this.tag = tag;
  }

  BasicHTMLElement(final String tag) {
    this.tag = tag;
  }

  public void setAttribute(final String key, final Object value) {
    attributes.put(key, value);
  }

  public String getCSSClassName() {
    return attributes.get("class").toString();
  }

  public void setCSSClassName(String className) {
    attributes.put("class", className);
  }

  public String getStyleInfo() {
    return attributes.get("style").toString();
  }

  public void setStyleInfo(String styleInfo) {
    attributes.put("style", styleInfo);
  }

  /**
   * Render the system attributes.
   * 
   * @return
   */
  public String renderAttributes() {
    String rc = "";
    for (String key : attributes.keySet()) {
       
      Object attribute = attributes.get(key);
      if (attribute != null) {
        if (attribute.getClass().equals(Boolean.class)) {
          rc += " " + key + " ";
        } else {
        rc += " " + key + "='" + attribute + "' ";
        }
      }
    }
    return rc;
  }

  public String renderContent() {
    String rc = "";
    for (HTMLElement element : content) {
      rc += element.render();
    }
    return rc;
  }

  /** Override this method in order to instruct the renderer if the given tag could be writen in the
   * short for (e.g. <br /> or nor (e.g. <link></link>) even if does not contain any content
   * @return 
   */
  protected boolean isShortTagAlowed() {
    return true;
  }
  
  @Override
  public String render() {
    final String contentString = renderContent();
    String rc="";
    if (contentString.length() > 0 || isShortTagAlowed() == false)
      rc = "<" + tag + " " + renderAttributes() + ">" + renderContent() + "</"
      + tag + ">";
    else 
      rc = "<" + tag + " " + renderAttributes() + " />";
    return rc;
  }

  public void setContent(List<HTMLElement> content) {
    this.content = content;
  }

  public List<HTMLElement> getContent() {
    return content;
  }

  /** Append elements to currently managed collection of content (see setContent).
   * If content is not set, function creates one.
   * @param elements 
   */
  public void append(HTMLElement... elements) {
   if (content == null) {
    content = new ArrayList<HTMLElement>();
   }
   for (HTMLElement e : elements) {
    content.add(e);
   }
  }
}
