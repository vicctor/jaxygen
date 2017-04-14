/*
 * Copyright 2017 Jakub Knast.
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
package org.jaxygen.netserviceapisample.business.dto.maps;

import java.util.HashMap;

/**
 *
 * @author jknast
 */
public class LanguageTranslationsMapBase {

    private HashMap<String, TranslationMap> languageTranslationsMap = new HashMap();

    public HashMap<String, TranslationMap> getLanguageTranslationsMap() {
        return languageTranslationsMap;
    }

    public void setLanguageTranslationsMap(HashMap<String, TranslationMap> languageTranslationsMap) {
        this.languageTranslationsMap = languageTranslationsMap;
    }

}
