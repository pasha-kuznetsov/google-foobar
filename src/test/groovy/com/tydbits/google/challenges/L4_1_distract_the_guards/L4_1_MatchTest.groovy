package com.tydbits.google.challenges.L4_1_distract_the_guards

import spock.lang.Specification
import spock.lang.Unroll

class L4_1_MatchTest extends Specification {

    @Unroll
    def "test finite #m"() {
        expect:
        !Answer.Match.isInfinite(m as Answer.Match)

        where:
        m        | _
        [1, 1]   | _
        [7, 7]   | _
        [7, 21]  | _
        [42, 14] | _
    }

    @Unroll
    def "test infinite #m"() {
        expect:
        Answer.Match.isInfinite(m as Answer.Match)

        where:
        m       | _
        [1, 4]  | _
        [4, 1]  | _
        [1, 19] | _
        [19, 1] | _
        [7, 3]  | _
        [3, 7]  | _
        [1, 13] | _
        [1, 21] | _
//                pair([3, 7]), pair([3, 19]),
//                pair([7, 13]), pair([7, 19]),
//                pair([13, 21]),
//                pair([19, 21])] // as ArrayList<Answer.Pair>
//        [7, 7, 5, 21, 7, 19] | [
//                pair([5, 7]), pair([5, 19]), pair([5, 21]),
//                pair([7, 19]),
//                pair([19, 21])] // as ArrayList<Answer.Pair>
    }
}
