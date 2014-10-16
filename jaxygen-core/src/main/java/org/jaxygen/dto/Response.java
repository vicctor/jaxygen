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
package org.jaxygen.dto;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Artur Keska
 */
@XmlRootElement
public class Response implements Serializable {

    public final static long serialVersionUID = 53489753810234L;

    public static class ResponseDTO implements Serializable {

        public final static long serialVersionUID = 53489753810231L;
        private String responseClass;
        private Object responseObject;

        public ResponseDTO() {
        }

        private ResponseDTO(Class<?> responseClass, Object o) {
            if (o != null) {
                responseObject = o;
                this.responseClass = o.getClass().getCanonicalName();
            } else {
                if (responseClass != null) {
                    this.responseClass = responseClass.getCanonicalName();
                } else {
                    this.responseClass = "null";
                }
                responseObject = null;
            }

        }

        public String getResponseClass() {
            return responseClass;
        }

        public void setResponseClass(String responseClass) {
            this.responseClass = responseClass;
        }

        public Object getResponseObject() {
            return responseObject;
        }

        public void setResponseObject(Object responseObject) {
            this.responseObject = responseObject;
        }
    }

    private ResponseDTO dto;

    public Response() {
    }

    public Response(Class<?> responseClass, Object o) {
        dto = new ResponseDTO(responseClass, o);
    }

    public ResponseDTO getDto() {
        return dto;
    }

    public void setDto(ResponseDTO dto) {
        this.dto = dto;
    }
}
