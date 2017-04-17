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
package org.jaxygen.frame;

import java.util.List;
import org.jaxygen.frame.config.JaxygenModule;

/**
 *
 * @author Artur
 */
public abstract class JaxygenApplication {
    private static JaxygenApplication application;
    
    protected abstract List<JaxygenModule> onStart();
    
    protected abstract void onStop();

    protected JaxygenApplication() {
        application = this;
    }
    
    public static JaxygenApplication getInstance() {
        return application;
    }
}
