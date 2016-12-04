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
package org.jaxygen.collections;

public class Pair<KeyType, ValueType> {
    private KeyType key;
    private ValueType value;

    public Pair() {
    }
    
    public Pair(final KeyType first, final ValueType second) {
        this.key = first;
        this.value = second;
    }

    public KeyType getKey() {
        return key;
    }

    public void setKey(KeyType key) {
        this.key = key;
    }
    
    public Pair<KeyType, ValueType> withKey(KeyType key) {
        this.key = key;
        return this;
    }
    
    public Pair<KeyType, ValueType> withValue(ValueType value) {
        this.value = value;
        return this;
    }

    public ValueType getValue() {
        return value;
    }

    public void setValue(ValueType value) {
        this.value = value;
    }
    
    

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;

        if (key != null ? !key.equals(pair.key) : pair.key != null) return false;
        if (value != null ? !value.equals(pair.value) : pair.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}