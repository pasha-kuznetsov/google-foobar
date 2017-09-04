package com.tydbits.google.challenges.L2_1_gearing_up_for_destruction

import spock.lang.Specification
import spock.lang.Unroll

class RationalTest extends Specification {

    @Unroll
    def "#left sub #right"() {
        expect:
        left.sub(right).compareTo(expectedResult) == 0

        where:
        left               | right              | expectedResult
        [0, 1] as Rational | [0, 1] as Rational | [0, 1] as Rational
        [1, 1] as Rational | [1, 1] as Rational | [0, 1] as Rational
        [2, 1] as Rational | [1, 1] as Rational | [1, 1] as Rational
        [3, 2] as Rational | [1, 2] as Rational | [2, 2] as Rational
        [1, 2] as Rational | [1, 1] as Rational | [-1, 2] as Rational
    }

    @Unroll
    def "#left div #right"() {
        expect:
        left.div(right).compareTo(expectedResult) == 0

        where:
        left               | right              | expectedResult
        [0, 1] as Rational | [1, 1] as Rational | [0, 1] as Rational
        [1, 1] as Rational | [1, 1] as Rational | [1, 1] as Rational
        [2, 1] as Rational | [1, 1] as Rational | [2, 1] as Rational
        [7, 2] as Rational | [7, 2] as Rational | [1, 1] as Rational
        [2, 7] as Rational | [2, 7] as Rational | [1, 1] as Rational
        [1, 1] as Rational | [2, 1] as Rational | [1, 2] as Rational
        [3, 1] as Rational | [2, 1] as Rational | [3, 2] as Rational
        [3, 2] as Rational | [1, 2] as Rational | [6, 2] as Rational
        [1, 2] as Rational | [-1, 1] as Rational | [-1, 2] as Rational
        [-1, 2] as Rational | [1, 1] as Rational | [-1, 2] as Rational
    }

    @Unroll
    def "simplify #arg"() {
        when:
        def x = arg.Simplify()

        then:
        [x.getNum(), x.getDenom()] == expectedResult

        where:
        arg                 | expectedResult
        [0, 17] as Rational | [0, 1]
        [10, 5] as Rational | [2, 1]
        [1, 17] as Rational | [1, 17]
        [17, 3] as Rational | [17, 3]
        [17 * 2, 3 * 2] as Rational | [17, 3]
        [1 * 9, 3 * 9] as Rational | [1, 3]
    }

    def "x/0"() {
        when: new Rational(10, 0)
        then: thrown ArithmeticException

        when: new Rational(10).div(0)
        then: thrown ArithmeticException

        when: new Rational(10).div(new Rational(0))
        then: thrown ArithmeticException
    }

    def "x/-"() {
        when: new Rational(10, -5)
        then: thrown ArithmeticException
    }
}
