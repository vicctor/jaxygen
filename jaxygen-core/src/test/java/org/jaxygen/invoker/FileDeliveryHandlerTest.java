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
import java.nio.charset.Charset;
import javax.servlet.ServletOutputStream;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import junit.framework.TestCase;
import org.jaxygen.dto.Downloadable;
import org.jaxygen.util.HttpRangeUtil;

/**
 *
 * @author Artur
 */
public class FileDeliveryHandlerTest extends TestCase {

    class OutputWrapper extends ServletOutputStream {

        public long bytesWritten = 0;
        public StringBuilder data = new StringBuilder();

        @Override
        public void write(int i) throws IOException {
            data.append(Integer.toString(i)).append("|");
            bytesWritten++;
        }
    }

    class InStreamWrapper extends InputStream {

        int maxSize = 1000;
        int v = 0;

        @Override
        public int read() throws IOException {
            if (v < maxSize) {
                return (v++) % 255;
            } else {
                return -1;
            }
        }
    }

    HttpServletRequest request;
    HttpServletResponse response;
    OutputWrapper out;
    InStreamWrapper input;
    Downloadable downloadable;

    @Override
    protected void setUp() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        out = new OutputWrapper();
        input = new InStreamWrapper();
        downloadable = mock(Downloadable.class);

        when(request.getHeader("Range")).thenReturn("bytes=5-10");
        when(response.getOutputStream()).thenReturn(out);

        when(downloadable.contentSize()).thenReturn(1000L);
        when(downloadable.getETag()).thenReturn("test-etag");
        when(downloadable.getFileName()).thenReturn("test-file-name");
        when(downloadable.getContentType()).thenReturn("test/content");
        when(downloadable.getCharset()).thenReturn(Charset.defaultCharset());
        when(downloadable.getDispositon()).thenReturn(Downloadable.ContentDisposition.inline);
        when(downloadable.getStream()).thenReturn(input);
    }

    public void test_postEntireFileFile() throws Exception {
        // given
        when(request.getHeader("Range")).thenReturn(null);

        // when
        FileDeliveryHandler.postFile(request, response, downloadable);

        // then
        assertThat(out.bytesWritten).isEqualTo(1000);
    }

    public void test_postPartFromBeginTheFile() throws Exception {
        // given
        when(request.getHeader("Range")).thenReturn("bytes=0-5");

        // when
        FileDeliveryHandler.postFile(request, response, downloadable);

        // then
        assertThat(out.bytesWritten).isEqualTo(6);
        assertThat(out.data.toString()).isEqualTo("0|1|2|3|4|5|");

        verify(response).setHeader(HttpRangeUtil.HEADER_ACCEPT_RANGES, "bytes");
        verify(response).setHeader(HttpRangeUtil.HEADER_ETAG, "test-etag");
        verify(response).setHeader("Content-Disposition", "inline; filename=\"test-file-name\"");
        verify(response).setHeader(HttpRangeUtil.CONTENT_HEADER_RANGE, "bytes 0-5/1000");
        verify(response).setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
    }

    public void test_postPartFromInsideTheFile() throws Exception {
        // given
        when(request.getHeader("Range")).thenReturn("bytes=5-10");

        // when
        FileDeliveryHandler.postFile(request, response, downloadable);

        // then
        assertThat(out.bytesWritten).isEqualTo(6);
        assertThat(out.data.toString()).isEqualTo("5|6|7|8|9|10|");

        verify(response).setHeader(HttpRangeUtil.HEADER_ACCEPT_RANGES, "bytes");
        verify(response).setHeader(HttpRangeUtil.HEADER_ETAG, "test-etag");
        verify(response).setHeader("Content-Disposition", "inline; filename=\"test-file-name\"");
        verify(response).setHeader(HttpRangeUtil.CONTENT_HEADER_RANGE, "bytes 5-10/1000");
        verify(response).setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
    }

    public void test_postPartFromEndOfTheFile() throws Exception {
        // given
        input.maxSize = 20;
        when(downloadable.contentSize()).thenReturn(20L);
        when(request.getHeader("Range")).thenReturn("bytes=-10");

        // when
        FileDeliveryHandler.postFile(request, response, downloadable);

        // then
        assertThat(out.bytesWritten).isEqualTo(10);
        assertThat(out.data.toString()).isEqualTo("10|11|12|13|14|15|16|17|18|19|");
        verify(response).setHeader(HttpRangeUtil.HEADER_ACCEPT_RANGES, "bytes");
        verify(response).setHeader(HttpRangeUtil.HEADER_ETAG, "test-etag");
        verify(response).setHeader("Content-Disposition", "inline; filename=\"test-file-name\"");
        verify(response).setHeader(HttpRangeUtil.CONTENT_HEADER_RANGE, "bytes 10-19/20");
        verify(response).setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
    }

    public void test_postPartToEndOfTheFile() throws Exception {
        // given
        input.maxSize = 20;
        when(downloadable.contentSize()).thenReturn(20L);
        when(request.getHeader("Range")).thenReturn("bytes=10-");

        // when
        FileDeliveryHandler.postFile(request, response, downloadable);

        // then
        assertThat(out.bytesWritten).isEqualTo(10);
        assertThat(out.data.toString()).isEqualTo("10|11|12|13|14|15|16|17|18|19|");
        verify(response).setHeader(HttpRangeUtil.HEADER_ACCEPT_RANGES, "bytes");
        verify(response).setHeader(HttpRangeUtil.HEADER_ETAG, "test-etag");
        verify(response).setHeader("Content-Disposition", "inline; filename=\"test-file-name\"");
        verify(response).setHeader(HttpRangeUtil.CONTENT_HEADER_RANGE, "bytes 10-19/20");
        verify(response).setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
    }
}
