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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * <p>This class provides few frequently used mathematical methods.</p>
 */
public final class MathToolbox {
    
    /**
     * @return String consisting of 32 random single digit (positive) integers.
     */
    public final static String getRandomUUID() {
        return getRandomUUID(32);
    }
    
    /**
     * @param size Integer indicating the required number of random single digit (positive) integers.
     * @return String consisting of the specified number of random single digit (positive) integers.
     */
    public final static String getRandomUUID(int size) {
        
        Random randomizer = new Random();

        StringBuilder sb = new StringBuilder();
        while(sb.length() < size) sb.append(Integer.toHexString(randomizer.nextInt()));

        return sb.toString().substring(0, size);

    }

    /**
     * @param min Integer indicating the lower boundary of the output.
     * @param max Integer indicating the upper boundary of the output.
     * @return Random integer between the specified boundaries.
     */
    public final static int getRandomNumber(int min, int max) {
        Random r = new Random();
	return r.nextInt((max - min) + 1) + min;
    }
    
    /**
     * @param value Double holding a decimal value to be rounded.
     * @param places Integer indicating the number of decimal places to which the value must be rounded.
     * @return Double rounded to the specified number of decimal places.
     */
    public static double roundDouble(double value, int places) {
        
        if (places < 0) throw new IllegalArgumentException();

        try {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        } catch (Exception ex) {
            return 0.00;
        }

    }

    /**
     * @param x Double holding a value for which the sigmoid function must be calculated.
     * @return Double holding the sigmoid value of the input.
     */
    public static double sigmoid(double x) {
        return (1/( 1 + Math.pow(Math.E,(-1*x))));
    }
    
}
