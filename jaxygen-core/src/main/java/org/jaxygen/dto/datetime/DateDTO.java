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
package org.jaxygen.dto.datetime;

import java.io.Serializable;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.annotations.QueryMessage;

/**
 *
 * @author imfact02
 */
@QueryMessage
@NetAPI(description="Class used to tranfer the Date information in Gregorian callendar (or similar notation)")
public class DateDTO implements Serializable{
  private static final long serialVersionUID = 123423423L;
    private int dayOfMonth;
    private int monthOfYear;
    private int year;

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getMonthOfYear() {
        return monthOfYear;
    }

    public void setMonthOfYear(int montOfYear) {
        this.monthOfYear = montOfYear;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
