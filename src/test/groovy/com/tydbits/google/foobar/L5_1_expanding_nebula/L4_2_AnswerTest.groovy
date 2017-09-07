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

        [['o', '.', 'o'].collect { it == 'o' },
         ['.', 'o', '.'].collect { it == 'o' },
         ['o', '.', 'o'].collect { it == 'o' }] |  4

        [['o', '.', 'o', '.', '.', 'o', 'o', 'o'].collect { it == 'o' },
         ['o', '.', 'o', '.', '.', '.', 'o', '.'].collect { it == 'o' },
         ['o', 'o', 'o', '.', '.', '.', 'o', '.'].collect { it == 'o' },
         ['o', '.', 'o', '.', '.', '.', 'o', '.'].collect { it == 'o' },
         ['o', '.', 'o', '.', '.', 'o', 'o', 'o'].collect { it == 'o' }] | 254

        [['o', 'o', '.', 'o', '.', 'o', '.', 'o', 'o', '.'].collect { it == 'o' },
         ['o', 'o', '.', '.', '.', '.', 'o', 'o', 'o', '.'].collect { it == 'o' },
         ['o', 'o', '.', '.', '.', '.', '.', '.', '.', 'o'].collect { it == 'o' },
         ['.', 'o', '.', '.', '.', '.', 'o', 'o', '.', '.'].collect { it == 'o' }] | 11567
    }
}
