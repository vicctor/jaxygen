package org.jaxygen.netservice.html;

public final class HTMLInput extends BasicHTMLElement implements HTMLElement {
  public static final String TAG_NAME = "INPUT";

  public static enum Type {

    button,
    checkbox,
    file,
    hidden,
    image,
    password,
    radio,
    reset,
    submit,
    text
  };

  public HTMLInput() {
    super(TAG_NAME);
  }

  public HTMLInput(final String name, final Object defaultValue) {
    super(TAG_NAME);
    setAttribute("name", name);
    setAttribute("value", defaultValue);
  }
  
  public HTMLInput(final String name, final String id, final String cssClass, final Object defaultValue) {
    super(TAG_NAME, id);
    setAttribute("name", name);
    setCSSClassName(cssClass);
    setAttribute("value", defaultValue);
  }

  public HTMLInput(Type type, final String name, final Object defaultValue) {
    super(TAG_NAME);
    setType(type);
    setAttribute("name", name);
    setAttribute("value", defaultValue);
  }
  
  public HTMLInput(Type type, final String name) {
    super(TAG_NAME);
    setType(type);
    setAttribute("name", name);
  }

  @Override
  public String renderContent() {
    return "";
  }

  public void setType(final Type type) {
    setAttribute("type", type);
  }

  public void setValue(final String value) {
    setAttribute("value", value);
  }

  void setName(String name) {
    setAttribute("name", name);
  }
}
