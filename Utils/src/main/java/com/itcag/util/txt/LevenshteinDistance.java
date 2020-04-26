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

package com.itcag.util.txt;

/**
 * <p>This class calculates the difference between two strings known as the <i>Levenshtein distance</i>.</p>
 */
public final class LevenshteinDistance {

    /**
     * @param left String to be compared.
     * @param right String to be compared.
     * @return Integer indicating how different the two strings are (between 0 for identical strings and the length of longer string if strings have nothing in common).
     */
    public final static synchronized int LevenshteinDistance (String left, String right) {

        int leftLength = left.length() + 1;
        int rightLength = right.length() + 1;

        /** Array of distances */
        int[] cost = new int[leftLength];
        int[] newcost = new int[leftLength];

        /** Initial cost of skipping prefix in left string */
        for (int i = 0; i < leftLength; i++) cost[i] = i;

        /** Transformation cost for each letter in right string */
        for (int j = 1; j < rightLength; j++) {
            /** Initial cost of skipping prefix in right string */
            newcost[0] = j;

            /** Transformation cost for each letter in left string */
            for(int i = 1; i < leftLength; i++) {
                /** Matching current letters in both strings */
                int match = (left.charAt(i - 1) == right.charAt(j - 1)) ? 0 : 1;

                /** Computing cost for each transformation */
                int cost_replace = cost[i - 1] + match;
                int cost_insert  = cost[i] + 1;
                int cost_delete  = newcost[i - 1] + 1;

                /** Keep minimum cost */
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            /** Swap cost arrays */
            int[] swap = cost; cost = newcost; newcost = swap;

        }

        /** The distance is the cost for transforming all letters in both strings */
        return cost[leftLength - 1];

    }

}
