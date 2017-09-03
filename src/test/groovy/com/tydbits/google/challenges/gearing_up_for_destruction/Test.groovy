package com.tydbits.google.challenges.gearing_up_for_destruction

import spock.lang.Specification
import spock.lang.Unroll

class Test extends Specification {
    def answer = new Answer();

    @Unroll
    def "destruct #pegs"() {
        expect:
        answer.answer(pegs as int[]) == expectedAnswer as int[]

        where:
        pegs        | expectedAnswer
        [4, 30, 50] | [12, 1]
        [4, 17, 50] | [-1, -1]
    }
}
