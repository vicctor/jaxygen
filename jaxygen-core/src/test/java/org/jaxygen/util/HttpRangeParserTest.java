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

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author Artur
 */
public class HttpRangeParserTest extends TestCase {

    public HttpRangeParserTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void test_shallParseValidRange() throws Exception {
        // given
        String spec = "bytes=0-";

        // when
        List<HttpRangeUtil.Range> ranges = HttpRangeUtil.decodeRange(spec);

        // then
        assertThat(ranges)
                .hasSize(1)
                .extracting("start", "end", "suffixLength")
                .contains(tuple(0L, null, null));
    }

    public void test_shallParseValidRangeWithEnd() throws Exception {
        // given
        String spec = "bytes=0-123456789";

        // when
        List<HttpRangeUtil.Range> ranges = HttpRangeUtil.decodeRange(spec);

        // then
        assertThat(ranges)
                .hasSize(1)
                .extracting("start", "end", "suffixLength")
                .contains(tuple(0L, 123456789L, null));
    }

    public void test_shallParseRangeWithoutStart() throws Exception {
        // given
        String spec = "bytes=-123456789";

        // when
        List<HttpRangeUtil.Range> ranges = HttpRangeUtil.decodeRange(spec);

        // then
        assertThat(ranges)
                .hasSize(1)
                .extracting("start", "end", "suffixLength")
                .contains(tuple(null, null, 123456789L));
    }

    public void test_shallParseRange_Start() throws Exception {
        // given
        String spec = "bytes=123456789-";

        // when
        List<HttpRangeUtil.Range> ranges = HttpRangeUtil.decodeRange(spec);

        // then
        assertThat(ranges)
                .hasSize(1)
                .extracting("start", "end", "suffixLength")
                .contains(tuple(123456789L, null, null));
    }
    
    public void test_shallParseRange_BeginAndEnd() throws Exception {
        // given
        String spec = "bytes=1-1,-1";

        // when
        List<HttpRangeUtil.Range> ranges = HttpRangeUtil.decodeRange(spec);

        // then
        assertThat(ranges)
                .hasSize(2)
                .extracting("start", "end", "suffixLength")
                .contains(tuple(1L, 1L, null))
                .contains(tuple(null, null, 1L));;
    }
    
     public void test_shallParseRange_TwoParts() throws Exception {
        // given
        String spec = "bytes=1-1,5-15";

        // when
        List<HttpRangeUtil.Range> ranges = HttpRangeUtil.decodeRange(spec);

        // then
        assertThat(ranges)
                .hasSize(2)
                .extracting("start", "end", "suffixLength")
                .contains(tuple(1L, 1L, null))
                .contains(tuple(5L, 15L, null));
    }
    
    public void test_contentRangeShallBeBuld() throws Exception {
        // given
        HttpRangeUtil.ContentRange range = HttpRangeUtil.buildContentRange();
        
        // when
        range
                .setBegin(100L)
                .setEnd(200L)
                .setTotal(300L);

        // then
        assertThat(range.toString())
                .isEqualTo("bytes 100-200/300");
    }
}
