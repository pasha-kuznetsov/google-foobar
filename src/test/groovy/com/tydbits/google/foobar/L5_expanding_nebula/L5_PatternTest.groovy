package com.tydbits.google.foobar.L5_expanding_nebula

import spock.lang.Specification
import spock.lang.Unroll

class L5_PatternTest extends Specification {

    @Unroll
    def "test left #args"() {
        expect:
        (args as Answer.Pattern).left == expectedAnswer

        where:
        args         | expectedAnswer
        [0b01, 0b01] | 0b00
        [0b10, 0b01] | 0b10
        [0b10, 0b10] | 0b11
    }

    @Unroll
    def "test right #args"() {
        expect:
        (args as Answer.Pattern).right == expectedAnswer

        where:
        args         | expectedAnswer
        [0b01, 0b01] | 0b11
        [0b10, 0b01] | 0b01
        [0b10, 0b10] | 0b00
    }

    @Unroll
    def "test left right #left #right"() {
        expect:
        (left as Answer.Pattern).left == (right as Answer.Pattern).right

        where:
        left         | right
        [0b01, 0b01] | [0b10, 0b10]
        [0b10, 0b10] | [0b01, 0b01]
        [0b11, 0b11] | [0b11, 0b11]
    }
}
