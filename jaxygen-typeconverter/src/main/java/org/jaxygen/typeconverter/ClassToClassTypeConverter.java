package org.jaxygen.typeconverter;

import org.jodah.typetools.TypeResolver;

/**
 * The abstract implementation of the TypeConverter which is a base for class to
 * class convertion. Class implements protected methods from() and to()
 * returning the class types of the corresponding convertion subjects.
 *
 * @author Artur Keska <artur.keska@xdsnet.pl>
 * @param <FROM>
 * @param <TO>
 */
public abstract class ClassToClassTypeConverter<FROM, TO> implements TypeConverter<FROM, TO> {

  private final Class<FROM> fromClass;
  private final Class<TO>   toClass;
  
  @SuppressWarnings("unchecked")
  public ClassToClassTypeConverter() {
    Class<?>[] types = TypeResolver.resolveRawArguments(ClassToClassTypeConverter.class, this.getClass());
    fromClass = (Class<FROM>) types[0];
    toClass = (Class<TO>) types[1];
  }
    
    @SuppressWarnings({"unchecked"})
    @Override
    public Class<FROM> from() {
        return fromClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<TO> to() {
       return toClass;
    }
}
