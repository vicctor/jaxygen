/*
 * Copyright 2012 domanu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jaxygen.netservice.html;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author domanu
 */
public class HTMLPre extends BasicHTMLElement implements HTMLElement {

    String caption;

    public HTMLPre() {
        super("PRE");
    }

    public HTMLPre(final String text) {
        super("PRE", text);
        caption = text;
    }

    public HTMLPre(final String id, final String text) {
        super("PRE", id);
        caption = text;
    }

    @Override
    public String renderContent() {
        if (caption != null) {
            caption = StringUtils.replace(caption, "<", "&lt;");
            caption = StringUtils.replace(caption, ">", "&gt;");
            caption = StringUtils.replace(caption, " ", "&nbsp;");
            /*    caption = caption.replace("<", "&lt;");
            caption = caption.replace(">", "&gt;");
            caption = caption.replace(" ", "&nbsp;");*/
        } else {
            caption = "";
        }
        return caption;
    }
}
