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

package com.itcag.rockwell.split;

import com.itcag.util.txt.TextToolbox;

/**
 * <p>This class standardizes Unicode characters with similar appearance, but different Unicode values. For example, a period (".") can have Unicode values 46, 1748, 8228, etc. In this class all alternative values are replaced with the Unicode character 46.</p>
 */
public final class Unicode {

    /**
     * @param input String builder holding the original text.
     */
    public final void standardize(StringBuilder input) {

        if (TextToolbox.isEmpty(input)) return;

        int i = 0;
        while (i < input.length()) {

            int c = input.charAt(i);

            switch (Character.getType(c)) {
                case Character.UPPERCASE_LETTER:
                case Character.LOWERCASE_LETTER:
                case Character.TITLECASE_LETTER:
                    if ((c > 383 && c < 8352) || c > 8399) {
                        input.deleteCharAt(i);
                        break;
                    }
                case Character.DECIMAL_DIGIT_NUMBER:
                case Character.LETTER_NUMBER:
                case Character.MATH_SYMBOL:
                case Character.CURRENCY_SYMBOL:
                    i++;
                    break;
                case Character.SPACE_SEPARATOR:
                    input.replace(i, i + 1, Character.toString((char) 32));
                    i++;
                    break;
                case Character.LINE_SEPARATOR:
                case Character.PARAGRAPH_SEPARATOR:
                /**
                 * Oddly "\n" is character 15.
                 */
                case Character.CONTROL:
                    input.replace(i, i + 1, Character.toString((char) 13));
                    i++;
                    break;
                case Character.DASH_PUNCTUATION:
                case Character.START_PUNCTUATION:
                case Character.END_PUNCTUATION:
                case Character.CONNECTOR_PUNCTUATION:
                case Character.OTHER_PUNCTUATION:
                case Character.MODIFIER_SYMBOL:
                case Character.INITIAL_QUOTE_PUNCTUATION:
                case Character.FINAL_QUOTE_PUNCTUATION:
                    c = standardize(c);
                    input.replace(i, i + 1, Character.toString((char) c));
                    i++;
                    break;
                default:
                    input.deleteCharAt(i);
            }
            
        }

    }

    private char standardize(int c) {
        switch (c) {
            /* hyphen */
            case 45:
            case 1418:
            case 1470:
            case 8208:
            case 8209:
            case 8210:
            case 8211:
            case 8212:
            case 8213:
            case 11834:
            case 11835:
            case 65112:
            case 65123:
            case 65293:
                return 45;
            /* single quote */
            case 39:
            case 96:
            case 145:
            case 146:
            case 8216:
            case 8217:
            case 8218:
            case 8219:
            case 8242:
            case 8245:
            case 65040:
            case 65041:
            case 65287:
            case 65344:
                return 39;
            /* double quote */
            case 34:
            case 132:
            case 139:
            case 147:
            case 148:
            case 155:
            case 171:
            case 187:
            case 8220:
            case 8221:
            case 8222:
            case 8223:
            case 8243:
            case 8246:
            case 8249:
            case 8250:
            case 12317:
            case 12318:
            case 12319:
            case 65282:
                return 34;
            /* left round bracket */
            case 40:
            case 10088:
            case 10090:
            case 10098:
            case 10629:
            case 10647:
            case 11816:
            case 12304:
            case 12308:
            case 12310:
            case 12312:
            case 65113:
            case 65117:
            case 65288:
            case 65375:
            case 64830:
                return 40;
            /* right round bracket */
            case 41:
            case 10089:
            case 10091:
            case 10099:
            case 10630:
            case 10648:
            case 11817:
            case 12305:
            case 12309:
            case 12311:
            case 12313:
            case 65114:
            case 65118:
            case 65289:
            case 65376:
            case 64831:
                return 41;
            /* left square bracket */
            case 91:
            case 10214:
            case 12314:
            case 65339:
                return 91;
            /* right square bracket */
            case 93:
            case 10215:
            case 12315:
            case 65341:
                return 93;
            /* left curly bracket */
            case 123:
            case 10100:
            case 10627:
            case 65115:
            case 65371:
                return 123;
            /* right curly bracket */
            case 125:
            case 10101:
            case 10628:
            case 65116:
            case 65373:
                return 125;
            /* left angle bracket */
            case 60:
            case 9001:
            case 10092:
            case 10094:
            case 10096:
            case 10216:
            case 10218:
            case 12296:
            case 12298:
            case 65124:
            case 65308:
                return 60;
            /* right angle bracket */
            case 62:
            case 9002:
            case 10093:
            case 10095:
            case 10097:
            case 10217:
            case 10219:
            case 12297:
            case 12299:
            case 65125:
            case 65310:
                return 62;
            /* exclamation mark */
            case 33:
            case 8252:
            case 8253:
            case 8265:
            case 65045:
            case 65111:
            case 65281:
                return 33;
            /* question mark */
            case 63:
            case 8263:
            case 8264:
            case 65046:
            case 65110:
            case 65311:
                return 63;
            /* comma */
            case 44:
            case 1548:
            case 12289:
            case 65104:
            case 65105:
            case 65292:
            case 65380:
                return 44;
            /* period */
            case 46:
            case 1748:
            case 8228:
            case 12290:
            case 65042:
            case 65106:
            case 65294:
            case 65377:
                return 46;
            /* colon */
            case 58:
            case 65043:
            case 65109:
            case 65306:
                return 58;
            /* semicolon */
            case 59:
            case 1563:
            case 65044:
            case 65108:
            case 65307:
                return 59;
            /* number sign */
            case 35:
            case 65119:
            case 65283:
                return 35;
            /* at */
            case 64:
            case 65131:
            case 65312:
                return 64;
            /* dollar */
            case 36:
            case 65129:
            case 65284:
                return 36;
            /* percent */
            case 37:
            case 1642:
            case 65130:
            case 65285:
                return 37;
            /* ampersand */
            case 38:
            case 65120:
            case 65286:
                return 38;
            /* asterisk */
            case 42:
            case 8727:
            case 10033:
            case 65121:
            case 65290:
                return 42;
            /* plus */
            case 43:
            case 65122:
            case 65291:
                return 43;
            /* slash */
            case 47:
            case 65295:
                return 47;
            /* backslash */
            case 92:
            case 65128:
            case 65340:
                return 92;
            /* carret */
            case 94:
            case 65342:
                return 94;
            /* pipe */
            case 124:
            case 65372:
                return 124;
            /* tilde */
            case 126:
            case 65374:
                return 126;
            /* equals */
            case 61:
            case 65126:
                return 61;
            /* underscore */
            case 95:
            case 65343:
                return 95;
            default:
                return (char) c;
        }
    }

    /**
     * Only currency signs above 687.
     */
    private boolean isCurrencySign(int c) {
        switch(c) {
            case 1423:
            case 2547:
            case 3647:
            case 6107:
            case 8353:
            case 8356:
            case 8358:
            case 8361:
            case 8362:
            case 8363:
            case 8364:
            case 8365:
            case 8366:
            case 8369:
            case 8370:
            case 8372:
            case 8373:
            case 8376:
            case 8377:
            case 8378:
            case 8380:
            case 8381:
            case 8382:
                return true;
            default:
                return false;
        }
    }

}
