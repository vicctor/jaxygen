/*
 * Copyright 2017 xnet.
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
package org.jaxygen.netserviceapisample;

import org.jaxygen.customimplementation.CustomTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.jaxygen.converters.json.GSONBuilderFactory;
import org.jaxygen.converters.json.JSONBuilderRegistry;

/**
 *
 * @author xnet
 */
public class ServletListenerContext implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        setupGsonBuilder();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

    public void setupGsonBuilder() {
        JSONBuilderRegistry.setGSONBuilder(new GSONBuilderFactory() {
            GsonBuilder builder = null;

            @Override
            public GsonBuilder createBuilder() {
                if (builder == null) {
                    builder = new GsonBuilder()
                            .registerTypeAdapterFactory(new CustomTypeAdapterFactory());
                }
                return builder;
            }

            @Override
            public Gson build() {

                createBuilder = createBuilder();
                return createBuilder.create();
            }
            private GsonBuilder createBuilder;
        });
    }

}
