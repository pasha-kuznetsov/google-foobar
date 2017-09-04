package com.tydbits.google.challenges.prepare_the_bunnies_escape

import spock.lang.Specification
import spock.lang.Unroll;

class AnswerTest extends Specification {
    def answer = new Answer();

    @Unroll
    def "test answer #n"() {
        expect:
        answer.answer(maze) == expectedAnswer

        where:
        n || maze                            | expectedAnswer
        1 || [[0, 1, 1, 0],
             [0, 0, 0, 1],
             [1, 1, 0, 0],
             [1, 1, 1, 0]] as int[][]       | 7
        2 || [[0, 0, 0, 0, 0, 0],
             [1, 1, 1, 1, 1, 0],
             [0, 0, 0, 0, 0, 0],
             [0, 1, 1, 1, 1, 1],
             [0, 1, 1, 1, 1, 1],
             [0, 0, 0, 0, 0, 0]] as int[][] | 11
    }
}
