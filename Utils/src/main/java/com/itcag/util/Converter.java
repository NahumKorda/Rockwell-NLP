/*
 *
 * Copyright 2020 IT Consulting AG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.itcag.util;

import com.itcag.util.txt.TextToolbox;

import java.text.SimpleDateFormat;

import java.sql.Timestamp;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>This class provides a collection of useful conversion methods. </p>
 */
public final class Converter {
    
    public final static String INPUT_DATE_FORMAT = "yyyy-MM-dd HH:mm";
    public final static String DISPLAY_DATE_FORMAT = "EEE, d MMM yyyy HH:mm";
    public final static String DISPLAY_TIME_FORMAT = "HH:mm";
    public final static String FILE_NAME_DATE_FORMAT = "yyyy-MM-dd";
    
    /**
     * @param input String presumably holding a Boolean value.
     * @return Boolean if conversion is successful, null otherwise.
     */
    public final static Boolean convertStringToBoolean(String input) {
        if (!TextToolbox.isEmpty(input)) {
            try {
                return Boolean.valueOf(input);
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }
    }
    
    /**
     * @param input String presumably holding a numeric value.
     * @return Integer if conversion is successful, null otherwise.
     */
    public final static Integer convertStringToInteger(String input) {
        if (!TextToolbox.isEmpty(input)) {
            try {
                return Integer.parseInt(input);
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }
    }
    
    /**
     * @param input String presumably holding a numeric value.
     * @return Long number if conversion is successful, null otherwise.
     */
    public final static Long convertStringToLong(String input) {
        if (!TextToolbox.isEmpty(input)) {
            input = input.trim();
            if (input.isEmpty()) return null;
            try {
                return Long.parseLong(input);
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }
    }
    
    /**
     * @param input String presumably holding a numeric value.
     * @return Double number if conversion is successful, null otherwise.
     */
    public final static Double convertStringToDouble(String input) {
        if (!TextToolbox.isEmpty(input)) {
            try {
                return Double.parseDouble(input);
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }
    }
    
    /**
     * @param input String presumably holding a timestamp.
     * @param format String holding the expected input format.
     * @return Timestamp if conversion is successful, null otherwise.
     */
    public final static Timestamp convertStringToTimestamp(String input, String format) {
        if (!TextToolbox.isEmpty(input)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            try {
                Date date = simpleDateFormat.parse(input);
                return new Timestamp(date.getTime());
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * @param input String presumably holding a date.
     * @param format String holding the expected input format.
     * @return Date if conversion is successful, null otherwise.
     */
    public final static Date convertStringToDate(String input, String format) {
        if (!TextToolbox.isEmpty(input)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            try {
                return simpleDateFormat.parse(input);
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * @param input Date to be formatted.
     * @param format String holding the desired output format.
     * @return String holding formatted date if formatting is successful, null otherwise.
     */
    public final static String formatDate(Date input, String format) {
        if (input != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            try {
                return simpleDateFormat.format(input);
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * @param timestamp Timestamp to be converted.
     * @param format String holding the desired output format.
     * @return String holding formatted date if formatting is successful, null otherwise.
     */
    public final static String formatTimestamp(Timestamp timestamp, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(timestamp);
    }

    /**
     * @param date Long number holding Java date.
     * @param format String holding the desired output format.
     * @return String holding formatted date if conversion is successful, null otherwise.
     */
    public final static String formatLongDate(long date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * @param time Long holding time in milliseconds.
     * @return String holding formatted time if formatting is successful, null otherwise.
     */
    public final static String formatLongTime(long time) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time), TimeUnit.MILLISECONDS.toMinutes(time) % TimeUnit.HOURS.toMinutes(1), TimeUnit.MILLISECONDS.toSeconds(time) % TimeUnit.MINUTES.toSeconds(1));
    }
    
    /**
     * @param input Double number, possibly decimal.
     * @return String holding formatted double number.
     */
    public final static String formatDouble(double input) {
        if (input == (long) input) {
            return String.format("%d", (long) input);
        } else {
            return String.format("%s", input);
        }        
    }
    
    /**
     * @param <T> Enum type.
     * @param someEnum enum type in all caps.
     * @param value String presumably holding an enum value. 
     * @return Corresponding enum if conversion is successful, null otherwise.
     */
    public static <T extends Enum<T>> T convertStringToEnumValue(Class<T> someEnum, String value) {
        if (someEnum != null && value != null ) {
            try {
                return Enum.valueOf(someEnum, value.trim().toUpperCase());
            } catch(IllegalArgumentException ex) {
                return null;
            }
        }
        return null;
    }

}
