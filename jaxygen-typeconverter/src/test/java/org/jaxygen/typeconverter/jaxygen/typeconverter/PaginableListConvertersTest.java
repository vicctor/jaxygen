package org.jaxygen.typeconverter.jaxygen.typeconverter;

import java.util.ArrayList;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.jaxygen.collections.PartialArrayList;
import org.jaxygen.dto.collections.PaginableBaseRequestDTO;
import org.jaxygen.dto.collections.PaginableListResponseBaseDTO;
import org.jaxygen.typeconverter.BeanConverter;
import org.jaxygen.typeconverter.TypeConverterFactory;
import org.jaxygen.typeconverter.converters.PaginableToPartialConverter;
import org.jaxygen.typeconverter.converters.PartialToPaginableConverter;
import org.jaxygen.typeconverter.exceptions.ConversionError;

public class PaginableListConvertersTest 
    extends TestCase
{
    static TypeConverterFactory converters = new TypeConverterFactory();
    
    public static class DTO {
        private String value;
        private boolean question;

        public boolean isQuestion() {
            return question;
        }

        public void setQuestion(boolean question) {
            this.question = question;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public DTO() {
        }

        public DTO(String value, boolean question) {
            this.value = value;
            this.question = question;
        }
    }
    
    public static class DO {
        private String value;
        private boolean question;

        public DO() {
            this.question = false;
            this.value = null;
        }
        
        public DO(String value, boolean question) {
            this.value = value;
            this.question = question;
        }

        
        public boolean isQuestion() {
            return question;
        }

        public void setQuestion(boolean question) {
            this.question = question;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
    
    public static class PartialDO extends PartialArrayList<DO> {};
    public static class PaginableDTO extends PaginableListResponseBaseDTO<DTO> {};
    
    static class DTOtoDO extends BeanConverter<DTO, DO> {};
    static class DOtoDTO extends BeanConverter<DO, DTO> {};
    static class PartialToPaginable extends PartialToPaginableConverter<PartialDO, PaginableDTO> {
        PartialToPaginable() {
            super(converters);
        }
    };
    static class PaginableToPartial extends PaginableToPartialConverter<PaginableDTO, PartialDO> {
        PaginableToPartial() {
            super(converters);
        }
    };
    
    
    
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PaginableListConvertersTest( String testName )
    {
        super( testName );
        converters.registerConverter(new DTOtoDO());
        converters.registerConverter(new DOtoDTO());
        converters.registerConverter(new PartialToPaginable());
        converters.registerConverter(new PaginableToPartial());
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( PaginableListConvertersTest.class );
    }

    /**
     * Check the simple DTO to DO and back way conversions
     */
    public void test_simpleConversion() throws ConversionError
    {
        final String expectedValue = "expeced1";
        DTO dtoObject = new DTO();
        dtoObject.value = expectedValue;
        dtoObject.question = true;
        DO dbObject = converters.convert(dtoObject, DO.class);
        DTO dtoCopy = converters.convert(dbObject, DTO.class);
        
        assertEquals(expectedValue, dbObject.value);
        assertEquals(expectedValue, dtoCopy.value);
        assertTrue(dbObject.isQuestion());
        assertTrue(dtoCopy.isQuestion());
        
    }
    
    public void test_PartialToPaginable() throws ConversionError
    {
        PartialDO partial = new PartialDO();
        partial.setTotalSize(123);
        partial.add(new DO("v1", true));
        partial.add(new DO("v2", false));
        partial.add(new DO("v3", true));
        
        PaginableDTO paginable = converters.convert(partial, PaginableDTO.class);
        
        assertEquals(3, paginable.getElements().size());
        assertEquals(123, paginable.getSize());
        
        assertEquals("v1",paginable.getElements().get(0).getValue());
        assertEquals("v2",paginable.getElements().get(1).getValue());
        assertEquals("v3",paginable.getElements().get(2).getValue());
        
        assertEquals(true, paginable.getElements().get(0).isQuestion());
        assertEquals(false, paginable.getElements().get(1).isQuestion());
        assertEquals(true, paginable.getElements().get(2).isQuestion());
        
    }
    
    public void test_PaginableToPartial() throws ConversionError
    {
        PaginableDTO paginable = new PaginableDTO();
        paginable.setSize(123);
        paginable.setElements(new ArrayList<DTO>());
        paginable.getElements().add(new DTO("v1", true));
        paginable.getElements().add(new DTO("v2", false));
        paginable.getElements().add(new DTO("v3", true));
        
        PartialDO partial = converters.convert(paginable, PartialDO.class);
        
        assertEquals(3, partial.size());
        assertEquals(123, partial.getTotalSize());
        
        assertEquals("v1",partial.get(0).getValue());
        assertEquals("v2",partial.get(1).getValue());
        assertEquals("v3",partial.get(2).getValue());
        
        assertEquals(true, partial.get(0).isQuestion());
        assertEquals(false, partial.get(1).isQuestion());
        assertEquals(true, partial.get(2).isQuestion());   
    }
}
