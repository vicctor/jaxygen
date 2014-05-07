/*
 * Copyright 2014 Artur.
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

package pl.devservices.jaxygen.dto.exception;

import java.util.HashMap;
import junit.framework.TestCase;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.dto.ExceptionResponse;
import org.jaxygen.dto.properties.PropertyDTO;

/**
 *
 * @author Artur
 */
public class ExceptionDTOTest extends TestCase {
    
    public class TestException extends Exception {
        private String strProperty;
        private int intProperty;

        @NetAPI
        public String getStrProperty() {
            return strProperty;
        }

        public void setStrProperty(String strProperty) {
            this.strProperty = strProperty;
        }

        @NetAPI
        public int getIntProperty() {
            return intProperty;
        }

        public void setIntProperty(int intProperty) {
            this.intProperty = intProperty;
        }
        
        
    }
    
    public ExceptionDTOTest(String testName) {
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

    public void test_shallConvertExceptionProperty() {
        TestException ex = new TestException();
        ex.setIntProperty(10);
        ex.setStrProperty("Test string to test");
        
        ExceptionResponse exr = new ExceptionResponse(ex, "Test exception");
        
        HashMap<String, Object> values = new HashMap<String, Object>();
        for (PropertyDTO pd : exr.getExceptionData().getArguments()) {
            values.put(pd.getKey(), pd.getValue());
        }
        
        assertEquals(10, values.get("intProperty"));
        assertEquals("Test string to test", values.get("strProperty"));
    }
}
