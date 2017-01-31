/*
 * Copyright 2017 Artur.
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
package org.jaxygen.apigin.beaninspector;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import static org.assertj.core.api.Assertions.*;
import org.jaxygen.apigin.beaninspector.data.ClassWithArrayOfPrimitiveTypes;
import org.jaxygen.apigin.beaninspector.data.ClassWithEnumField;
import org.jaxygen.apigin.beaninspector.data.ClassWithListTypes;
import org.jaxygen.apigin.beaninspector.data.SimpleClassWithSimpleFields;
import org.jaxygen.jaxygen.apigin.beaninspector.BeanInspector;
import org.jaxygen.jaxygen.apigin.beaninspector.exceptions.InspectionError;
import org.jaxygen.jaxygen.apigin.beaninspector.model.ArrayField;
import org.jaxygen.jaxygen.apigin.beaninspector.model.BooleanField;
import org.jaxygen.jaxygen.apigin.beaninspector.model.EnumField;
import org.jaxygen.jaxygen.apigin.beaninspector.model.IntegerField;
import org.jaxygen.jaxygen.apigin.beaninspector.model.ObjectDescriptor;
import org.jaxygen.jaxygen.apigin.beaninspector.model.StringField;
import org.junit.Test;

/**
 *
 * @author Artur
 */
public class BeanInspectorTest {

    public BeanInspectorTest() {
    }

    @Test
    public void should_twoStringClassesNotBeQueal() {
        // given
        StringField f1 = new StringField();
        StringField f2 = new StringField();
        f1.setName("N1");
        f2.setName("N2");

        // when
        boolean rc = f1.equals(f2);

        // then
        assertThat(rc).isFalse();
    }

    @Test
    public void should_twoStringClassesBeQueal() {
        // given
        StringField f1 = new StringField();
        StringField f2 = new StringField();
        f1.setName("N1");
        f2.setName("N1");

        // when
        boolean rc = f1.equals(f2);

        // then
        assertThat(rc).isTrue();
    }

    @Test
    public void should_StringAndIntNeverEquals() {
        // given
        StringField f1 = new StringField();
        IntegerField f2 = new IntegerField();
        f1.setName("1");
        f2.setName("1");

        // when
        boolean rc = f1.equals(f2);

        // then
        assertThat(rc).isFalse();
    }

    @Test
    public void should_TwoArrayTypesBeEqual() {
        // given
        ArrayField f1 = new ArrayField("arrayOfStrings", new StringField());
        ArrayField f2 = new ArrayField("arrayOfStrings", new StringField());

        // when
        boolean rc = f1.equals(f2) && f2.equals(f1);

        // then
        assertThat(rc).isTrue();
    }

    @Test
    public void should_IntegerAndStrignFieldShouldGenerateDifferentHashes() {
        // given
        StringField sf = new StringField();

        // when
        IntegerField ii = new IntegerField();

        // then
        assertThat(sf.hashCode())
                .isNotEqualTo(ii.hashCode());
    }

    @Test
    public void should_TwoArraysWithDifferentContentTypesNotBeEqual() {
        // given
        ArrayField f1 = new ArrayField("arrayOfStrings", new StringField());
        ArrayField f2 = new ArrayField("arrayOfStrings", new IntegerField());

        // when
        boolean rc = f1.equals(f2) || f2.equals(f1);

        // then
        assertThat(rc).isFalse();
    }

    @Test
    public void should_TwoArraysWithDifferentnamesNotBeEqual() {
        // given
        ArrayField f1 = new ArrayField("arrayOfStrings", new StringField());
        ArrayField f2 = new ArrayField("arrayOfInts", new StringField());

        // when
        boolean rc = f1.equals(f2) || f2.equals(f1);

        // then
        assertThat(rc).isFalse();
    }
    
    @Test
    public void shoudl_twoComplexArrayListaBeEqual() {
        // given
        ObjectDescriptor content1 = new ObjectDescriptor();
        content1.setName("SimpleClassWithSimpleFields");
        content1.setClassName("SimpleClassWithSimpleFields");
        content1.setClassFullName("org.jaxygen.beaninspector.data.SimpleClassWithSimpleFields");
        content1.setFields(Lists.newArrayList(
                new StringField("stringField"),
                new BooleanField("booleanField"),
                new BooleanField("booleanObjectField"),
                new IntegerField("intField"),
                new IntegerField("integerObjectField")));
        ArrayField array1 = new ArrayField("arrayListWithoutInformation", content1);
        
        ObjectDescriptor content2 = new ObjectDescriptor();
        content2.setName("SimpleClassWithSimpleFields");
        content2.setClassName("SimpleClassWithSimpleFields");
        content2.setClassFullName("org.jaxygen.beaninspector.data.SimpleClassWithSimpleFields");
        content2.setFields(Lists.newArrayList(
                new StringField("stringField"),
                new BooleanField("booleanField"),
                new BooleanField("booleanObjectField"),
                new IntegerField("intField"),
                new IntegerField("integerObjectField")));
        ArrayField array2 = new ArrayField("arrayListWithoutInformation", content2);
        
        // when
        boolean expected = array1.equals(array2);
        
        // then
        assertThat(expected)
                .isTrue();
    }
    
    @Test
    public void shoudl_twoComplexArrayListaNotBeEqual() {
        // given
        ObjectDescriptor content1 = new ObjectDescriptor();
        content1.setName("SimpleClassWithSimpleFields");
        content1.setClassName("SimpleClassWithSimpleFields");
        content1.setClassFullName("org.jaxygen.beaninspector.data.SimpleClassWithSimpleFields");
        content1.setFields(Lists.newArrayList(
                new StringField("stringField"),
                new BooleanField("booleanField"),
                new BooleanField("booleanObjectField"),
                new IntegerField("intField"),
                new IntegerField("integerObjectField")));
        ArrayField array1 = new ArrayField("a", content1);
        
        ObjectDescriptor content2 = new ObjectDescriptor();
        content2.setName("SimpleClassWithSimpleFields");
        content2.setClassName("SimpleClassWithSimpleFields");
        content2.setClassFullName("org.jaxygen.beaninspector.data.SimpleClassWithSimpleFields");
        content2.setFields(Lists.newArrayList(
                new StringField("stringField"),
                new BooleanField("booleanField"),
                new BooleanField("booleanObjectField"),
                new IntegerField("intField"),
                new IntegerField("integerObjectField")));
        ArrayField array2 = new ArrayField("b", content2);
        
        // when
        boolean expected = array1.equals(array2);
        
        // then
        assertThat(expected)
                .isFalse();
    }

    @Test
    public void should_TwoObjectsShallBeEqual() {
        // given
        ObjectDescriptor o1 = new ObjectDescriptor();
        o1.setName("n1");
        o1.setClassName("c");
        o1.setClassFullName("cc");
        o1.setFields(Lists.newArrayList(new IntegerField("i1"), new StringField("s1")));

        ObjectDescriptor o2 = new ObjectDescriptor();
        o2.setName("n1");
        o2.setClassName("c");
        o2.setClassFullName("cc");
        o2.setFields(Lists.newArrayList(new IntegerField("i1"), new StringField("s1")));

        // when
        boolean rc = o1.equals(o2) && o2.equals(o1);

        // then
        assertThat(rc).isTrue();
    }

    @Test
    public void should_TwoObjectsWthDifferentnamesAreNotEqual() {
        // given
        ObjectDescriptor o1 = new ObjectDescriptor();
        o1.setName("n");
        o1.setClassName("c");
        o1.setClassFullName("cc");
        o1.setFields(Lists.newArrayList(new IntegerField("i1"), new StringField("s1")));

        ObjectDescriptor o2 = new ObjectDescriptor();
        o2.setName("n1");
        o2.setClassName("c");
        o2.setClassFullName("cc");
        o2.setFields(Lists.newArrayList(new IntegerField("i1"), new StringField("s1")));

        // when
        boolean rc = o1.equals(o2) && o2.equals(o1);

        // then
        assertThat(rc).isFalse();
    }

    @Test
    public void should_TwoObjectsWthDifferentClassNamesAreNotEqual() {
        // given
        ObjectDescriptor o1 = new ObjectDescriptor();
        o1.setName("n1");
        o1.setClassName("c1");
        o1.setClassFullName("cc");
        o1.setFields(Lists.newArrayList(new IntegerField("i1"), new StringField("s1")));

        ObjectDescriptor o2 = new ObjectDescriptor();
        o2.setName("n1");
        o2.setClassName("c");
        o2.setClassFullName("cc");
        o2.setFields(Lists.newArrayList(new IntegerField("i1"), new StringField("s1")));

        // when
        boolean rc = o1.equals(o2) && o2.equals(o1);

        // then
        assertThat(rc).isFalse();
    }

    @Test
    public void should_TwoObjectsWthDifferentFullClassNamesAreNotEqual() {
        // given
        ObjectDescriptor o1 = new ObjectDescriptor();
        o1.setName("n1");
        o1.setClassName("c1");
        o1.setClassFullName("cc0");
        o1.setFields(Lists.newArrayList(new IntegerField("i1"), new StringField("s1")));

        ObjectDescriptor o2 = new ObjectDescriptor();
        o2.setName("n1");
        o2.setClassName("c");
        o2.setClassFullName("cc");
        o2.setFields(Lists.newArrayList(new IntegerField("i1"), new StringField("s1")));

        // when
        boolean rc = o1.equals(o2) && o2.equals(o1);

        // then
        assertThat(rc).isFalse();
    }

    @Test
    public void should_TwoObjectsWthDifferentFieldsAreNotEqual() {
        // given
        ObjectDescriptor o1 = new ObjectDescriptor();
        o1.setName("n1");
        o1.setClassName("c1");
        o1.setClassFullName("cc");
        o1.setFields(Lists.newArrayList(new IntegerField("i"), new StringField("s1")));

        ObjectDescriptor o2 = new ObjectDescriptor();
        o2.setName("n1");
        o2.setClassName("c");
        o2.setClassFullName("cc");
        o2.setFields(Lists.newArrayList(new IntegerField("i1"), new StringField("s1")));

        // when
        boolean rc = o1.equals(o2) && o2.equals(o1);

        // then
        assertThat(rc).isFalse();
    }
    @Test
    public void should_twoEnumWithDifferentValuesNotMatch() {
        // given
        EnumField f1 = new EnumField("a", "A", "B");
        EnumField f2 = new EnumField("a", "Z", "B");
        
        // when
        boolean expected = f1.equals(f2);
        
        // then
        assertThat(expected)
                .isFalse();
    }
    
    @Test
    public void should_twoEnumWithDifferentNamesNotMatch() {
        // given
        EnumField f1 = new EnumField("a", "A", "B");
        EnumField f2 = new EnumField("b", "A", "B");
        
        // when
        boolean expected = f1.equals(f2);
        
        // then
        assertThat(expected)
                .isFalse();
    }
    
    @Test
    public void should_twoEnumIdenticalEnumsMatch() {
        // given
        EnumField f1 = new EnumField("a", "A", "B");
        EnumField f2 = new EnumField("a", "A", "B");
        
        // when
        boolean expected = f1.equals(f2);
        
        // then
        assertThat(expected)
                .isTrue();
    }

    @Test
    public void should_describeSimpleClassWithFields() throws InspectionError {
        // given
        Class input = SimpleClassWithSimpleFields.class;

        // when
        ObjectDescriptor rc = (ObjectDescriptor)BeanInspector.inspect(input);

        // then
        assertThat(rc)
                .isNotNull();
        assertThat(rc.getClassName())
                .isEqualTo("SimpleClassWithSimpleFields");
        assertThat(rc.getClassFullName())
                .isEqualTo("org.jaxygen.apigin.beaninspector.data.SimpleClassWithSimpleFields");
        assertThat(rc.getType())
                .isEqualTo("OBJECT");
        assertThat(rc.getFields())
                .contains(
                        new StringField("stringField"),
                        new BooleanField("booleanField"),
                        new BooleanField("booleanObjectField"),
                        new IntegerField("intField"),
                        new IntegerField("integerObjectField")
                );
    }
    
    @Test
    public void should_describeClassWithEnum() throws InspectionError {
        // given
        Class input = ClassWithEnumField.class;

        // when
        ObjectDescriptor rc = (ObjectDescriptor)BeanInspector.inspect(input);

        // then
        assertThat(rc)
                .isNotNull();
        assertThat(rc.getClassName())
                .isEqualTo("ClassWithEnumField");
        assertThat(rc.getClassFullName())
                .isEqualTo("org.jaxygen.apigin.beaninspector.data.ClassWithEnumField");
        assertThat(rc.getType())
                .isEqualTo("OBJECT");
        assertThat(rc.getFields())
                .contains(
                        new EnumField("enumField", "E1", "E2", "E3"));
    }

    @Test
    public void should_describeArrayOfPromitives() throws InspectionError {
        // given
        Class input = ClassWithArrayOfPrimitiveTypes.class;

        // when
        ObjectDescriptor rc = (ObjectDescriptor)BeanInspector.inspect(input);

        // then
        assertThat(rc)
                .isNotNull();
        assertThat(rc.getClassName())
                .isEqualTo("ClassWithArrayOfPrimitiveTypes");
        assertThat(rc.getClassFullName())
                .isEqualTo("org.jaxygen.apigin.beaninspector.data.ClassWithArrayOfPrimitiveTypes");
        assertThat(rc.getType())
                .isEqualTo("OBJECT");
        assertThat(rc.getFields())
                .contains(
                        new ArrayField("arrayOfStrings", new StringField()),
                        new ArrayField("arrayOfIntegersObjects", new IntegerField()),
                        new ArrayField("arrayOfBooleans", new BooleanField()),
                        new ArrayField("arrayOfBooleanObjects", new BooleanField())
                );
    }

    @Test
    public void should_describeObjectWithListInstance() throws InspectionError {
        // given
        Class input = ClassWithListTypes.class;

        // when
        ObjectDescriptor rc = (ObjectDescriptor)BeanInspector.inspect(input);
        

        // then
        ObjectDescriptor content = new ObjectDescriptor();
        content.setName("SimpleClassWithSimpleFields");
        content.setClassName("SimpleClassWithSimpleFields");
        content.setClassFullName("org.jaxygen.apigin.beaninspector.data.SimpleClassWithSimpleFields");
        content.setFields(Lists.newArrayList(
                new StringField("stringField"),
                new BooleanField("booleanField"),
                new BooleanField("booleanObjectField"),
                new IntegerField("intField"),
                new IntegerField("integerObjectField")));
        ArrayField expected = new ArrayField("arrayListInstanceContent", content);

        assertThat(rc)
                .isNotNull();
        assertThat(rc.getName())
                .isEqualTo("ClassWithListTypes");
        assertThat(rc.getClassName())
                .isEqualTo("ClassWithListTypes");
        assertThat(rc.getClassFullName())
                .isEqualTo("org.jaxygen.apigin.beaninspector.data.ClassWithListTypes");
        assertThat(rc.getType())
                .isEqualTo("OBJECT");
        assertThat(rc.getFields())
                .contains(expected);
    }
    
    @Test
    public void should_describeObjectWithListObject() throws InspectionError {
        // given
        Class input = ClassWithListTypes.class;

        // when
        ObjectDescriptor rc = (ObjectDescriptor)BeanInspector.inspect(input);
        

        // then
        ObjectDescriptor content = new ObjectDescriptor();
        content.setName("SimpleClassWithSimpleFields");
        content.setClassName("SimpleClassWithSimpleFields");
        content.setClassFullName("org.jaxygen.apigin.beaninspector.data.SimpleClassWithSimpleFields");
        content.setFields(Lists.newArrayList(
                new StringField("stringField"),
                new BooleanField("booleanField"),
                new BooleanField("booleanObjectField"),
                new IntegerField("intField"),
                new IntegerField("integerObjectField")));
        ArrayField expected = new ArrayField("arrayListObjectContent", content);

        assertThat(rc)
                .isNotNull();
        assertThat(rc.getName())
                .isEqualTo("ClassWithListTypes");
        assertThat(rc.getClassName())
                .isEqualTo("ClassWithListTypes");
        assertThat(rc.getClassFullName())
                .isEqualTo("org.jaxygen.apigin.beaninspector.data.ClassWithListTypes");
        assertThat(rc.getType())
                .isEqualTo("OBJECT");
        assertThat(rc.getFields())
                .contains(expected);
    }
    
        @Test
    public void should_describeObjectWithListWithoutAnything() throws InspectionError {
        // given
        Class input = ClassWithListTypes.class;

        // when
        ObjectDescriptor rc = (ObjectDescriptor)BeanInspector.inspect(input);
        

        // then
        ObjectDescriptor content = new ObjectDescriptor();
        content.setName("SimpleClassWithSimpleFields");
        content.setClassName("SimpleClassWithSimpleFields");
        content.setClassFullName("org.jaxygen.apigin.beaninspector.data.SimpleClassWithSimpleFields");
        content.setFields(Lists.newArrayList(
                new StringField("stringField"),
                new BooleanField("booleanField"),
                new BooleanField("booleanObjectField"),
                new IntegerField("intField"),
                new IntegerField("integerObjectField")));
        ArrayField expected = new ArrayField("arrayListWithoutInformation", content);

        assertThat(rc)
                .isNotNull();
        assertThat(rc.getName())
                .isEqualTo("ClassWithListTypes");
        assertThat(rc.getClassName())
                .isEqualTo("ClassWithListTypes");
        assertThat(rc.getClassFullName())
                .isEqualTo("org.jaxygen.apigin.beaninspector.data.ClassWithListTypes");
        assertThat(rc.getType())
                .isEqualTo("OBJECT");
        assertThat(rc.getFields())
                .contains(expected);
            System.out.println("JSON:" + new GsonBuilder().setPrettyPrinting().create().toJson(rc));
    }

}
