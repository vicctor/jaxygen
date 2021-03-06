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

import java.util.List;

/**Extended list connection having the total size of the collection.
 * It is usable when returning only a part of data (usually as a database query
 * result with limits).
 *
 * @author Artur
 * @param <T> Class managed by the partial list.
 */
public interface PartialList<T> extends List<T> {
    public long getTotalSize();
    public void setTotalSize(long size);
}

