package com.tydbits.google.foobar.L4_2_free_the_bunny_prisoners

import spock.lang.Specification
import spock.lang.Unroll

class L4_2_AnswerTest extends Specification {
    def answer = new Answer()

    @Unroll
    def "test #buns #required"() {
        expect:
        answer.answer(buns, required) == expectedAnswer as int[][]

        where:
        buns | required || expectedAnswer
        2    | 1        || [[0], [0]]
        5    | 3        || [[0, 1, 2, 3, 4, 5],
                            [0, 1, 2, 6, 7, 8],
                            [0, 3, 4, 6, 7, 9],
                            [1, 3, 5, 6, 8, 9],
                            [2, 4, 5, 7, 8, 9]]
        4    | 4        || [[0], [1], [2], [3]]
    }
}
