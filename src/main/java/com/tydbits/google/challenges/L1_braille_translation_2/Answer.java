package com.tydbits.google.challenges.L1_braille_translation_2;

public class Answer {
    public static String answer(String plaintext) {
        StringBuilder braille = new StringBuilder();
        for (char c : plaintext.toCharArray())
            braille.append(toBraille(c));
        return braille.toString();
	}

    private static String toBraille(char c) {
        String braille = "";
        if (c >= 'A' && c <= 'Z') {
            braille = braille + "000001";
            c = (char)('a' + (c - 'A'));
        }
        braille = braille + getBrailleChar(c);
        return braille;
    }

    private static String getBrailleChar(char c) {
        switch (c) {
            case 'a': return "100000";
            case 'b': return "110000";
            case 'c': return "100100";
            case 'd': return "100110";
            case 'e': return "100010";
            case 'f': return "110100";
            case 'g': return "110110";
            case 'h': return "110010";
            case 'i': return "010100";
            case 'j': return "010110";
            case 'k': return "101000";
            case 'l': return "111000";
            case 'm': return "101100";
            case 'n': return "101110";
            case 'o': return "101010";
            case 'p': return "111100";
            case 'q': return "111110";
            case 'r': return "111010";
            case 's': return "011100";
            case 't': return "011110";
            case 'u': return "101001";
            case 'v': return "111001";
            case 'w': return "010111";
            case 'x': return "101101";
            case 'y': return "101111";
            case 'z': return "101011";
            case ' ': return "000000";
        }
        return "";
    }
}
