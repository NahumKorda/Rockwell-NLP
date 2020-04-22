package com.itcag.util.txt;

/**
  * <p>This class is implementation of the so-called <i>Porter Stemming Algorithm</i>.</p>
  * <p>Copied from <a href="https://tartarus.org/martin/PorterStemmer/java.txt" target="_blank">https://tartarus.org/martin/PorterStemmer/java.txt</a>.</p>
  * <p></p>
  * <p>It operates in the following way:</p>
  * <ol>
  * <li>Create an instance of this class.</li>
  * <li>Feed a string character by character using the {@link #add(char)} method, or convert string into an array of characters, and feed the entire array at once using the {@link #add(char[])} method.</li>
  * <li>Call the method {@link #stem()} to run the algorithm.</li>
  * <li>Read the stemmed string from the {@link #toString()} method.</li>
  * </ol>

  */
public final class Stemmer {

    private char[] buffer;
    private int i;
    private int i_end;
    private int j;
    private int k;
    private static final int INC = 50;

    public Stemmer() {
        buffer = new char[INC];
        i = 0;
        i_end = 0;
    }

   /**
    * Feed a string character by character.
    * @param character A character.
    */
    public void add(char character) {
        if (i == buffer.length) {
            char[] new_b = new char[i + INC];
            for (int c = 0; c < i; c++) {
                new_b[c] = buffer[c];
            }
            buffer = new_b;
        }
        buffer[i++] = character;
    }


   /**
    * Convert a string into an array of characters, and feed it all at once.
    * @param characters Character array.
    */
    public void add(char[] characters) {
        int length = characters.length;
        if (i + length >= buffer.length) {
            char[] new_b = new char[i + length + INC];
            for (int c = 0; c < i; c++) {
                new_b[c] = buffer[c];
            }
            buffer = new_b;
        }
        for (int c = 0; c < length; c++) buffer[i++] = characters[c];
    }

    /**
     * Run the algorithm.
     */
    public void stem() {
        k = i - 1;
        if (k > 1) {
            step1();
            step2();
            step3();
            step4();
            step5();
            step6();
        }
        i_end = k+1;
        i = 0;
    }

   /**
    * Read the stemmed string.
    * @return Stemmed string.
    */
    @Override
   public String toString() {
       return new String(buffer, 0, i_end);
   }

    private boolean cons(int i) {
        switch (buffer[i]) {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
                return false;
            case 'y':
                return (i==0) ? true : !cons(i-1);
            default:
                return true;
       }
    }

    private int m() {
        int n = 0;
        int i = 0;
        while(true) {
            if (i > j) return n;
            if (! cons(i)) break; i++;
        }
        i++;
        while(true) {
            while(true) {
                if (i > j) return n;
                if (cons(i)) break;
                i++;
            }
            i++;
            n++;
            while(true) {
                if (i > j) return n;
                if (! cons(i)) break;
                i++;
            }
            i++;
        }
    }

    private boolean vowelinstem() {
        int i;
        for (i = 0; i <= j; i++) if (! cons(i)) return true;
        return false;
    }

    private boolean doublec(int j) {
        if (j < 1) return false;
        if (buffer[j] != buffer[j-1]) return false;
        return cons(j);
    }

   private boolean cvc(int i) {
       if (i < 2 || !cons(i) || cons(i-1) || !cons(i-2)) return false;
      {  int ch = buffer[i];
         if (ch == 'w' || ch == 'x' || ch == 'y') return false;
      }
      return true;
   }

   private boolean ends(String s) {
        int l = s.length();
        int o = k-l+1;
        if (o < 0) return false;
        for (int i = 0; i < l; i++) if (buffer[o+i] != s.charAt(i)) return false;
        j = k-l;
        return true;
   }

    private void setto(String s) {
        int l = s.length();
        int o = j+1;
        for (int i = 0; i < l; i++) buffer[o+i] = s.charAt(i);
        k = j+l;
    }

   private void r(String s) {
       if (m() > 0) setto(s);
   }

    private void step1() {
        if (buffer[k] == 's') {
            if (ends("sses")) k -= 2; else
            if (ends("ies")) setto("i"); else
            if (buffer[k-1] != 's') k--;
        }
        if (ends("eed")) { if (m() > 0) k--; } else
        if ((ends("ed") || ends("ing")) && vowelinstem()) {
            k = j;
            if (ends("at")) setto("ate"); else
            if (ends("bl")) setto("ble"); else
            if (ends("iz")) setto("ize"); else
            if (doublec(k)) {
                k--;
               {  int ch = buffer[k];
                  if (ch == 'l' || ch == 's' || ch == 'z') k++;
               }
            } else if (m() == 1 && cvc(k)) setto("e");
        }
    }

   private void step2() { if (ends("y") && vowelinstem()) buffer[k] = 'i'; }

   private void step3() {
        if (k == 0) return;
        switch (buffer[k-1]) {
            case 'a':
                if (ends("ational")) { r("ate"); break; }
                if (ends("tional")) { r("tion"); break; }
                break;
            case 'c': if (ends("enci")) { r("ence"); break; }
                if (ends("anci")) { r("ance"); break; }
                break;
            case 'e':
                if (ends("izer")) { r("ize"); break; }
                break;
            case 'l':
                if (ends("bli")) { r("ble"); break; }
                if (ends("alli")) { r("al"); break; }
                if (ends("entli")) { r("ent"); break; }
                if (ends("eli")) { r("e"); break; }
                if (ends("ousli")) { r("ous"); break; }
                break;
            case 'o': if (ends("ization")) { r("ize"); break; }
                if (ends("ation")) { r("ate"); break; }
                if (ends("ator")) { r("ate"); break; }
                break;
            case 's': if (ends("alism")) { r("al"); break; }
                if (ends("iveness")) { r("ive"); break; }
                if (ends("fulness")) { r("ful"); break; }
                if (ends("ousness")) { r("ous"); break; }
                break;
            case 't': if (ends("aliti")) { r("al"); break; }
                if (ends("iviti")) { r("ive"); break; }
                if (ends("biliti")) { r("ble"); break; }
                break;
            case 'g':
                if (ends("logi")) { r("log"); break; }
        }
    }

    private void step4() {
        switch (buffer[k]) {
            case 'e':
                if (ends("icate")) { r("ic"); break; }
                if (ends("ative")) { r(""); break; }
                if (ends("alize")) { r("al"); break; }
                break;
            case 'i':
                if (ends("iciti")) { r("ic"); break; }
                break;
            case 'l':
                if (ends("ical")) { r("ic"); break; }
                if (ends("ful")) { r(""); break; }
                break;
            case 's':
                if (ends("ness")) { r(""); break; }
                break;
        }
    }

    private void step5() {
       if (k == 0) return;
       switch (buffer[k-1]) {
            case 'a':
                if (ends("al")) break; return;
            case 'c':
                if (ends("ance")) break;
                if (ends("ence")) break; return;
            case 'e':
                if (ends("er")) break; return;
            case 'i':
                if (ends("ic")) break; return;
            case 'l':
                if (ends("able")) break;
                if (ends("ible")) break; return;
            case 'n':
                if (ends("ant")) break;
                if (ends("ement")) break;
                if (ends("ment")) break;
                /* element etc. not stripped before the m */
                if (ends("ent")) break; return;
            case 'o':
                if (ends("ion") && j >= 0 && (buffer[j] == 's' || buffer[j] == 't')) break;
                /* j >= 0 fixes Bug 2 */
                if (ends("ou")) break; return;
                /* takes care of -ous */
            case 's':
                if (ends("ism")) break; return;
            case 't':
                if (ends("ate")) break;
                if (ends("iti")) break; return;
            case 'u':
                if (ends("ous")) break; return;
            case 'v':
                if (ends("ive")) break; return;
            case 'z':
                if (ends("ize")) break; return;
            default:
                return;
       }
       if (m() > 1) k = j;
    }

    private void step6() {
        j = k;
        if (buffer[k] == 'e') {
            int a = m();
            if (a > 1 || a == 1 && !cvc(k-1)) k--;
        }
        if (buffer[k] == 'l' && doublec(k) && m() > 1) k--;
    }

}
