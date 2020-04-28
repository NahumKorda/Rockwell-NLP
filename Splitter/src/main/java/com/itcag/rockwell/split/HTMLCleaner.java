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

import java.util.HashMap;

/**
 * <p>This class removes HTML tags from the text, and replaces HTML characters with their Unicode equivalents.</p>
 */
public final class HTMLCleaner {
    
    private final HashMap<String, String> specialChars =  new HashMap<>();
    
    public HTMLCleaner() {
        specialChars.put("&quot;", "\"");
        specialChars.put("&num;", "#");
        specialChars.put("&dollar;", "$");
        specialChars.put("&percnt;", "%");
        specialChars.put("&amp;", "&");
        specialChars.put("&apos;", "'");
        specialChars.put("&lpar;", "(");
        specialChars.put("&rpar;", ")");
        specialChars.put("&ast;", "*");
        specialChars.put("&plus;", "+");
        specialChars.put("&comma;", ",");
        specialChars.put("&minus;", "-");
        specialChars.put("&period;", ".");
        specialChars.put("&sol;", "/");
        specialChars.put("&colon;", ":");
        specialChars.put("&semi;", ";");
        specialChars.put("&lt;", "<");
        specialChars.put("&equals;", "=");
        specialChars.put("&gt;", ">");
        specialChars.put("&quest;", "?");
        specialChars.put("&commat;", "@");
        specialChars.put("&lsqb;", "[");
        specialChars.put("&bsol;", "\\");
        specialChars.put("&rsqb;", "]");
        specialChars.put("&Hat;", "^");
        specialChars.put("&lowbar;", "_");
        specialChars.put("&grave;", "`");
        specialChars.put("&lcub;", "{");
        specialChars.put("&verbar;", "|");
        specialChars.put("&rcub;", "}");
        specialChars.put("&nbsp;", " ");
        specialChars.put("&iexcl;", "¡");
        specialChars.put("&cent;", "¢");
        specialChars.put("&pound;", "£");
        specialChars.put("&curren;", "¤");
        specialChars.put("&yen;", "¥");
        specialChars.put("&#x20B9;", "₹");
        specialChars.put("&brvbar;", "¦");
        specialChars.put("&sect;", "§");
        specialChars.put("&uml;", "¨");
        specialChars.put("&copy;", "©");
        specialChars.put("&ordf;", "ª");
        specialChars.put("&laquo;", "«");
        specialChars.put("&not;", "¬");
        specialChars.put("&reg;", "®");
        specialChars.put("&macr;", "¯");
        specialChars.put("&deg;", "°");
        specialChars.put("&plusmn;", "±");
        specialChars.put("&sup2;", "²");
        specialChars.put("&sup3;", "³");
        specialChars.put("&acute;", "´");
        specialChars.put("&micro;", "µ");
        specialChars.put("&para;", "¶");
        specialChars.put("&middot;", "·");
        specialChars.put("&cedil;", "¸");
        specialChars.put("&sup1;", "¹");
        specialChars.put("&ordm;", "º");
        specialChars.put("&raquo;", "»");
        specialChars.put("&frac14;", "¼");
        specialChars.put("&frac12;", "½");
        specialChars.put("&frac34;", "¾");
        specialChars.put("&iquest;", "¿");
        specialChars.put("&Agrave;", "À");
        specialChars.put("&Aacute;", "Á");
        specialChars.put("&Acirc;", "Â");
        specialChars.put("&Atilde;", "Ã");
        specialChars.put("&Auml;", "Ä");
        specialChars.put("&Aring;", "Å");
        specialChars.put("&AElig;", "Æ");
        specialChars.put("&Ccedil;", "Ç");
        specialChars.put("&Egrave;", "È");
        specialChars.put("&Eacute;", "É");
        specialChars.put("&Ecirc;", "Ê");
        specialChars.put("&Euml;", "Ë");
        specialChars.put("&Igrave;", "Ì");
        specialChars.put("&Iacute;", "Í");
        specialChars.put("&Icirc;", "Î");
        specialChars.put("&Iuml;", "Ï");
        specialChars.put("&ETH;", "Ð");
        specialChars.put("&Ntilde;", "Ñ");
        specialChars.put("&Ograve;", "Ò");
        specialChars.put("&Oacute;", "Ó");
        specialChars.put("&Ocirc;", "Ô");
        specialChars.put("&Otilde;", "Õ");
        specialChars.put("&Ouml;", "Ö");
        specialChars.put("&times;", "×");
        specialChars.put("&Oslash;", "Ø");
        specialChars.put("&Ugrave;", "Ù");
        specialChars.put("&Uacute;", "Ú");
        specialChars.put("&Ucirc;", "Û");
        specialChars.put("&Uuml;", "Ü");
        specialChars.put("&Yacute;", "Ý");
        specialChars.put("&THORN;", "Þ");
        specialChars.put("&szlig;", "ß");
        specialChars.put("&agrave;", "à");
        specialChars.put("&aacute;", "á");
        specialChars.put("&acirc;", "â");
        specialChars.put("&atilde;", "ã");
        specialChars.put("&auml;", "ä");
        specialChars.put("&aring;", "å");
        specialChars.put("&aelig;", "æ");
        specialChars.put("&ccedil;", "ç");
        specialChars.put("&egrave;", "è");
        specialChars.put("&eacute;", "é");
        specialChars.put("&ecirc;", "ê");
        specialChars.put("&euml;", "ë");
        specialChars.put("&igrave;", "ì");
        specialChars.put("&iacute;", "í");
        specialChars.put("&icirc;", "î");
        specialChars.put("&iuml;", "ï");
        specialChars.put("&eth;", "ð");
        specialChars.put("&ntilde;", "ñ");
        specialChars.put("&ograve;", "ò");
        specialChars.put("&oacute;", "ó");
        specialChars.put("&ocirc;", "ô");
        specialChars.put("&otilde;", "õ");
        specialChars.put("&ouml;", "ö");
        specialChars.put("&divide;", "÷");
        specialChars.put("&oslash;", "ø");
        specialChars.put("&ugrave;", "ù");
        specialChars.put("&uacute;", "ú");
        specialChars.put("&ucirc;", "û");
        specialChars.put("&uuml;", "ü");
        specialChars.put("&yacute;", "ý");
        specialChars.put("&thorn;", "þ");
        specialChars.put("&yuml;", "ÿ");
        specialChars.put("&OElig;", "Œ");
        specialChars.put("&oelig;", "œ");
        specialChars.put("&Scaron;", "Š");
        specialChars.put("&scaron;", "š");
        specialChars.put("&Yuml;", "Ÿ");
        specialChars.put("&fnof;", "ƒ");
        specialChars.put("&circ;", "ˆ");
        specialChars.put("&tilde;", "˜");
        specialChars.put("&Alpha;", "Α");
        specialChars.put("&Beta;", "Β");
        specialChars.put("&Gamma;", "Γ");
        specialChars.put("&Delta;", "Δ");
        specialChars.put("&Epsilon;", "Ε");
        specialChars.put("&Zeta;", "Ζ");
        specialChars.put("&Eta;", "Η");
        specialChars.put("&Theta;", "Θ");
        specialChars.put("&Iota;", "Ι");
        specialChars.put("&Kappa;", "Κ");
        specialChars.put("&Lambda;", "Λ");
        specialChars.put("&Mu;", "Μ");
        specialChars.put("&Nu;", "Ν");
        specialChars.put("&Xi;", "Ξ");
        specialChars.put("&Omicron;", "Ο");
        specialChars.put("&Pi;", "Π");
        specialChars.put("&Rho;", "Ρ");
        specialChars.put("&Sigma;", "Σ");
        specialChars.put("&Tau;", "Τ");
        specialChars.put("&Upsilon;", "Υ");
        specialChars.put("&Phi;", "Φ");
        specialChars.put("&Chi;", "Χ");
        specialChars.put("&Psi;", "Ψ");
        specialChars.put("&Omega;", "Ω");
        specialChars.put("&alpha;", "α");
        specialChars.put("&beta;", "β");
        specialChars.put("&gamma;", "γ");
        specialChars.put("&delta;", "δ");
        specialChars.put("&epsilon;", "ε");
        specialChars.put("&zeta;", "ζ");
        specialChars.put("&eta;", "η");
        specialChars.put("&theta;", "θ");
        specialChars.put("&iota;", "ι");
        specialChars.put("&kappa;", "κ");
        specialChars.put("&lambda;", "λ");
        specialChars.put("&mu;", "μ");
        specialChars.put("&nu;", "ν");
        specialChars.put("&xi;", "ξ");
        specialChars.put("&omicron;", "ο");
        specialChars.put("&pi;", "π");
        specialChars.put("&rho;", "ρ");
        specialChars.put("&sigmaf;", "ς");
        specialChars.put("&sigma;", "σ");
        specialChars.put("&tau;", "τ");
        specialChars.put("&upsilon;", "υ");
        specialChars.put("&phi;", "φ");
        specialChars.put("&chi;", "χ");
        specialChars.put("&psi;", "ψ");
        specialChars.put("&omega;", "ω");
        specialChars.put("&thetasym;", "ϑ");
        specialChars.put("&upsih;", "ϒ");
        specialChars.put("&piv;", "ϖ");
        specialChars.put("&ensp;", " ");
        specialChars.put("&emsp;", " ");
        specialChars.put("&thinsp;", " ");
        specialChars.put("&zwnj;", "‌ ");
        specialChars.put("&zwj;", "‍ ");
        specialChars.put("&lrm;", "‎ ");
        specialChars.put("&rlm;", "‏ ");
        specialChars.put("&ndash;", "–");
        specialChars.put("&mdash;", "—");
        specialChars.put("&lsquo;", "‘");
        specialChars.put("&rsquo;", "’");
        specialChars.put("&sbquo;", "‚");
        specialChars.put("&ldquo;", "“");
        specialChars.put("&rdquo;", "”");
        specialChars.put("&bdquo;", "„");
        specialChars.put("&dagger;", "†");
        specialChars.put("&Dagger;", "‡");
        specialChars.put("&permil;", "‰");
        specialChars.put("&lsaquo;", "‹");
        specialChars.put("&rsaquo;", "›");
        specialChars.put("&bull;", "•");
        specialChars.put("&hellip;", "…");
        specialChars.put("&prime;", "′");
        specialChars.put("&Prime;", "″");
        specialChars.put("&oline;", "‾");
        specialChars.put("&frasl;", "⁄");
        specialChars.put("&weierp;", "℘");
        specialChars.put("&image;", "ℑ");
        specialChars.put("&real;", "ℜ");
        specialChars.put("&trade;", "™");
        specialChars.put("&alefsym;", "ℵ");
        specialChars.put("&larr;", "←");
        specialChars.put("&uarr;", "↑");
        specialChars.put("&rarr;", "→");
        specialChars.put("&darr;", "↓");
        specialChars.put("&harr;", "↔");
        specialChars.put("&crarr;", "↵");
        specialChars.put("&lArr;", "⇐");
        specialChars.put("&uArr;", "⇑");
        specialChars.put("&rArr;", "⇒");
        specialChars.put("&dArr;", "⇓");
        specialChars.put("&hArr;", "⇔");
        specialChars.put("&forall;", "∀");
        specialChars.put("&part;", "∂");
        specialChars.put("&exist;", "∃");
        specialChars.put("&empty;", "∅");
        specialChars.put("&nabla;", "∇");
        specialChars.put("&isin;", "∈");
        specialChars.put("&notin;", "∉");
        specialChars.put("&ni;", "∋");
        specialChars.put("&prod;", "∏");
        specialChars.put("&sum;", "∑");
        specialChars.put("&minus;", "−");
        specialChars.put("&lowast;", "∗");
        specialChars.put("&radic;", "√");
        specialChars.put("&prop;", "∝");
        specialChars.put("&infin;", "∞");
        specialChars.put("&ang;", "∠");
        specialChars.put("&and;", "∧");
        specialChars.put("&or;", "∨");
        specialChars.put("&cap;", "∩");
        specialChars.put("&cup;", "∪");
        specialChars.put("&int;", "∫");
        specialChars.put("&there4;", "∴");
        specialChars.put("&sim;", "∼");
        specialChars.put("&cong;", "≅");
        specialChars.put("&asymp;", "≈");
        specialChars.put("&ne;", "≠");
        specialChars.put("&equiv;", "≡");
        specialChars.put("&le;", "≤");
        specialChars.put("&ge;", "≥");
        specialChars.put("&sub;", "⊂");
        specialChars.put("&sup;", "⊃");
        specialChars.put("&nsub;", "⊄");
        specialChars.put("&sube;", "⊆");
        specialChars.put("&supe;", "⊇");
        specialChars.put("&oplus;", "⊕");
        specialChars.put("&otimes;", "⊗");
        specialChars.put("&perp;", "⊥");
        specialChars.put("&sdot;", "⋅");
        specialChars.put("&lceil;", "⌈");
        specialChars.put("&rceil;", "⌉");
        specialChars.put("&lfloor;", "⌊");
        specialChars.put("&rfloor;", "⌋");
        specialChars.put("&lang;", "〈");
        specialChars.put("&rang;", "〉");
        specialChars.put("&loz;", "◊");
        specialChars.put("&spades;", "♠");
        specialChars.put("&clubs;", "♣");
        specialChars.put("&hearts;", "♥");
        specialChars.put("&diams;", "♦");
        specialChars.put("&#34;", "\"");
        specialChars.put("&#35;", "#");
        specialChars.put("&#36;", "$");
        specialChars.put("&#37;", "%");
        specialChars.put("&#38;", "&");
        specialChars.put("&#39;", "'");
        specialChars.put("&#40;", "(");
        specialChars.put("&#41;", ")");
        specialChars.put("&#42;", "*");
        specialChars.put("&#43;", "+");
        specialChars.put("&#44;", ",");
        specialChars.put("&#45;", "-");
        specialChars.put("&#46;", ".");
        specialChars.put("&#47;", "/");
        specialChars.put("&#58;", ":");
        specialChars.put("&#59;", ";");
        specialChars.put("&#60;", "<");
        specialChars.put("&#61;", "=");
        specialChars.put("&#62;", ">");
        specialChars.put("&#63;", "?");
        specialChars.put("&#64;", "@");
        specialChars.put("&#91;", "[");
        specialChars.put("&#92;", "\\");
        specialChars.put("&#93;", "]");
        specialChars.put("&#94;", "^");
        specialChars.put("&#95;", "_");
        specialChars.put("&#96;", "`");
        specialChars.put("&#123;", "{");
        specialChars.put("&#124;", "|");
        specialChars.put("&#125;", "}");
        specialChars.put("&#126;", "~");
        specialChars.put("&#160;", " ");
        specialChars.put("&#161;", "¡");
        specialChars.put("&#162;", "¢");
        specialChars.put("&#163;", "£");
        specialChars.put("&#164;", "¤");
        specialChars.put("&#165;", "¥");
        specialChars.put("&#x20B9;", "₹");
        specialChars.put("&#166;", "¦");
        specialChars.put("&#167;", "§");
        specialChars.put("&#168;", "¨");
        specialChars.put("&#169;", "©");
        specialChars.put("&#170;", "ª");
        specialChars.put("&#171;", "«");
        specialChars.put("&#172;", "¬");
        specialChars.put("&#174;", "®");
        specialChars.put("&#175;", "¯");
        specialChars.put("&#176;", "°");
        specialChars.put("&#177;", "±");
        specialChars.put("&#178;", "²");
        specialChars.put("&#179;", "³");
        specialChars.put("&#180;", "´");
        specialChars.put("&#181;", "µ");
        specialChars.put("&#182;", "¶");
        specialChars.put("&#183;", "·");
        specialChars.put("&#184;", "¸");
        specialChars.put("&#185;", "¹");
        specialChars.put("&#186;", "º");
        specialChars.put("&#187;", "»");
        specialChars.put("&#188;", "¼");
        specialChars.put("&#189;", "½");
        specialChars.put("&#190;", "¾");
        specialChars.put("&#191;", "¿");
        specialChars.put("&#192;", "À");
        specialChars.put("&#193;", "Á");
        specialChars.put("&#194 ;", "Â");
        specialChars.put("&#195;", "Ã");
        specialChars.put("&#196;", "Ä");
        specialChars.put("&#197;", "Å");
        specialChars.put("&#198;", "Æ");
        specialChars.put("&#199;", "Ç");
        specialChars.put("&#200;", "È");
        specialChars.put("&#201;", "É");
        specialChars.put("&#202;", "Ê");
        specialChars.put("&#203;", "Ë");
        specialChars.put("&#204;", "Ì");
        specialChars.put("&#205;", "Í");
        specialChars.put("&#206;", "Î");
        specialChars.put("&#207;", "Ï");
        specialChars.put("&#208;", "Ð");
        specialChars.put("&#209;", "Ñ");
        specialChars.put("&#210;", "Ò");
        specialChars.put("&#211;", "Ó");
        specialChars.put("&#212;", "Ô");
        specialChars.put("&#213;", "Õ");
        specialChars.put("&#214;", "Ö");
        specialChars.put("&#215;", "×");
        specialChars.put("&#216;", "Ø");
        specialChars.put("&#217;", "Ù");
        specialChars.put("&#218;", "Ú");
        specialChars.put("&#219;", "Û");
        specialChars.put("&#220;", "Ü");
        specialChars.put("&#221;", "Ý");
        specialChars.put("&#222;", "Þ");
        specialChars.put("&#223;", "ß");
        specialChars.put("&#224;", "à");
        specialChars.put("&#225;", "á");
        specialChars.put("&#226;", "â");
        specialChars.put("&#227;", "ã");
        specialChars.put("&#228;", "ä");
        specialChars.put("&#229;", "å");
        specialChars.put("&#230;", "æ");
        specialChars.put("&#231;", "ç");
        specialChars.put("&#232;", "è");
        specialChars.put("&#233;", "é");
        specialChars.put("&#234;", "ê");
        specialChars.put("&#235;", "ë");
        specialChars.put("&#236;", "ì");
        specialChars.put("&#237;", "í");
        specialChars.put("&#238;", "î");
        specialChars.put("&#239;", "ï");
        specialChars.put("&#240;", "ð");
        specialChars.put("&#241;", "ñ");
        specialChars.put("&#242;", "ò");
        specialChars.put("&#243;", "ó");
        specialChars.put("&#244;", "ô");
        specialChars.put("&#245;", "õ");
        specialChars.put("&#246;", "ö");
        specialChars.put("&#247;", "÷");
        specialChars.put("&#248;", "ø");
        specialChars.put("&#249;", "ù");
        specialChars.put("&#250;", "ú");
        specialChars.put("&#251;", "û");
        specialChars.put("&#252;", "ü");
        specialChars.put("&#253;", "ý");
        specialChars.put("&#254;", "þ");
        specialChars.put("&#255;", "ÿ");
        specialChars.put("&#338;", "Œ");
        specialChars.put("&#339;", "œ");
        specialChars.put("&#352;", "Š");
        specialChars.put("&#353;", "š");
        specialChars.put("&#376;", "Ÿ");
        specialChars.put("&#402;", "ƒ");
        specialChars.put("&#913;", "Α");
        specialChars.put("&#914;", "Β");
        specialChars.put("&#915;", "Γ");
        specialChars.put("&#916;", "Δ");
        specialChars.put("&#917;", "Ε");
        specialChars.put("&#918;", "Ζ");
        specialChars.put("&#919;", "Η");
        specialChars.put("&#920;", "Θ");
        specialChars.put("&#921;", "Ι");
        specialChars.put("&#922;", "Κ");
        specialChars.put("&#923;", "Λ");
        specialChars.put("&#924;", "Μ");
        specialChars.put("&#925;", "Ν");
        specialChars.put("&#926;", "Ξ");
        specialChars.put("&#927;", "Ο");
        specialChars.put("&#928;", "Π");
        specialChars.put("&#929;", "Ρ");
        specialChars.put("&#931;", "Σ");
        specialChars.put("&#932;", "Τ");
        specialChars.put("&#933;", "Υ");
        specialChars.put("&#934;", "Φ");
        specialChars.put("&#935;", "Χ");
        specialChars.put("&#936;", "Ψ");
        specialChars.put("&#937;", "Ω");
        specialChars.put("&#945;", "α");
        specialChars.put("&#946;", "β");
        specialChars.put("&#947;", "γ");
        specialChars.put("&#948;", "δ");
        specialChars.put("&#949;", "ε");
        specialChars.put("&#950;", "ζ");
        specialChars.put("&#951;", "η");
        specialChars.put("&#952;", "θ");
        specialChars.put("&#953;", "ι");
        specialChars.put("&#954;", "κ");
        specialChars.put("&#955;", "λ");
        specialChars.put("&#956;", "μ");
        specialChars.put("&#957;", "ν");
        specialChars.put("&#958;", "ξ");
        specialChars.put("&#959;", "ο");
        specialChars.put("&#960;", "π");
        specialChars.put("&#961;", "ρ");
        specialChars.put("&#962;", "ς");
        specialChars.put("&#963;", "σ");
        specialChars.put("&#964;", "τ");
        specialChars.put("&#965;", "υ");
        specialChars.put("&#966;", "φ");
        specialChars.put("&#967;", "χ");
        specialChars.put("&#968;", "ψ");
        specialChars.put("&#969;", "ω");
        specialChars.put("&#977;", "ϑ");
        specialChars.put("&#978;", "ϒ");
        specialChars.put("&#982;", "ϖ");
        specialChars.put("&#8194;", " ");
        specialChars.put("&#8195;", " ");
        specialChars.put("&#8201;", " ");
        specialChars.put("&#8204;", "‌ ");
        specialChars.put("&#8205;", "‍ ");
        specialChars.put("&#8206;", "‎ ");
        specialChars.put("&#8207;", "‏ ");
        specialChars.put("&#8211;", "–");
        specialChars.put("&#8212;", "—");
        specialChars.put("&#8216;", "‘");
        specialChars.put("&#8217;", "’");
        specialChars.put("&#8218;", "‚");
        specialChars.put("&#8220;", "“");
        specialChars.put("&#8221;", "”");
        specialChars.put("&#8222;", "„");
        specialChars.put("&#8224;", "†");
        specialChars.put("&#8225;", "‡");
        specialChars.put("&#8240;", "‰");
        specialChars.put("&#8249;", "‹");
        specialChars.put("&#8250;", "›");
        specialChars.put("&#8226;", "•");
        specialChars.put("&#8230;", "…");
        specialChars.put("&#8242;", "′");
        specialChars.put("&#8243;", "″");
        specialChars.put("&#8254;", "‾");
        specialChars.put("&#8260;", "⁄");
        specialChars.put("&#8472;", "℘");
        specialChars.put("&#8465;", "ℑ");
        specialChars.put("&#8476;", "ℜ");
        specialChars.put("&#8482;", "™");
        specialChars.put("&#8501;", "ℵ");
        specialChars.put("&#8592;", "←");
        specialChars.put("&#8593;", "↑");
        specialChars.put("&#8594;", "→");
        specialChars.put("&#8595;", "↓");
        specialChars.put("&#8596;", "↔");
        specialChars.put("&#8629;", "↵");
        specialChars.put("&#8656;", "⇐");
        specialChars.put("&#8657;", "⇑");
        specialChars.put("&#8658;", "⇒");
        specialChars.put("&#8659;", "⇓");
        specialChars.put("&#8660;", "⇔");
        specialChars.put("&#9668;", "◄");
        specialChars.put("&#9658;", "►");
        specialChars.put("&#9650;", "▲");
        specialChars.put("&#9660;", "▼");
        specialChars.put("&#8704;", "∀");
        specialChars.put("&#8706;", "∂");
        specialChars.put("&#8707;", "∃");
        specialChars.put("&#8709;", "∅");
        specialChars.put("&#8711;", "∇");
        specialChars.put("&#8712;", "∈");
        specialChars.put("&#8713;", "∉");
        specialChars.put("&#8715;", "∋");
        specialChars.put("&#8719;", "∏");
        specialChars.put("&#8721;", "∑");
        specialChars.put("&#8722;", "−");
        specialChars.put("&#8727;", "∗");
        specialChars.put("&#8730;", "√");
        specialChars.put("&#8733;", "∝");
        specialChars.put("&#8734;", "∞");
        specialChars.put("&#8736;", "∠");
        specialChars.put("&#8743;", "∧");
        specialChars.put("&#8744;", "∨");
        specialChars.put("&#8745;", "∩");
        specialChars.put("&#8746;", "∪");
        specialChars.put("&#8747;", "∫");
        specialChars.put("&#8756;", "∴");
        specialChars.put("&#8764;", "∼");
        specialChars.put("&#8773;", "≅");
        specialChars.put("&#8776;", "≈");
        specialChars.put("&#8800;", "≠");
        specialChars.put("&#8801;", "≡");
        specialChars.put("&#8804;", "≤");
        specialChars.put("&#8805;", "≥");
        specialChars.put("&#8834;", "⊂");
        specialChars.put("&#8835;", "⊃");
        specialChars.put("&#8836;", "⊄");
        specialChars.put("&#8838;", "⊆");
        specialChars.put("&#8839;", "⊇");
        specialChars.put("&#8853;", "⊕");
        specialChars.put("&#8855;", "⊗");
        specialChars.put("&#8869;", "⊥");
        specialChars.put("&#8901;", "⋅");
        specialChars.put("&#8968;", "⌈");
        specialChars.put("&#8969;", "⌉");
        specialChars.put("&#8970;", "⌊");
        specialChars.put("&#8971;", "⌋");
        specialChars.put("&#9001;", "〈");
        specialChars.put("&#9002;", "〉");
        specialChars.put("&#9674;", "◊");
        specialChars.put("&#9824;", "♠");
        specialChars.put("&#9827;", "♣");
        specialChars.put("&#9829;", "♥");
        specialChars.put("&#9830;", "♦");
    }
    
    /**
     * @param input String builder holding the original text.
     * @throws Exception if anything goes wrong.
     */
    public final synchronized void clean(StringBuilder input) throws Exception {
        
        replaceSpecialChars(input);
        removeLineBreaks("<li>", "</li>", input);
        removeLineBreaks("<td>", "</td>", input);
        removeLineBreaks("<p>", "</p>", input);
        removeHTMLTags(input);
        
    }
    
    private void replaceSpecialChars(StringBuilder input) {
        
        int start = input.indexOf("&");
        while (start > -1) {

            int end = input.indexOf(";", start);
            if (end == -1) break;
            
            String specChar = input.substring(start, end + 1);
            if (specialChars.containsKey(specChar)) {
                input.replace(start, end + 1, specialChars.get(specChar));
            }
            
            start = input.indexOf("&", end + 1);
            
        }
    
    }
    
    private void removeLineBreaks(String left, String right, StringBuilder input) {
        
        /**
         * HTML parsers ignore line breaks within tags,
         * but the text is split on these line breaks,
         * assuming that this is the end of a sentence.
         * Therefore, these line breaks are better removed.
         */
        
        int start = input.indexOf(left);
        while (start > -1) {

            int end = input.indexOf(right, start);
            if (end == -1) break;
            
            String specChar = input.substring(start, end + 1);
            TextToolbox.replaceWithin(input, specChar, "\n", " ");
            
            start = input.indexOf(left, end + 1);
            
        }
    
    }
    
    private void removeHTMLTags(StringBuilder input) throws Exception {

        TextToolbox.replaceCaIn(input, "<br>", "\n");
        TextToolbox.replaceCaIn(input, "</li>", "\n");
        TextToolbox.replaceCaIn(input, "</dt>", "\n");
        TextToolbox.replaceCaIn(input, "</p>", "\n");
        
        TextToolbox.removeParentheses(input, "<", ">");
        
    }
    
}
