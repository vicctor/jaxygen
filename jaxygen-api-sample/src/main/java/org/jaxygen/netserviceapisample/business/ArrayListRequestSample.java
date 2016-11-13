/*
 * Copyright 2016 Jakub Knast.
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
package org.jaxygen.netserviceapisample.business;

import org.jaxygen.netserviceapisample.business.dto.lists.GenericArrayListRequestDTO;
import org.jaxygen.netserviceapisample.business.dto.lists.GenericArrayListResponseDTO;
import org.jaxygen.netserviceapisample.business.dto.lists.ArrayListExampleCreateRequestDTO;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.Status;
import org.jaxygen.netserviceapisample.business.dto.lists.ArrayListExampleResponseDTO;
import org.jaxygen.netserviceapisample.business.dto.lists.ArrayListWithArrayListRequestDTO;
import org.jaxygen.netserviceapisample.business.dto.lists.ArrayListOfObjectsRequestDTO;
import org.jaxygen.netserviceapisample.business.dto.lists.SimpleArrayListRequestDTO;
import org.jaxygen.util.BeanUtil;

/**
 *
 * @author jknast
 */
public class ArrayListRequestSample {

  @NetAPI(description = "Very simple request contains ArrayList of strings.",
          status = Status.GenerallyAvailable,
          version = "1.0.5"
  )
  public SimpleArrayListRequestDTO simpleArrayListRequest(SimpleArrayListRequestDTO arrayListRequestDTO) {
    return arrayListRequestDTO;
  }

  @NetAPI(description = "Request contains ArrayList of objects, UserDTO in this case.",
          status = Status.GenerallyAvailable,
          version = "1.0.5"
  )
  public ArrayListOfObjectsRequestDTO arrayListOfObjectsRequest(ArrayListOfObjectsRequestDTO listOfObjectsRequestDTO) {
    return listOfObjectsRequestDTO;
  }

  @NetAPI(description = "This sample show you ArrayList with field type ArrayList",
          status = Status.GenerallyAvailable,
          version = "1.0.5"
  )
  public ArrayListWithArrayListRequestDTO arrayListWithArrayListRequest(ArrayListWithArrayListRequestDTO arrayListWithArrayListRequestDTO) {
    return arrayListWithArrayListRequestDTO;
  }

  @NetAPI(description = "This sample show you that request and response calsses can extend from base class with ArrayList",
          status = Status.GenerallyAvailable,
          version = "1.0.5"
  )
  public ArrayListExampleResponseDTO extendsBaseClassWithArrayList(ArrayListExampleCreateRequestDTO request) {
    ArrayListExampleResponseDTO responseDTO = new ArrayListExampleResponseDTO();
    BeanUtil.translateBean(request, responseDTO);
    return responseDTO;
  }

  @NetAPI(description = "This sample presents that you can use generic types for ArrayList",
          status = Status.GenerallyAvailable,
          version = "1.0.5"
  )
  public GenericArrayListResponseDTO genericArrayListRequest(GenericArrayListRequestDTO request) {
    GenericArrayListResponseDTO responseDTO = new GenericArrayListResponseDTO();
    BeanUtil.translateBean(request, responseDTO);
    return responseDTO;
  }
}
