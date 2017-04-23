package org.jaxygen.netservice.html;

import org.apache.commons.lang3.StringUtils;

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
            caption = StringUtils.replace(caption, "<", "&lt;");
            caption = StringUtils.replace(caption, ">", "&gt;");
            caption = StringUtils.replace(caption, " ", "&nbsp;");
        } else {
            caption = "";
        }
        return caption;
    }

}
