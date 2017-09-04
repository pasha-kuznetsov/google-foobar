package com.tydbits.google.challenges.L3_3_queue_to_do

import spock.lang.Specification
import spock.lang.Unroll

class L2_3_AnswerTest extends Specification {

    def answer = new Answer()

    @Unroll
    def "test #start #length"() {
        expect:
        answer.answer(start, length) == expectedAnswer

        where:
        start | length | expectedAnswer
        0     | 3      | 2
        17    | 4      | 14
    }
}
