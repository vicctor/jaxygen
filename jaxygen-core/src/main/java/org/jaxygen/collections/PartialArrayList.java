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
package org.jaxygen.collections;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Artur
 * @param <T>
 */
public abstract class PartialArrayList<T> extends ArrayList<T> implements PartialList<T> {
    private long totalSize;

    public PartialArrayList() {
    }
    
    public PartialArrayList(Collection<? extends T> c) {
        super(c);
    }

    public PartialArrayList(Collection<? extends T> c, int totalSize) {
        super(c);
        this.totalSize = totalSize;
    }
    
    public long getTotalSize() {
        return totalSize;
    }
    
    public void setTotalSize(long size) {
        totalSize = size;
    }
}
