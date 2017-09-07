package com.tydbits.google.foobar.L4_2_free_the_bunny_prisoners

import spock.lang.Specification
import spock.lang.Unroll

class L4_2_AnswerTest extends Specification {
    def answer = new Answer()

    @Unroll
    def "debug #buns #required"() {
        expect:
        answer.answer(buns, required) == expectedAnswer as int[][]

        where:
        buns | required || expectedAnswer
        1    | 1        || [[0]]
        4    | 3        || [[0, 1, 2, _, _, _].findAll {it != _},
                            [0, _, _, 3, 4, _].findAll {it != _},
                            [_, 1, _, 3, _, 5].findAll {it != _},
                            [_, _, 2, _, 4, 5].findAll {it != _}]
    }

    @Unroll
    def "test #buns #required"() {
        expect:
        answer.answer(buns, required) == expectedAnswer as int[][]

        where:
        buns | required || expectedAnswer
        1    | 0        || [[]]
        1    | 1        || [[0]]

        2    | 0        || [[], []]
        2    | 1        || [[0], [0]]
        2    | 2        || [[0], [1]]

        3    | 0        || [[], [], []]
        3    | 1        || [[0], [0], [0]]
        3    | 2        || [[0, 1, _].findAll {it != _},
                            [0, _, 2].findAll {it != _},
                            [_, 1, 2].findAll {it != _}]
        3    | 3        || [[0], [1], [2]]

        4    | 0        || [[], [], [], []]
        4    | 1        || [[0], [0], [0], [0]]
        4    | 2        || [[0, 1, 2, _].findAll {it != _},        // 3 copies of each key
                            [0, 1, _, 3].findAll {it != _},        // 1 missing keys in 1 keychain
                            [0, _, 2, 3].findAll {it != _},        // 2 overlap
                            [_, 1, 2, 3].findAll {it != _}]
        4    | 3        || [[0, 1, 2, _, _, _].findAll {it != _},  // 2 copies of each key
                            [0, _, _, 3, 4, _].findAll {it != _},  // 1 missing key in 2 keychains
                            [_, 1, _, 3, _, 5].findAll {it != _},  // 1 overlap
                            [_, _, 2, _, 4, 5].findAll {it != _}]
        4    | 4        || [[0], [1], [2], [3]]

        5    | 0        || [[], [], [], [], []]
        5    | 1        || [[0], [0], [0], [0], [0]]            // 5 copies, 1 overlap, no missed keys
        5    | 2        || [[0, 1, 2, 3, _].findAll {it != _},  // 4 copies of each key
                            [0, 1, 2, _, 4].findAll {it != _},  // 1 missing key in 1 keychain
                            [0, 1, _, 3, 4].findAll {it != _},  // 3 overlap
                            [0, _, 2, 3, 4].findAll {it != _},
                            [_, 1, 2, 3, 4].findAll {it != _}]
        5    | 3        || [[0, 1, 2, 3, 4, 5, _, _, _, _].findAll {it != _},   // 3 copies of each key
                            [0, 1, 2, _, _, _, 6, 7, 8, _].findAll {it != _},   // 1 missing key in 2 keychains
                            [0, _, _, 3, 4, _, 6, 7, _, 9].findAll {it != _},   // 3 overlap (10 - 1) * 2 / 6
                            [_, 1, _, 3, _, 5, 6, _, 8, 9].findAll {it != _},
                            [_, _, 2, _, 4, 5, _, 7, 8, 9].findAll {it != _}]
        5    | 4        || [[0, 1, 2, 3, _, _, _, _, _, _].findAll {it != _},   // 2 copies of each key
                            [0, _, _, _, 4, 5, 6, _, _, _].findAll {it != _},   // 1 missing key in 3 keychains
                            [_, 1, _, _, 4, _, _, 7, 8, _].findAll {it != _},   // 1 overlap
                            [_, _, 2, _, _, 5, _, 7, _, 9].findAll {it != _},
                            [_, _, _, 3, _, _, 6, _, 8, 9].findAll {it != _}]
        5    | 5        || [[0], [1], [2], [3], [4]]            // 1 copy, 5-5+1 = 1 mismatch
    }
}
