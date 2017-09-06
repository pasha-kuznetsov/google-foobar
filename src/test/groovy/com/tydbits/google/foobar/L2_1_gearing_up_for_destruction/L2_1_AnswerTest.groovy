package com.tydbits.google.foobar.L2_1_gearing_up_for_destruction

import spock.lang.Specification
import spock.lang.Unroll

class L2_1_AnswerTest extends Specification {
    def answer = new Answer();

    @Unroll
    def "test #pegs"() {
        expect:
        answer.answer(pegs as int[]) == expectedGears as int[]

        where:
        pegs                      | expectedGears
        [4, 30, 50]               | [12, 1]
        [1, 16, 33, 43]           | [16, 3]
        [11, 31, 48, 60, 78, 103] | [-1, -1] // [44, 3] -- Google doesn't allow gears < 1, which happens here (1/3)
        [4, 17, 50]               | [-1, -1]
    }
}
