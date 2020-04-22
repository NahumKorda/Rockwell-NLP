package com.itcag.util.txt;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;

import java.util.Collection;

/**
 * This class contains a collection of useful text manipulating methods.
 */
public final class TextToolbox {

    /**
     * Evaluates whether a string is null or empty.
     * @param input String to evaluate.
     * @return Boolean indicating whether the input string is null or empty.
     */
    public final static synchronized boolean isEmpty(String input) {
        return input == null || input.isEmpty();
    }
    
    /**
     * Evaluates whether the input string is null or empty. String consisting of one or more empty spaces is also considered empty.
     * @param input String to evaluate.
     * @return Boolean indicating whether the input string is null or empty.
     */
    public final static synchronized boolean isReallyEmpty(String input) {
        if (input == null) return true;
        input = input.trim();
        return input.isEmpty();
    }

    /**
     * Case insensitive version of the Java {@link java.lang.String#contains(java.lang.CharSequence)} method.
     * @param text String holding the string to be searched.
     * @param query String holding the query.
     * @return Boolean indicating whether query is contained in the text.
     */
    public final static synchronized boolean containsCaIn(String text, String query)  {
        return text.toLowerCase().contains(query.toLowerCase());
    }

    /**
     * Case insensitive version of the Java {@link java.lang.String#startsWith(java.lang.String)} method.
     * @param text String holding the string to be searched.
     * @param searchStr String holding the query.
     * @return Boolean indicating whether the text starts with the query.
     */
    public final static synchronized boolean startsWithCaIn(String text, String searchStr)  {
        return text.toLowerCase().startsWith(searchStr.toLowerCase());
    }

    /**
     * Case insensitive version of the Java {@link java.lang.String#endsWith(java.lang.String)} method.
     * @param text String holding the string to be searched.
     * @param searchStr String holding the query.
     * @return Boolean indicating whether the text ends with the query.
     */
    public final static synchronized boolean endsWithCaIn(String text, String searchStr)  {
        return text.toLowerCase().endsWith(searchStr.toLowerCase());
    }

    /**
     * Case insensitive version of the Java {@link java.lang.String#indexOf(java.lang.String)} method.
     * @param text String holding the string to be searched.
     * @param query String holding the query.
     * @return Integer indicating the index position of the query in the text..
     */
    public final static synchronized int indexOfCaIn(String text, String query)  {
        return text.toLowerCase().indexOf(query.toLowerCase());
    }
    
    /**
     * Case insensitive version of the Java {@link java.lang.String#lastIndexOf(java.lang.String)} method.
     * @param text String holding the string to be searched.
     * @param query String holding the query.
     * @return Integer indicating the last index position of the query in the text..
     */
    public final static synchronized int lastIndexOfCaIn(String text, String query)  {
        return text.toLowerCase().lastIndexOf(query.toLowerCase());
    }

    /**
     * A faster version of the {@link java.lang.String#replace(java.lang.CharSequence, java.lang.CharSequence)} method.
     * @param text String holding the text to be searched.
     * @param query String holding the text that is to be replaced.
     * @param replacement String holding the text that is to replace.
     * @return String holding the original string after replacing.
     */
    public final static synchronized String replace(String text, String query, String replacement) {
        if (TextToolbox.isEmpty(text) || TextToolbox.isEmpty(query) || replacement == null) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(query, start);
        if (end == -1) {
            return text;
        }
        int replLength = query.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= 64;
        StringBuilder stringBuilder = new StringBuilder(text.length() + increase);
        while (end != -1) {
            stringBuilder.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            end = text.indexOf(query, start);
        }
        stringBuilder.append(text.substring(start));
        return stringBuilder.toString();
    }

    /**
     * A case insensitive and faster version of the {@link java.lang.String#replace(java.lang.CharSequence, java.lang.CharSequence)} method.
     * @param text String holding the text to be searched.
     * @param query String holding the text that is to be replaced.
     * @param replacement String holding the text that is to replace.
     * @return String holding the original string after replacing.
     */
    public final static synchronized String replaceCaIn(String text, String query, String replacement) {
        if (TextToolbox.isEmpty(text) || TextToolbox.isEmpty(query) || replacement == null) {
            return text;
        }
        String lowerCaseText = text.toLowerCase();
        String lowerCaseSearchString = query.toLowerCase();
        int start = 0;
        int end = lowerCaseText.indexOf(lowerCaseSearchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = query.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= 64;
        StringBuilder stringBuilder = new StringBuilder(text.length() + increase);
        while (end != -1) {
            stringBuilder.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            end = lowerCaseText.indexOf(lowerCaseSearchString, start);
        }
        stringBuilder.append(text.substring(start));
        return stringBuilder.toString();
    }

    /**
     * This method first searches for primary query within text, and if it finds it, it searches within it for the secondary query to replace it.
     * For example:
     * text = "I live in the U.S.A."
     * primarySearchString = "U.S.A"
     * secondarySearchString = "."
     * replacement = ""
     * Outcome: "I live in the USA."
     * @param text String holding the text to be searched.
     * @param primaryQuery String holding the text within which the replacement will be made.
     * @param secondaryQuery String holding the text that is to be replaced.
     * @param replacement String holding the replacement text.
     * @return String holding the original string after replacing.
     */
    public final static synchronized String replaceWithin(String text, String primaryQuery, String secondaryQuery, String replacement) {
        if (TextToolbox.isEmpty(text) || TextToolbox.isEmpty(primaryQuery) || replacement == null) {
            return text;
        }
        String lowerCaseText = text.toLowerCase();
        String lowerCaseSearchString = primaryQuery.toLowerCase();
        int start = 0;
        int end = lowerCaseText.indexOf(lowerCaseSearchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = primaryQuery.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= 64;
        StringBuilder stringBuilder = new StringBuilder(text.length() + increase);
        while (end != -1) {
            stringBuilder.append(text.substring(start, end));
            String excerpt = text.substring(end, end + replLength);
            excerpt = replace(excerpt, secondaryQuery, replacement);
            stringBuilder.append(excerpt);
            start = end + replLength;
            end = lowerCaseText.indexOf(lowerCaseSearchString, start);
        }
        stringBuilder.append(text.substring(start));
        return stringBuilder.toString();
    }

    /**
     * Removes text enclosed in specified type of parentheses, together with the parentheses.
     * @param text String holding the text to be searched.
     * @param left String holding the left (opening) parenthesis.
     * @param right String holding the right (closing) parenthesis.
     * @return String holding the original text without parentheses and the text enclosed in them.
     */
    public final static synchronized String removeParentheses(String text, String left, String right) {
        
        if (isReallyEmpty(text)) return text;
        
        if (!text.contains(left)) return text;
        if (!text.contains(right)) return text;
        
        StringBuilder stringBuilder = new StringBuilder(text.length());
        
        while (text.contains(left)) {
            int start = text.indexOf(left);
            int end = text.indexOf(right);
            if (end == -1) break;
            if (end > start) {
                
                /**
                 * Check if there are parentheses within parentheses.
                 */
                int testStart = text.indexOf(left, start + 1);
                while (testStart > -1 && testStart < end) {
                    int testEnd = text.indexOf(right, end + 1);
                    if (testEnd == -1) break;
                    end = testEnd;
                    testStart = text.indexOf(left, testStart + 1);
                }
                
                stringBuilder.append(text.substring(0, start).trim());
                text = text.substring(end + 1);
            } else {
                stringBuilder.append(text.substring(0, start).trim());
                text = text.substring(start + 1);
            }
        }
        
        stringBuilder.append(text);
        String retVal = stringBuilder.toString();
        
        return retVal;
    
    }

    /**
     * Extract text enclosed in specified parentheses. If the original text contains multiple sets of parentheses, all of them are extracted.
     * @param text String holding the text to be searched.
     * @param left String holding the left (opening) parenthesis.
     * @param right String holding the right (closing) parenthesis.
     * @return Array list containing the extracted texts.
     */
    public final static synchronized ArrayList<String> extractParentheses(String text, String left, String right) {
        
        ArrayList<String> retVal = new ArrayList<>();
        
        if (isReallyEmpty(text)) return retVal;
        
        if (!text.contains(left)) return retVal;
        if (!text.contains(right)) return retVal;
        
        while (text.contains(left)) {
            int start = text.indexOf(left);
            int end = text.indexOf(right);
            if (end == -1) break;
            if (end > start) {
                
                /**
                 * Check if there are parentheses within parentheses.
                 */
                int testStart = text.indexOf(left, start + 1);
                while (testStart > -1 && testStart < end) {
                    int testEnd = text.indexOf(right, end + 1);
                    if (testEnd == -1) break;
                    end = testEnd;
                    testStart = text.indexOf(left, testStart + 1);
                }

                retVal.add(text.substring(start + 1, end).trim());
                text = text.substring(end + 1);
            
            } else {

                return retVal;
                
            }
        }
        
        return retVal;
    
    }

    /**
     * Concatenates multiple strings inserting the specified delimiter between them. 
     * @param collection Array list containing strings to be concatenated.
     * @param delimiter String holding the delimiter.
     * @return String holding the concatenated input strings.
     */
    public final static synchronized String joinWithDelimiter(Collection<String> collection, String delimiter) {
        String retVal = "";
        for (String item : collection) {
            if (retVal.isEmpty()) {
                retVal = item;
            } else {
                retVal += delimiter + item;
            }
        }
        return retVal;
    }

    /**
     * Capitalizes the first character of a text.
     * @param input Text to be capitalized.
     * @return Capitalized string.
     */
    public final static synchronized String capitalize(String input) {
        if (isEmpty(input)) return input;
        input = input.toLowerCase();
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public final static synchronized String repeat(int times, String toRepeat) {
        if (times < 1) return "";
        if (times == 1) return toRepeat;
        StringBuilder retVal = new StringBuilder(times * toRepeat.length() + 1);
        for (int i = 0; i < times; i++) {
            retVal.append(toRepeat);
        }
        return retVal.toString();
    }

    /**
     * Removes diacritic markings from text.
     * @param text String holding text from which the diacritics must be removed.
     * @return String holding the original text without diacritics.
     */
    public final static synchronized String removeDiacritics(String text) {
        if (isEmpty(text)) return text;
        return Normalizer.normalize(text, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    
    /**
     * Evaluates whether a string is null or empty.
     * @param input String builder to evaluate.
     * @return Boolean indicating whether the input string is null or empty.
     */
    public final static synchronized boolean isEmpty(StringBuilder input) {
        return input == null || input.length() == 0;
    }

    /**
     * Equivalent of the {@link java.lang.String#trim()} method for the {@link java.lang.StringBuilder StringBuilder} class.
     * @param text String builder to be trimmed.
     */
    public final static synchronized void trim(StringBuilder text) {

        if (isEmpty(text)) return;

        while (Character.isWhitespace(text.charAt(0))) {
            text.deleteCharAt(0);
            if (isEmpty(text)) return;
        }
        
        if (isEmpty(text)) return;

        while (Character.isWhitespace(text.charAt(text.length() - 1))) {
            text.deleteCharAt(text.length() - 1);
            if (isEmpty(text)) return;
        }
        
    }

    /**
     * Removes multiple consecutive empty spaces in a string builder leaving only single empty spaces behind.
     * @param input String builder to be fixed.
     */
    public final static synchronized void fixEmptySpaces(StringBuilder input) {
        
        if (isEmpty(input)) return;

        while (input.indexOf("  ") != -1) {
            replace(input, "  ", " ");
            if (isEmpty(input)) break;
        }

        if (isEmpty(input)) return;

        trim(input);
            
    }

    /**
     * Case insensitive version of the Java {@link java.lang.String#indexOf(java.lang.String)} method for the {@link java.lang.StringBuilder StringBuilder} class.
     * @param text String holding the string to be searched.
     * @param query String holding the query.
     * @return Integer indicating the index position of the query in the text.
     */
    public final static synchronized int indexOfCaIn(StringBuilder text, String query) {

        if (isEmpty(text) || TextToolbox.isEmpty(query)) {
            return -1;
        }
        
        StringBuilder lowerCaseText = new StringBuilder(text.length());
        lowerCaseText.append(text.toString().toLowerCase());

        return lowerCaseText.indexOf(query.toLowerCase());
        
    }
    
    /**
     * Case insensitive version of the Java {@link java.lang.String#indexOf(java.lang.String, int) (java.lang.String)} method for the {@link java.lang.StringBuilder StringBuilder} class.
     * @param text String holding the string to be searched.
     * @param query String holding the query.
     * @param start Integer indicating the index position from which the searching starts.
     * @return Integer indicating the index position of the query in the text.
     */
    public final static synchronized int indexOfCaIn(StringBuilder text, String query, int start) {

        if (isEmpty(text) || TextToolbox.isEmpty(query)) {
            return -1;
        }
        
        StringBuilder lowerCaseText = new StringBuilder(text.length());
        lowerCaseText.append(text.toString().toLowerCase());

        return lowerCaseText.indexOf(query.toLowerCase(), start);
        
    }
    
    /**
     * A version of the {@link java.lang.String#replace(java.lang.CharSequence, java.lang.CharSequence)} method for the {@link java.lang.StringBuilder StringBuilder} class.
     * @param text String holding the text to be searched.
     * @param query String holding the text that is to be replaced.
     * @param replacement String holding the text that is to replace.
     */
    public final static synchronized void replace(StringBuilder text, String query, String replacement) {

        if (isEmpty(text) || TextToolbox.isEmpty(query) || replacement == null) {
            return;
        }

        int start = text.indexOf(query, 0);
        if (start == -1) return;

        while (start != -1) {
            int end = start + query.length();
            text.replace(start, end, replacement);
            end = start + replacement.length();
            start = text.indexOf(query, end);
        }

    }

    /**
     * A case insensitive version of the {@link java.lang.String#replace(java.lang.CharSequence, java.lang.CharSequence)} method for the {@link java.lang.StringBuilder StringBuilder} class.
     * @param text String holding the text to be searched.
     * @param query String holding the text that is to be replaced.
     * @param replacement String holding the text that is to replace.
     */
    public final static synchronized void replaceCaIn(StringBuilder text, String query, String replacement) {
        
        if (isEmpty(text) || TextToolbox.isEmpty(query) || replacement == null) {
            return;
        }
        
        StringBuilder lowerCaseText = new StringBuilder(text.length());
        lowerCaseText.append(text.toString().toLowerCase());

        String lowerCaseSearchString = query.toLowerCase();
        
        int start = lowerCaseText.indexOf(lowerCaseSearchString);

        if (start == -1) return;

        int length = query.length();
        
        while (start != -1) {
            
            int end = start + length;
            
            text.replace(start, end, replacement);
            lowerCaseText.replace(start, end, replacement);

            end = start + replacement.length();
            start = lowerCaseText.indexOf(lowerCaseSearchString, end);
        
        }

    }

    /**
     * This method first searches for primary query within text, and if it finds it, it searches within it for the secondary query to replace it.
     * For example:
     * text = "I live in the U.S.A."
     * primarySearchString = "U.S.A"
     * secondarySearchString = "."
     * replacement = ""
     * Outcome: "I live in the USA."
     * @param text String holding the text to be searched.
     * @param primaryQuery String holding the text within which the replacement will be made.
     * @param secondaryQuery String holding the text that is to be replaced.
     * @param replacement String holding the replacement text.
     */
    public final static synchronized void replaceWithin(StringBuilder text, String primaryQuery, String secondaryQuery, String replacement) {
        
        if (isEmpty(text) || TextToolbox.isEmpty(primaryQuery) || replacement == null) {
            return;
        }
        
        StringBuilder lowerCaseText = new StringBuilder(text.length());
        lowerCaseText.append(text.toString().toLowerCase());
        
        String lowerCaseSearchString = primaryQuery.toLowerCase();

        int start = lowerCaseText.indexOf(lowerCaseSearchString);
        if (start == -1) return;

        int length = primaryQuery.length();
        
        while (start != -1) {
            
            int end = start + length;
            
            String excerpt = text.substring(start, end);
            excerpt = replace(excerpt, secondaryQuery, replacement);
            
            text.replace(start, end, excerpt);
            lowerCaseText.replace(start, end, excerpt);
            
            start = lowerCaseText.indexOf(lowerCaseSearchString, end);
        
        }
    
    }

    /**
     * Removes text enclosed in specified type of parentheses, together with the parentheses.
     * @param text String holding the text to be searched.
     * @param left String holding the left (opening) parenthesis.
     * @param right String holding the right (closing) parenthesis.
     */
    public final static synchronized void removeParentheses(StringBuilder text, String left, String right) {
        
        if (isEmpty(text) || TextToolbox.isEmpty(left) || TextToolbox.isEmpty(right)) {
            return;
        }
        
        if (text.indexOf(left) == -1) return;
        if (text.indexOf(right) == -1) return;

        int start = text.indexOf(left);
        
        while (start != -1) {
            int end = text.indexOf(right, start);
            if (end == -1) break;
            end++;
            if (end > start) text.replace(start, end, " ");
            start = text.indexOf(left, start + 1);
        }
        
    }

    /**
     * Capitalizes the first character of a text.
     * @param input Text to be capitalized.
     */
    public final static synchronized void capitalize(StringBuilder input) {
        if (isEmpty(input)) return;
        for (int i = 0; i < input.length(); i++) {
           char c = input.charAt(i);
           input.setCharAt(i, Character.toLowerCase(c));
        }
        input.replace(0, 1, input.substring(0, 1).toUpperCase());
    }

}
