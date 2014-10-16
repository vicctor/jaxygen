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
package org.jaxygen.http;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.jaxygen.network.UploadedFile;
import org.jaxygen.exceptions.InvalidRequestParameter;


/**
 * This interface defines functionality of accessing HTTP request parameters.
 * 
 * @author Artur Keska
 * 
 */
public interface HttpRequestParams extends Serializable {

    public Map<String, String> getParameters();

    public Map<String, UploadedFile> getFiles();

    public String getAsString(String paramName, int minLen, int maxLen,
            boolean mandatory) throws InvalidRequestParameter;

    public String getAsString(String paramName, int minLen, int maxLen,
            String defaultValue) throws InvalidRequestParameter;

    public boolean getAsBoolean(String paramName, boolean mandatory)
            throws InvalidRequestParameter;

    public boolean getAsBooleanWithDefault(String paramName, boolean defaultVal)
            throws InvalidRequestParameter;

    public Date getAsDate(String paramName, boolean mandatory)
            throws InvalidRequestParameter;

    public int getAsInt(String paramName, int min, int max, int defaultValue)
            throws InvalidRequestParameter;

    public int getAsInt(String paramName, int min, int max, boolean mandatory)
            throws InvalidRequestParameter;

    public Object getAsEnum(String paramName, Class<?> enumClass,
            boolean mandatory) throws InvalidRequestParameter;

    public Object getAsEnum(String paramName, Class<?> enumClass,
            Object defaultValue) throws InvalidRequestParameter;

    public List<Integer> getAsListOfInt(String listName)
            throws InvalidRequestParameter;

    public List<String> getAsListOfStrings(String listName)
            throws InvalidRequestParameter;

    public List<?> getAsEnums(String name, Class<?> clazz)
            throws InvalidRequestParameter;
    
    public void dispose();
}
