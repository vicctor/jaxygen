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

import org.jaxygen.netserviceapisample.business.dto.maps.HashMapSimpleRequestDTO;
import org.jaxygen.netserviceapisample.business.dto.maps.HashMapSimpleResponseDTO;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.Status;
import org.jaxygen.netserviceapisample.business.dto.maps.HashMapComplexRequestDTO;
import org.jaxygen.netserviceapisample.business.dto.maps.HashMapComplexResponseDTO;
import org.jaxygen.util.BeanUtil;

/**
 *
 * @author jknast
 */
public class MapRequestSample {

  @NetAPI(description = "",
          status = Status.GenerallyAvailable,
          version = "1.0.6"
  )
  public HashMapSimpleResponseDTO simpeHashmapRequest(HashMapSimpleRequestDTO request) {
    HashMapSimpleResponseDTO responseDTO = new HashMapSimpleResponseDTO();
    BeanUtil.translateBean(request, responseDTO);
    return responseDTO;
  }  
  
  @NetAPI(description = "",
          status = Status.GenerallyAvailable,
          version = "1.0.6"
  )
  public HashMapComplexResponseDTO complexHashmapRequest(HashMapComplexRequestDTO request) {
    HashMapComplexResponseDTO responseDTO = new HashMapComplexResponseDTO();
    BeanUtil.translateBean(request, responseDTO);
    return responseDTO;
  }
}
