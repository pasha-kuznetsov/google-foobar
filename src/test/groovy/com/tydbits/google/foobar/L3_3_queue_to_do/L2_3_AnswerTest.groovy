package com.tydbits.google.foobar.L3_3_queue_to_do

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
        // 0 1 2 /
        // 3 4 / 5
        // 6 / 7 8
        // where the guards' XOR (^) checksum is 0^1^2^3^4^6 == 2.

        17    | 4      | 14
        // 17 18 19 20 /
        // 21 22 23 / 24
        // 25 26 / 27 28
        // 29 / 30 31 32
        // which produces the checksum 17^18^19^20^21^22^23^25^26^29 == 14.
    }
}
