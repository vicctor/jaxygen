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
package org.jaxygen.invoker;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.jaxygen.dto.Downloadable;
import org.jaxygen.util.HttpRangeUtil;
import org.jaxygen.util.exceptions.ParseError;

/**
 *
 * @author Artur
 */
public class FileDeliveryHandler {

    public static void postFile(HttpServletRequest request, HttpServletResponse response, Downloadable downloadable) throws IOException {
        final String fileName = downloadable.getFileName();
        response.setHeader("Content-Disposition", downloadable.getDispositon().name() + "; filename=\"" + fileName + "\"");
        response.setCharacterEncoding(downloadable.getCharset().name());
        response.setContentType(downloadable.getContentType());
        List<HttpRangeUtil.Range> ranges = decodeRanges(request, downloadable.contentSize());
        // ranges.size != 1 - multipart ranges are't supported so far.
        if (ranges == null || ranges.size() != 1) {
            IOUtils.copy(downloadable.getStream(), response.getOutputStream());
        } else {
            sendFilePartially(ranges, response, downloadable);
        }
        downloadable.dispose();
    }

    private static List<HttpRangeUtil.Range> decodeRanges(HttpServletRequest request, Long outputSize) {
        List<HttpRangeUtil.Range> ranges = null;
        String rangeSpec = request.getHeader(HttpRangeUtil.HEADER_RANGE);
        if (rangeSpec != null && outputSize != null) {
            try {
                ranges = HttpRangeUtil.decodeRange(rangeSpec);
            } catch (ParseError ex) {
                Logger.getLogger(FileDeliveryHandler.class.getName()).log(Level.SEVERE, "Unexpeced Range Http field value. Sending file ignoring ranges", ex);
            }
        }
        return ranges;
    }

    private static void sendFilePartially(List<HttpRangeUtil.Range> ranges, final HttpServletResponse response, final Downloadable downloadable) throws IOException {
        final InputStream is = downloadable.getStream();
        final OutputStream os = response.getOutputStream();
        long pos = 0;
        HttpRangeUtil.Range range = ranges.get(0);
        HttpRangeUtil.Range normalizedRange = normalize(range, downloadable.contentSize());
        response.setHeader(HttpRangeUtil.HEADER_ACCEPT_RANGES, "bytes");
        response.setHeader(HttpRangeUtil.CONTENT_HEADER_RANGE,
                HttpRangeUtil.buildContentRange()
                .setBegin(normalizedRange.getStart())
                .setEnd(normalizedRange.getEnd())
                .setTotal(downloadable.contentSize()).toString());
        response.setHeader(HttpRangeUtil.HEADER_ETAG, downloadable.getETag());
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

        sendPart(pos, normalizedRange, is, os);
    }

    private static long sendPart(long pos, HttpRangeUtil.Range range, InputStream is, OutputStream os) throws IOException {
        long initPos = pos;
        if (range.getStart() > pos) {
            pos = range.getStart();
        }
        long toSkip = pos - initPos;
        long toSend = range.getEnd() - pos;
        int b;
        is.skip((int) toSkip);
        // TODO: performance improovement needed.
        for (int i = 0; i <= toSend; i++) {
            b = is.read();
            if (b > -1) {
                os.write(b);
            }
            pos++;
        }
        return pos;
    }

    private static HttpRangeUtil.Range normalize(HttpRangeUtil.Range range, long fileSize) {
        final Long start;
        final Long end;
        final Long siffix = null;
        if (isStartEnd(range)) {
            if (range.getEnd() != null) {
                end = range.getEnd();
            } else {
                end = fileSize - 1;
            }
            if (range.getStart() != null) {
                start = range.getStart();
            } else {
                start = 0L;
            }
        } else {
            start = fileSize - range.getSuffixLength();
            end = fileSize - 1;
        }
        HttpRangeUtil.Range rc = new HttpRangeUtil.Range() {

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
                return siffix;
            }
        };
        return rc;
    }

    private static boolean isSuffix(HttpRangeUtil.Range range) {
        return range.getSuffixLength() != null;
    }

    private static boolean isStartEnd(HttpRangeUtil.Range range) {
        return (range.getStart() != null);
    }
}
