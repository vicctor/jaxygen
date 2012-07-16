package pl.devservices.netservice.html;

public class HTMLInput extends BasicHTMLElement implements HTMLElement {

 
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
  text};

 public HTMLInput() {
  super("INPUT");
 }

 public HTMLInput(String id, final Object defaultValue) {
  super("INPUT", id);
  setAttribute("name", id);
  setAttribute("value", defaultValue);
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
