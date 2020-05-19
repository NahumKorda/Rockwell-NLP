package com.itcag.rockwell.semantex;

import java.util.ArrayList;

public class Tests {

    public static ArrayList<Test> getNERDateTests() {
        
        ArrayList<Test> retVal = new ArrayList<>();
        
        String text = "Alicja was born on June 26, 1982 in Warsaw.";
        Test test = new Test(text, null, 4, 7);
        retVal.add(test);
        
        text = "Alicja was born on 26th June, 1982 in Warsaw.";
        test = new Test(text, null, 4, 7);
        retVal.add(test);

        text = "Alicja was born on 26/06/1982 in Warsaw.";
        test = new Test(text, null, 4, 4);
        retVal.add(test);

        text = "Alicja was born on 1982-06-26 in Warsaw.";
        test = new Test(text, null, 4, 4);
        retVal.add(test);

        text = "Election day is on Monday, March 2, 2020.";
        test = new Test(text, null, 4, 9);
        retVal.add(test);

        text = "Election day is on Monday, 2 March 2020.";
        test = new Test(text, null, 4, 8);
        retVal.add(test);

        text = "THIS AGREEMENT made as of the 22nd day of February, 2020, between IT Consulting AG Ltd. a corporation incorporated under the laws of the Province of Ontario, and having its principal place of business at Warsaw, Poland (the \"Employer\"); and Alicja Gruzdz, of the City of Toronto in the Province of Ontario (the \"Employee\").";
        test = new Test(text, null, 6, 11);
        retVal.add(test);

        return retVal;
        
    }
    
    public static ArrayList<Test> getNERPersonTests() {
        
        ArrayList<Test> retVal = new ArrayList<>();
        
        String text = "Dr. Alicja Gruzdz from Warsaw.";
        Test test = new Test(text, null, 0, 2);
        retVal.add(test);
        
        text = "Dr Alicja Gruzdz from Warsaw.";
        test = new Test(text, null, 0, 2);
        retVal.add(test);

        text = "Alicja J. Gruzdz from Warsaw.";
        test = new Test(text, null, 0, 2);
        retVal.add(test);

        text = "Alicja J Gruzdz from Warsaw.";
        test = new Test(text, null, 0, 2);
        retVal.add(test);

        text = "Dr. Alicja J. Gruzdz from Warsaw.";
        test = new Test(text, null, 0, 3);
        retVal.add(test);

        text = "Dr Alicja J Gruzdz from Warsaw.";
        test = new Test(text, null, 0, 3);
        retVal.add(test);

        text = "Today Anna Eleanor Roosevelt Jr. inaugurated a ship.";
        test = new Test(text, null, 1, 4);
        retVal.add(test);

        text = "Today Mrs. Anna Eleanor Roosevelt Jr. inaugurated a ship.";
        test = new Test(text, null, 1, 5);
        retVal.add(test);

        text = "THIS AGREEMENT made as of the 22nd day of February, 2020, between IT Consulting AG Ltd. a corporation incorporated under the laws of the Province of Ontario, and having its principal place of business at Warsaw, Poland (the \"Employer\"); and Alicja Gruzdz, of the City of Toronto in the Province of Ontario (the \"Employee\").";
        test = new Test(text, null, 45, 46);
        retVal.add(test);

        return retVal;
        
    }

    public static ArrayList<Test> getNERCorporationTests() {
        
        ArrayList<Test> retVal = new ArrayList<>();
        
        String text = "The New York Times Company said it expects advertising revenue to fall between 50-55% year-over-year in the second quarter as impacts of the pandemic are hitting demand for advertisers.";
        Test test = new Test(text, null, 0, 2);
        retVal.add(test);
        
        return retVal;
        
    }
    
    public static ArrayList<Test> getNERCurrencyTests() {
        
        ArrayList<Test> retVal = new ArrayList<>();
        
        String text = "It costs $15.";
        Test test = new Test(text, null, 2, 3);
        retVal.add(test);
        
        text = "Investments passed USD 14 billion.";
        test = new Test(text, null, 2, 3);
        retVal.add(test);

        text = "Capitalization swelled by 132.929 billion rupees to 7.004 trillion rupees.";
        test = new Test(text, null, 3, 5);
        retVal.add(test);

        text = "Capitalization swelled by 132.929 billion rupees to 7.004 trillion rupees.";
        test = new Test(text, null, 7, 9);
        retVal.add(test);

        text = "Capitalization swelled by 1.329 billion U.S. dollars to 70.044 billion U.S. dollars.";
        test = new Test(text, null, 3, 6);
        retVal.add(test);
        
        text = "Capitalization swelled by 1.329 billion U.S. dollars to 70.044 billion U.S. dollars.";
        test = new Test(text, null, 8, 11);
        retVal.add(test);
        
        text = "I have 20 dollars.";
        test = new Test(text, null, 2, 3);
        retVal.add(test);
        
        text = "I have 20 pounds.";
        test = new Test(text, null, 2, 3);
        retVal.add(test);
        
        text = "I have 20 euros.";
        test = new Test(text, null, 2, 3);
        retVal.add(test);
        
        text = "I have $20.";
        test = new Test(text, null, 2, 3);
        retVal.add(test);
        
        text = "I have 20$.";
        test = new Test(text, null, 2, 3);
        retVal.add(test);
        
        text = "I have 20¢.";
        test = new Test(text, null, 2, 3);
        retVal.add(test);
        
        text = "I have ¢20.";
        test = new Test(text, null, 2, 3);
        retVal.add(test);
        
        text = "I have £20.";
        test = new Test(text, null, 2, 3);
        retVal.add(test);
        
        text = "I have 20£.";
        test = new Test(text, null, 2, 3);
        retVal.add(test);
        
        text = "I have €20.";
        test = new Test(text, null, 2, 3);
        retVal.add(test);
        
        text = "I have 20€.";
        test = new Test(text, null, 2, 3);
        retVal.add(test);
        
        return retVal;
        
    }
    
}
