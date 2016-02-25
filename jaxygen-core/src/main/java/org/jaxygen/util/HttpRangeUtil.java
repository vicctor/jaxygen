/*
 * Copyright 2016 Artur.
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
package org.jaxygen.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jaxygen.util.exceptions.ParseError;

/**
 *
 * @author Artur
 */
public class HttpRangeUtil {

    public final static String HEADER_RANGE = "Range";
    public final static String CONTENT_HEADER_RANGE = "Content-Range";
    public final static String HEADER_ACCEPT_RANGES = "Accept-Ranges";
    public final static String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";
    public final static String HEADER_IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    public final static String HEADER_IF_MATCH = "If-Match";
    public final static String HEADER_ETAG = "ETag";

    private final static String byteRangeSetRegex = "(((?<byteRangeSpec>(?<firstBytePos>\\d+)-(?<lastBytePos>\\d+)?)|(?<suffixByteRangeSpec>-(?<suffixLength>\\d+)))(,|$))";
    private final static String byteRangesSpecifierRegex = "bytes=(?<byteRangeSet>" + byteRangeSetRegex + "{1,})";

    private final static Pattern byteRangesSpecifierPattern = Pattern.compile(byteRangesSpecifierRegex);

    public interface Range {

        Long getStart();

        Long getEnd();

        Long getSuffixLength();
    }
    
    public interface ContentRange {
        ContentRange setBegin(Long v);
        ContentRange setEnd(Long v);
        ContentRange setTotal(Long v);

        @Override
        public String toString();
    }

    static class RangeInplementation implements Range {

        Long start;
        Long end;
        Long suffixLength;

        @Override
        public Long getStart() {
            return start;
        }

        @Override
        public Long getEnd() {
            return end;
        }

        @Override
        public Long getSuffixLength() {
            return suffixLength;
        }

        
    }
    
    static class ContentRangeImplementation implements ContentRange {
        private Long begin = 0L, end = 0L, total = 0L;
        @Override
        public ContentRange setBegin(Long v) {
            this.begin = v;
            return this;
        }

        @Override
        public ContentRange setEnd(Long v) {
            this.end = v;
            return this;
        }

        @Override
        public ContentRange setTotal(Long v) {
            this.total = v;
            return this;
        }
        @Override
        public String toString() {
            return "bytes "+ begin + "-" + end + "/" + total;
        }
    }

    public static List<Range> decodeRange(String rangeHeader) throws ParseError {
        Pattern byteRangeSetPattern = Pattern.compile(rangeHeader).compile(byteRangeSetRegex);
        List<Range> ranges = new ArrayList<Range>();
        Matcher byteRangesSpecifierMatcher = byteRangesSpecifierPattern.matcher(rangeHeader);
        if (byteRangesSpecifierMatcher.matches()) {
            String byteRangeSet = byteRangesSpecifierMatcher.group("byteRangeSet");
            Matcher byteRangeSetMatcher = byteRangeSetPattern.matcher(byteRangeSet);
            while (byteRangeSetMatcher.find()) {
                RangeInplementation range = new RangeInplementation();
                if (byteRangeSetMatcher.group("byteRangeSpec") != null) {
                    String start = byteRangeSetMatcher.group("firstBytePos");
                    String end = byteRangeSetMatcher.group("lastBytePos");
                    range.start = Long.valueOf(start);
                    range.end = end == null ? null : Long.valueOf(end);
                } else if (byteRangeSetMatcher.group("suffixByteRangeSpec") != null) {
                    range.suffixLength = Long.valueOf(byteRangeSetMatcher.group("suffixLength"));
                } else {
                    throw new ParseError("Invalid range header");
                }
                ranges.add(range);
            }
        } else {
            throw new ParseError("Invalid range header");
        }
        return ranges;
    }
    
    public static ContentRange buildContentRange() {
        return new ContentRangeImplementation();
    }
}
