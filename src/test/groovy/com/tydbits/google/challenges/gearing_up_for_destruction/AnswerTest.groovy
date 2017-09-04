package com.tydbits.google.challenges.gearing_up_for_destruction

import spock.lang.Specification
import spock.lang.Unroll

class AnswerTest extends Specification {
    def answer = new Answer();

    @Unroll
    def "test #pegs"() {
        expect:
        answer.answer(pegs as int[]) == expectedGears as int[]

        where:
        pegs                      | expectedGears
        [4, 30, 50]               | [12, 1]
        [1, 16, 33, 43]           | [16, 3]
        [11, 31, 48, 60, 78, 103] | [44, 3]
        [4, 17, 50]               | [-1, -1]
    }
}
