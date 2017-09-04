package com.tydbits.google.challenges.dont_get_volunteered

import spock.lang.Specification
import spock.lang.Unroll

class AnswerTest extends Specification {
    def answer = new Answer();

    @Unroll
    def "test answer #src #dest"() {
        expect:
        answer.answer(src, dest) == expectedAnswer

        where:
        src | dest || expectedAnswer
        19  | 36   || 1
        0   | 1    || 3
    }

    @Unroll
    def "test possible moves #src"() {
        when:
        def result = answer.possibleMoves(src).toList()

        then:
        result.containsAll(expectedResult)
        expectedResult.containsAll(result)
        result.size() == expectedResult.size()

        where:
        src || expectedResult
        0   || [10, 17]
        10  || [0, 4, 20, 27, 25, 16]
        19  || [34, 25, 9, 2, 4, 13, 29, 36]
    }
}
