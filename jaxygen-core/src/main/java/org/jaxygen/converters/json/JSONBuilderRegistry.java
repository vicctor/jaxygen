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
package org.jaxygen.converters.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author Artur
 */
public class JSONBuilderRegistry {

    //! default build in gson builder is created here.
    private static GSONBuilderFactory builder = new GSONBuilderFactory() {
        Gson gson = createBuilder()
                .create();
        @Override
        public Gson build() {
            return gson;
        }

        @Override
        public GsonBuilder createBuilder() {
            return  new GsonBuilder()
                .setDateFormat("dd-MM-yyyy HH:mm:ss");
        }
    };

    public static void setGSONBuilder(GSONBuilderFactory builder) {
        JSONBuilderRegistry.builder = builder;
    }
    
    public static GSONBuilderFactory getBuilder() {
        return JSONBuilderRegistry.builder;
    }
}
