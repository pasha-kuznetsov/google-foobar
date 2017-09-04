package com.tydbits.google.challenges.L3_2_fuel_injection_perfection

import spock.lang.Specification
import spock.lang.Unroll;

class L3_2_AnswerTest extends Specification {
    def answer = new Answer();

    @Unroll
    def "test answer #n"() {
        expect:
        answer.answer(n) == expectedAnswer

        where:
        n | expectedAnswer
        4 | 2
        15 | 5
    }
}
