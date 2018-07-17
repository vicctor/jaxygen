package org.jaxygen.netservice.html;

import java.util.ArrayList;
import java.util.Arrays;
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
    private String tag;
    private List<HTMLElement> content = new ArrayList<HTMLElement>();

    BasicHTMLElement(final String tag, final String id) {
        attributes.put("id", id);
        this.tag = tag;
    }

    BasicHTMLElement(final String tag) {
        this.tag = tag;
    }

    public BasicHTMLElement setAttribute(final String key, final Object value) {
        attributes.put(key, value);
        return this;
    }

    public String getCSSClassName() {
        return attributes.get("class").toString();
    }

    public BasicHTMLElement setCSSClassName(String className) {
        attributes.put("class", className);
        return this;
    }

    public String getStyleInfo() {
        return attributes.get("style").toString();
    }

    public BasicHTMLElement setStyleInfo(String styleInfo) {
        attributes.put("style", styleInfo);
        return this;
    }

    public String getLabelTooltip() {
        return attributes.get("style").toString();
    }

    public BasicHTMLElement setLabelTooltip(String tooltip) {
        attributes.put("title", tooltip);
        return this;
    }

    /**
     * Render the system attributes.
     *
     * @return Get the string containing tag attributes.
     */
    public String renderAttributes() {
        StringBuilder sb = new StringBuilder();
        for (String key : attributes.keySet()) {

            Object attribute = attributes.get(key);
            if (attribute != null) {
                if (attribute.getClass().equals(Boolean.class)) {
                    sb.append(" ");
                    sb.append(key);
                    sb.append(" ");
                } else {
                    sb.append(" ");
                    sb.append(key);
                    sb.append("='");
                    sb.append(attribute);
                    sb.append("' ");
                }
            }
        }
        return sb.toString();
    }

    public String renderContent() {
        StringBuilder sb = new StringBuilder();
        for (HTMLElement element : content) {
            sb.append(element.render());
        }
        return sb.toString();
    }

    /**
     * Override this method in order to instruct the renderer if the given tag
     * could be written in the short for (e.g. &lt;br /&gt; or nor (e.g.
     * &lt;link&gt;&lt;/link&gt;) even if does not contain any content
     *
     * @return true if allowed.
     */
    protected boolean isShortTagAlowed() {
        return true;
    }

    @Override
    public String render() {
        final String contentString = renderContent();
        StringBuilder sb = new StringBuilder();
        if (contentString.length() > 0 || isShortTagAlowed() == false) {
            sb.append("<");
            sb.append(tag);
            sb.append(" ");
            sb.append(renderAttributes());
            sb.append(">");
            sb.append(contentString);
            sb.append("</");
            sb.append(tag);
            sb.append(">");
        } else {
            sb.append("<");
            sb.append(tag);
            sb.append(" ");
            sb.append(renderAttributes());
            sb.append(" />");
        }
        return sb.toString();
    }

    public void setContent(List<HTMLElement> content) {
        this.content = content;
    }

    public List<HTMLElement> getContent() {
        return content;
    }

    /**
     * Append elements to currently managed collection of content (see
     * setContent). If content is not set, function creates one.
     *
     * @param elements appended list of HTML elements.
     */
    public void append(HTMLElement... elements) {
        if (content == null) {
            content = new ArrayList<HTMLElement>();
        }
        content.addAll(Arrays.asList(elements));
    }
}
