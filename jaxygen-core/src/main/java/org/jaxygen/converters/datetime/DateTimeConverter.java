/*
 * Copyright 2013 imfact02.
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
package org.jaxygen.converters.datetime;

import java.util.Calendar;
import org.jaxygen.dto.datetime.DateDTO;
import org.jaxygen.dto.datetime.TimeDTO;
import org.jaxygen.dto.datetime.TimestampDTO;

/**
 *
 * @author ak
 */
public class DateTimeConverter {
    public static TimeDTO calendarToTime(Calendar calendar) {
        TimeDTO t = new TimeDTO();
        t.setHour(calendar.get(Calendar.HOUR_OF_DAY));
        t.setMinute(calendar.get(Calendar.MINUTE));
        t.setSec(calendar.get(Calendar.SECOND));
        t.setTimeZone(calendar.getTimeZone().getID());
        return t;
    }
    
    public static DateDTO calendarToDate(Calendar calendar) {
        DateDTO d = new DateDTO();
        d.setYear(calendar.get(Calendar.YEAR));
        d.setMontOfYear(calendar.get(Calendar.MONTH));
        d.setDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
        return d;
    }
    
    public static TimestampDTO calendarToTimestamp(Calendar calendar) {
        TimestampDTO t = new TimestampDTO();
        t.setTime(calendarToTime(calendar));
        t.setDate(calendarToDate(calendar));
        return t;
    }
}
