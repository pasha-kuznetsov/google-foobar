package com.tydbits.google.challenges.dont_get_volunteered

import spock.lang.Specification
import spock.lang.Unroll

class AnswerTest extends Specification {
    def answer = new Answer();

    @Unroll
    def "test #src #dest"() {
        expect:
        answer.answer(src, dest) == expectedAnswer

        where:
        src | dest || expectedAnswer
        19  | 36   || 1
        0   | 1    || 3
    }
}
