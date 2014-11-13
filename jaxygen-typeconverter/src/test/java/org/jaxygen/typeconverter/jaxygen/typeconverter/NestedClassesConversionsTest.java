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
package org.jaxygen.typeconverter.jaxygen.typeconverter;

import static junit.framework.TestCase.assertEquals;
import org.jaxygen.typeconverter.TypeConverter;
import org.jaxygen.typeconverter.TypeConverterFactory;
import org.jaxygen.typeconverter.exceptions.ConversionError;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 *
 * @author Artur
 */
public class NestedClassesConversionsTest {

    private TypeConverter<B, A> bToAConverterMock;
    private TypeConverter<A, B> aToBConverterMock;
    private TypeConverter<A, C> aToCConverterMock;
    private TypeConverter<C, A> cToAConverterMock;
    private TypeConverter<C, B> cToBConverterMock;

    private final A a = new A();
    private final B b = new B();
    private final C c = new C();

    public static class A {

        private Long value = null;

        public Long getValue() {
            return value;
        }

        public void setValue(Long value) {
            this.value = value;
        }
    }

    public static class B extends A {

        private String bValue = null;

        public String getbValue() {
            return bValue;
        }

        public void setbValue(String bValue) {
            this.bValue = bValue;
        }
    }

    public static class C extends B {

        private Double cValue = 0.0d;

        public Double getcValue() {
            return cValue;
        }

        public void setcValue(Double cValue) {
            this.cValue = cValue;
        }
    }

    public NestedClassesConversionsTest() {
    }

    @Before
    public void setUp() throws ConversionError {

        bToAConverterMock = Mockito.mock(TypeConverter.class);
        aToBConverterMock = Mockito.mock(TypeConverter.class);
        aToCConverterMock = Mockito.mock(TypeConverter.class);
        cToAConverterMock = Mockito.mock(TypeConverter.class);
        cToBConverterMock = Mockito.mock(TypeConverter.class);

        Mockito.when(bToAConverterMock.convert(b)).thenReturn(a);
        Mockito.when(aToBConverterMock.convert(a)).thenReturn(b);
        Mockito.when(aToCConverterMock.convert(a)).thenReturn(c);
        Mockito.when(cToAConverterMock.convert(c)).thenReturn(a);
        Mockito.when(cToBConverterMock.convert(c)).thenReturn(b);

        Mockito.when(bToAConverterMock.from()).thenReturn(B.class);
        Mockito.when(bToAConverterMock.to()).thenReturn(A.class);

        Mockito.when(cToAConverterMock.from()).thenReturn(C.class);
        Mockito.when(cToAConverterMock.to()).thenReturn(A.class);

        Mockito.when(aToBConverterMock.from()).thenReturn(A.class);
        Mockito.when(aToBConverterMock.to()).thenReturn(B.class);

        Mockito.when(aToCConverterMock.from()).thenReturn(A.class);
        Mockito.when(aToCConverterMock.to()).thenReturn(C.class);

        Mockito.when(cToBConverterMock.from()).thenReturn(C.class);
        Mockito.when(cToBConverterMock.to()).thenReturn(B.class);

        Mockito.when(cToAConverterMock.from()).thenReturn(C.class);
        Mockito.when(cToAConverterMock.to()).thenReturn(A.class);

    }

    @Test
    public void test_shallConvertCtoA() throws ConversionError {
        TypeConverterFactory factory = TypeConverterFactory.instance("check_shallConvertBtoA");
        // given
        factory.registerConverter(cToAConverterMock);

        // when
        A aFromC = factory.convert(c, A.class);

        // then
        Mockito.verify(cToAConverterMock).convert(c);
        assertEquals(a, aFromC);
    }

    @Test
    public void test_shallConvertCtoA_2() throws ConversionError {
        TypeConverterFactory factory = TypeConverterFactory.instance("test_shallConvertCtoA_2");
        // given
        factory.registerConverter(cToAConverterMock);
        factory.registerConverter(bToAConverterMock);

        // when
        A aFromC = factory.convert(c, A.class);

        // then
        Mockito.verify(cToAConverterMock).convert(c);
        assertEquals(a, aFromC);
    }

    @Test
    public void test_shallConvertBtoA() throws ConversionError {
        TypeConverterFactory factory = TypeConverterFactory.instance("test_shallConvertBtoA");
        // given
        factory.registerConverter(cToAConverterMock);
        factory.registerConverter(bToAConverterMock);

        // when
        A aFromB = factory.convert(b, A.class);

        // then
        Mockito.verify(bToAConverterMock).convert(b);
        assertEquals(a, aFromB);
    }

    @Test
    public void test_shallConvertAtoC() throws ConversionError {
        TypeConverterFactory factory = TypeConverterFactory.instance("test_shallConvertAtoC");
        // given

        factory.registerConverter(cToAConverterMock);
        factory.registerConverter(bToAConverterMock);
        factory.registerConverter(aToCConverterMock);
        factory.registerConverter(aToBConverterMock);

        // when
        A aFromB = factory.convert(b, A.class);

        // then
        Mockito.verify(bToAConverterMock).convert(b);
        assertEquals(a, aFromB);
    }

    @Test
    public void test_shallConvertAtoB() throws ConversionError {
        TypeConverterFactory factory = TypeConverterFactory.instance("test_shallConvertAtoB");
        // given

        factory.registerConverter(cToAConverterMock);
        factory.registerConverter(bToAConverterMock);
        factory.registerConverter(aToCConverterMock);
        factory.registerConverter(aToBConverterMock);

        // when
        B bFromA = factory.convert(a, B.class);

        // then
        Mockito.verify(aToBConverterMock).convert(a);
        assertEquals(b, bFromA);
    }
    
    @Test
    public void test_shallConvertCtoA_usingNested() throws ConversionError {
        TypeConverterFactory factory = TypeConverterFactory.instance("test_shallConvertCtoA_usingNested");
        // given
        Mockito.when(bToAConverterMock.convert(c)).thenReturn(a);
        factory.registerConverter(bToAConverterMock);

        // when
        A aFromC = factory.convert(c, A.class);

        // then
        Mockito.verify(bToAConverterMock).convert(c);
        assertEquals(a, aFromC);
    }

    @Test(expected = ConversionError.class)
    public void test_shallConvertAtoB_throw_ConversionError() throws ConversionError {
        TypeConverterFactory factory = TypeConverterFactory.instance("test_shallConvertAtoB_throw_ConversionError");
        // given
        factory.registerConverter(cToAConverterMock);
        factory.registerConverter(bToAConverterMock);
        factory.registerConverter(aToCConverterMock);

        B bFromA = null;
        try {
            // when
            bFromA = factory.convert(a, B.class);
        } finally {
            // then
            Mockito.verify(aToBConverterMock, Mockito.never()).convert(a);
            Mockito.verify(cToAConverterMock, Mockito.never()).convert(Matchers.any(C.class));
            Mockito.verify(bToAConverterMock, Mockito.never()).convert(Matchers.any(B.class));
            Mockito.verify(aToCConverterMock, Mockito.never()).convert(a);
            assertNull(bFromA);
        }
    }
}
