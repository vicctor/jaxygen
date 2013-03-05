/*
 * Copyright 2013 ak.
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
package org.jaxygen.dto.datetime;

import java.io.Serializable;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.QueryMessage;

/**
 *
 * @author ak
 */
@QueryMessage
@NetAPI(description="Class handles the timestamp")
public class TimestampDTO implements Serializable{
    private DateDTO date;
    private TimeDTO time;

    public DateDTO getDate() {
        return date;
    }

    public void setDate(DateDTO date) {
        this.date = date;
    }

    public TimeDTO getTime() {
        return time;
    }

    public void setTime(TimeDTO time) {
        this.time = time;
    }
}
