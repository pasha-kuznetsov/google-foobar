package com.tydbits.google.foobar.L5_1_expanding_nebula

import spock.lang.Specification
import spock.lang.Unroll

class L5_1_AnswerTest extends Specification {
    def answer = new Answer()

    @Unroll
    def "test #g.size()"() {
        expect:
        answer.answer(g as boolean[][]) == expectedAnswer

        where:
        g | expectedAnswer

        [[true, false, true],
         [false, true, false],
         [true, false, true]] |  4

        [[true, false, true, false, false, true, true, true],
         [true, false, true, false, false, false, true, false],
         [true, true, true, false, false, false, true, false],
         [true, false, true, false, false, false, true, false],
         [true, false, true, false, false, true, true, true]] | 254

        [[true, true, false, true, false, true, false, true, true, false],
         [true, true, false, false, false, false, true, true, true, false],
         [true, true, false, false, false, false, false, false, false, true],
         [false, true, false, false, false, false, true, true, false, false]] | 11567
    }
}
