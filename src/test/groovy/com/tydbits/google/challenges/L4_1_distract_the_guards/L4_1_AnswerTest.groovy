package com.tydbits.google.challenges.L4_1_distract_the_guards

import spock.lang.Specification
import spock.lang.Unroll

class L4_1_AnswerTest extends Specification {

    def answer = new Answer()

    @Unroll
    def "test #bananas"() {
        expect:
        answer.answer(bananas) == expectedAnswer

        where:
        bananas               | expectedAnswer
        [1, 1]                | 2
        [1, 7, 3, 21, 13, 19] | 0
    }
}
