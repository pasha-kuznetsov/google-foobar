package com.tydbits.google.foobar.L3_2_fuel_injection_perfection

import spock.lang.Specification
import spock.lang.Unroll

class L3_2_AnswerTest extends Specification {
    def answer = new Answer()

    @Unroll
    def "test #n"() {
        expect:
        answer.answer(n) == expectedAnswer

        where:
        n    | expectedAnswer
        "0"  | 0      // ???
        "1"  | 0      // 1
        "2"  | 1      // 2 -> 1
        "4"  | 2      // 4 -> 2 -> 1
        "15" | 5      // 15 -> 16 -> 8 -> 4 -> 2 -> 1
        "123" | 9     // 123 -> 122 -> 66 -> ...
        "125" | 9     // 125 -> 126 -> 68 -> 33 -> ...
    }

    @Unroll
    def "test prime #n"() {
        expect:
        answer.answer(n) == expectedAnswer

        where:
        n             | expectedAnswer
        "105733"      | 22
        "15487457"    | 31
        "179425579"   | 39
        "32416189361" | 46
    }

    @Unroll
    def "test #n digits #d"() {
        when:
        def str = new String(new char[n]).replace('\0', d)

        then:
        answer.answer(str) == expectedAnswer

        where:
        n   || d   || expectedAnswer
        309 || '0' || 0
        309 || '7' || 1365
        309 || '8' || 1370
        309 || '9' || 1278
        // not sure how to check these, but OK as long as it finishes quickly
    }
}
