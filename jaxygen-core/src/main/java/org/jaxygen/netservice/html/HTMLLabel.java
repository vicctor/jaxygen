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
        this.caption = text;
    }

    public HTMLLabel(final String text, final String title) {
        super("SPAN");
        this.caption = text;
        setLabelTooltip(title);
    }

    public HTMLLabel(final String id, final String text, final String title) {
        super("SPAN", id);
        this.caption = text;
        setLabelTooltip(title);
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
