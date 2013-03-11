/*
 * Copyright 2013 imfact02.
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
package org.jaxygen.dto.collections;

import java.io.Serializable;
import java.util.List;

/**This is a base class for paginable collection responses
 * When building an response one has to create a derived class
 * of this having concrete generic type of managed collection.
 *
 * @author ak
 */
public class PaginableListResponseBaseDTO<T> implements Serializable{
    private List<T> elements;
    private long size;

    public List<T> getElements() {
        return elements;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
