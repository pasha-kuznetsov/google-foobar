package com.tydbits.google.foobar.L5_expanding_nebula

import spock.lang.Specification
import spock.lang.Unroll

class L5_AnswerTest extends Specification {

    @Unroll
    def "test basic #g"() {
        given:
        def q = new Answer.Query(toBool(g))
//        q.print = true;

        expect:
        q.count() == expectedAnswer

        where:
        g       | expectedAnswer

        ["o"]   | 4

        ["."]   | 12

        ["o.",
         ".o"]  | 12

        [".o",
         "o."]  | 12

        ["o.o",
         "o.o"] | 10

        /*
        ..o.
        .o..
        ..o.

        ....
        .oo.
        ....

        ....
        o..o
        ....

        ...o
        o...
        ...o

        .oo.
        ....
        .oo.

        .o..
        ..o.
        .o..

        .oo.
        ....
        o..o

        o..o
        ....
        .oo.

        o...
        ...o
        o...

        o..o
        ....
        o..o
        */

        ["o.o",
         ".o.",
         "o.o"] | 4
    }

    @Unroll
    def "test advanced #g"() {
        expect:
        answer(g) == expectedAnswer

        where:
        g              | expectedAnswer

        ["o.o..ooo",
         "o.o...o.",
         "ooo...o.",
         "o.o...o.",
         "o.o..ooo"]   | 254

        ["oo.o.o.oo.",
         "oo....ooo.",
         "oo.......o",
         ".o....oo.."] | 11567
    }

    private static int answer(g) {
        Answer.answer(toBool(g))
    }

    private static boolean[][] toBool(g) {
        g.collect { it.collect { it == 'o' } } as boolean[][]
    }
}
