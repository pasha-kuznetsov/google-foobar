package com.tydbits.google.foobar.L4_1_distract_the_guards

import spock.lang.Specification
import spock.lang.Unroll

class L4_1_AnswerTest extends Specification {

    def answer = new Answer()

    @Unroll
    def "test answer #bananas"() {
        expect:
        answer.answer(bananas as int[]) == expectedAnswer

        where:
        bananas               | expectedAnswer
        [1, 1]                | 2
        [1, 7, 3, 21, 13, 19] | 0
        [7, 7, 5, 21, 7, 19]  | 2
    }
}
