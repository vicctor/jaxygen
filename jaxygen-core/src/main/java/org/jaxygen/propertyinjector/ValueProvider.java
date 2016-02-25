/*
 * Copyright 2016 Your Name <your.name at your.org>.
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
package org.jaxygen.propertyinjector;

/** 
 *
 */
public class ValueProvider<T> {
  public interface ValueFactory<T> {
    T value();
  }
  public static class ValueBuilder<T> {
    private final Class annotation;
    private ValueFactory factory;
    public ValueBuilder(Class annotation) {
      this.annotation = annotation;
    }
    public ValueBuilder<T> provide(ValueFactory<T> provider) {
      this.factory = provider;
      return this;
    }
    public Class getAnnotation() {
      return annotation;
    }    

    public ValueFactory<T> getFactory() {
      return factory;
    }    
  }
  public static <T> ValueBuilder<T> on(Class annotation) {
    return new ValueBuilder(annotation);
  }
}
