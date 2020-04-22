package com.itcag.rockwell.tagger;

import java.util.ArrayList;

public class Tests {

    public static ArrayList<Test> getAdverbialPhrases() {
        
        ArrayList<Test> retVal = new ArrayList<>();

        String rule = "@cain+prefix :executed+adverb | test ";

        String text = "Efficiently executed performance.";
        Test test = new Test(text, rule, 0, 1);
        retVal.add(test);
        
        text = "Truly rapidly executed performance.";
        test = new Test(text, rule, 0, 2);
        retVal.add(test);

        text = "Efficiently and rapidly executed performance.";
        test = new Test(text, rule, 0, 3);
        retVal.add(test);

        text = "Efficiently and truly rapidly executed performance.";
        test = new Test(text, rule, 0, 4);
        retVal.add(test);

        text = "Efficiently, rapidly executed performance.";
        test = new Test(text, rule, 0, 3);
        retVal.add(test);

        text = "Efficiently, truly rapidly executed performance.";
        test = new Test(text, rule, 0, 4);
        retVal.add(test);

        text = "Efficiently, smoothly and rapidly executed performance.";
        test = new Test(text, rule, 0, 5);
        retVal.add(test);

        text = "Efficiently, smoothly and truly rapidly executed performance.";
        test = new Test(text, rule, 0, 6);
        retVal.add(test);

        text = "Efficiently, really smoothly and truly rapidly executed performance.";
        test = new Test(text, rule, 0, 7);
        retVal.add(test);

        return retVal;
        
    }
    
    public static ArrayList<Test> getAdjectivePhrases() {
        
        ArrayList<Test> retVal = new ArrayList<>();

        String rule = "@cain+prefix :performance+adjective | test ";

        String text = "Terrible performance.";
        Test test = new Test(text, rule, 0, 1);
        retVal.add(test);
        
        text = "Really terrible performance.";
        test = new Test(text, rule, 0, 2);
        retVal.add(test);

        text = "Really decisively terrible performance.";
        test = new Test(text, rule, 0, 3);
        retVal.add(test);

        text = "Terrible ghastly performance.";
        test = new Test(text, rule, 0, 2);
        retVal.add(test);
        
        text = "Really terrible ghastly performance.";
        test = new Test(text, rule, 0, 3);
        retVal.add(test);
        
        text = "Terrible and ghastly performance.";
        test = new Test(text, rule, 0, 3);
        retVal.add(test);
        
        text = "Really terrible and ghastly performance.";
        test = new Test(text, rule, 0, 4);
        retVal.add(test);
        
        text = "Terrible, ghastly performance.";
        test = new Test(text, rule, 0, 3);
        retVal.add(test);
        
        text = "Really terrible, ghastly performance.";
        test = new Test(text, rule, 0, 4);
        retVal.add(test);
        
        text = "Really terrible and truly ghastly performance.";
        test = new Test(text, rule, 0, 5);
        retVal.add(test);
        
        return retVal;
        
    }
    
    public static ArrayList<Test> getNounPhrases() {
        
        ArrayList<Test> retVal = new ArrayList<>();

        String rule = "@cain+prefix{x} :stable+noun | test ";

        String text = "Horse stable.";
        Test test = new Test(text, rule, 0, 1);
        retVal.add(test);
        
        text = "His stable.";
        test = new Test(text, rule, 0, 1);
        retVal.add(test);

        text = "The stable.";
        test = new Test(text, rule, 0, 1);
        retVal.add(test);

        text = "Large stable.";
        test = new Test(text, rule, 0, 1);
        retVal.add(test);

        text = "His horse stable.";
        test = new Test(text, rule, 0, 2);
        retVal.add(test);

        text = "The horse stable.";
        test = new Test(text, rule, 0, 2);
        retVal.add(test);

        text = "Large horse stable.";
        test = new Test(text, rule, 0, 2);
        retVal.add(test);

        text = "Large and roomy horse stable.";
        test = new Test(text, rule, 0, 4);
        retVal.add(test);

        text = "Very large and roomy horse stable.";
        test = new Test(text, rule, 0, 5);
        retVal.add(test);

        text = "His large stable.";
        test = new Test(text, rule, 0, 2);
        retVal.add(test);

        text = "A large stable.";
        test = new Test(text, rule, 0, 2);
        retVal.add(test);

        text = "His large horse stable.";
        test = new Test(text, rule, 0, 3);
        retVal.add(test);

        text = "A large horse stable.";
        test = new Test(text, rule, 0, 3);
        retVal.add(test);

        text = "What a stable.";
        test = new Test(text, rule, 0, 2);
        retVal.add(test);

        text = "What a horse stable.";
        test = new Test(text, rule, 0, 3);
        retVal.add(test);

        text = "What a beautiful stable.";
        test = new Test(text, rule, 0, 3);
        retVal.add(test);

        text = "What a beautiful horse stable.";
        test = new Test(text, rule, 0, 4);
        retVal.add(test);

        text = "Three times my stable.";
        test = new Test(text, rule, 0, 3);
        retVal.add(test);

        text = "Three times my horse stable.";
        test = new Test(text, rule, 0, 4);
        retVal.add(test);

        text = "Three times my largest horse stable.";
        test = new Test(text, rule, 0, 5);
        retVal.add(test);

        text = "John's stable.";
        test = new Test(text, rule, 0, 2);
        retVal.add(test);

        text = "John's largest stable.";
        test = new Test(text, rule, 0, 3);
        retVal.add(test);

        text = "John's horse stable.";
        test = new Test(text, rule, 0, 3);
        retVal.add(test);

        text = "John's largest horse stable.";
        test = new Test(text, rule, 0, 4);
        retVal.add(test);

        text = "Lord Wellington's horse stable.";
        test = new Test(text, rule, 0, 4);
        retVal.add(test);

        text = "My brother's horse stable.";
        test = new Test(text, rule, 0, 4);
        retVal.add(test);

        text = "My older brother's horse stable.";
        test = new Test(text, rule, 0, 5);
        retVal.add(test);

        text = "My brother's largest horse stable.";
        test = new Test(text, rule, 0, 5);
        retVal.add(test);

        text = "My older brother's largest horse stable.";
        test = new Test(text, rule, 0, 6);
        retVal.add(test);

        text = "Twice Lord Wellington's horse stable.";
        test = new Test(text, rule, 0, 5);
        retVal.add(test);

        text = "Twice Lord Wellington's largest horse stable.";
        test = new Test(text, rule, 0, 6);
        retVal.add(test);

        text = "Twice my brother's horse stable.";
        test = new Test(text, rule, 0, 5);
        retVal.add(test);

        text = "Twice my older brother's horse stable.";
        test = new Test(text, rule, 0, 6);
        retVal.add(test);

        text = "Twice my brother's largest horse stable.";
        test = new Test(text, rule, 0, 6);
        retVal.add(test);

        text = "Twice my older brother's largest horse stable.";
        test = new Test(text, rule, 0, 7);
        retVal.add(test);

        text = "Cattle and horse stable.";
        test = new Test(text, rule, 0, 3);
        retVal.add(test);

        text = "Cattle, donkey and horse stable.";
        test = new Test(text, rule, 0, 5);
        retVal.add(test);

        return retVal;
        
    }
    
    public static ArrayList<Test> getAdHocTests() {
        
        ArrayList<Test> retVal = new ArrayList<>();

        String rule = "@pos+prefix+suffix :PRP+noun+noun / @cain :at ; @cain :all / @type :VV ; @pos :TO0 / @cain :due ; @cain :to | valid_noun_phrase";

        String text = "irritation to the skin";
        Test test = new Test(text, rule, 0, 3);
        retVal.add(test);
        
        return retVal;
        
    }
        
}
    
