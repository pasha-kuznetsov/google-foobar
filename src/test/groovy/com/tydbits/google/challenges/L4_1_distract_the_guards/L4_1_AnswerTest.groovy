package com.tydbits.google.challenges.L4_1_distract_the_guards

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

    @Unroll
    def "test finite #m"() {
        expect:
        answer.isFiniteMatch(m as Answer.Pair)

        where:
        m        | _
        [1, 1]   | _
        [7, 7]   | _
        [7, 21]  | _
        [42, 14] | _
    }

    @Unroll
    def "test infinite #m"() {
        expect:
        !answer.isFiniteMatch(m as Answer.Pair)

        where:
        m       | _
        [1, 4]  | _
        [4, 1]  | _
        [1, 19] | _
        [19, 1] | _
        [7, 3]  | _
        [3, 7]  | _
    }
}
