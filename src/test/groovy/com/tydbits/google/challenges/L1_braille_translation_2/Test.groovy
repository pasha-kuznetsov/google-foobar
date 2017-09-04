package com.tydbits.google.challenges.L1_braille_translation_2

import spock.lang.Specification
import spock.lang.Unroll

class Test extends Specification {
    def answer = new Answer();

    @Unroll
    def "braille #plaintext"() {
        expect:
        answer.answer(plaintext) == expectedAnswer

        where:
        plaintext                                      | expectedAnswer
        "code"                                         | "100100101010100110100010"
        "Braille"                                      | "000001110000111010100000010100111000111000100010"
        "The quick brown fox jumped over the lazy dog" | "000001011110110010100010000000111110101001010100100100101000000000110000111010101010010111101110000000110100101010101101000000010110101001101100111100100010100110000000101010111001100010111010000000011110110010100010000000111000100000101011101111000000100110101010110110"
    }
}
