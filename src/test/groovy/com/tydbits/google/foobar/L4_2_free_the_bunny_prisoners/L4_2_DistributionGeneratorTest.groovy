package com.tydbits.google.foobar.L4_2_free_the_bunny_prisoners

import spock.lang.Specification
import spock.lang.Unroll

class L4_2_DistributionGeneratorTest extends Specification {
    @Unroll
    def "test #buns #keyCopies"() {
        given:
        def results = new ArrayList<int[]>();
        def gen = new Answer.DistributionGenerator(buns, keyCopies)

        when:
        for (int[] result = gen.next(); result != null; result = gen.next())
            results.add(result);

        then:
        results == expectedAnswer as ArrayList<int[]>

        where:
        buns | keyCopies || expectedAnswer
        1    | 1         || [[0]]
        4    | 2         || [[0, 1], [0, 2], [0, 3], [1, 2], [1, 3], [2, 3]]
    }
}
