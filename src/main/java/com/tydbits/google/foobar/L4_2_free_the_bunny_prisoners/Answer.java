package com.tydbits.google.foobar.L4_2_free_the_bunny_prisoners;

import java.util.ArrayList;

public class Answer {

    public static int[][] answer(int num_buns, int num_required) {

        // to allow any `num_buns` to have at least one copy of each key
        // there should be this many copies of each key distributed between all buns
        int keyCopies = num_required > 0 ? num_buns - num_required + 1 : 0;
        if (keyCopies > num_buns)
            keyCopies = num_buns;

        ArrayList<ArrayList<Integer>> keychains = new ArrayList<>(num_buns);
        for (int pos = 0; pos < num_buns; ++pos)
            keychains.add(new ArrayList<Integer>());

        int key = 0;
        DistributionGenerator distributions = new DistributionGenerator(num_buns, keyCopies);
        for (int[] distribution = distributions.next(); distribution != null; distribution = distributions.next()) {
            for (int keychainIndex : distribution) {
                keychains.get(keychainIndex).add(key);
            }
            ++key;
        }

        int[][] result = new int[num_buns][];
        for (int keychainIndex = 0; keychainIndex < keychains.size(); ++keychainIndex) {
            ArrayList<Integer> keychain = keychains.get(keychainIndex);
            result[keychainIndex] = new int[keychain.size()];
            for (int i = 0; i < keychain.size(); ++i) {
                result[keychainIndex][i] = keychain.get(i);
            }
        }

        return result;
    }

    private static class Keychain {
        ArrayList<Integer> keys;
    }

    // generates key distributions in lexicographic order
    // using `base` == num buns and `digits` == key copies, for 5 / 3:
    //
    // 0    0    0    0    0    0
    // 1    1    1                -> 1    1    1
    // 2           -> 2    2         2    2      -> 2
    //   -> 3         3      -> 3    3      -> 3    3
    //        -> 4      -> 4    4      -> 4    4    4
    //
    static class DistributionGenerator {
        private final int base;
        private final int[] digits;
        private boolean started;

        DistributionGenerator(int base, int digits) {
            if (digits > base)
                digits = base;
            this.base = base;
            this.digits = new int[digits];
            this.started = false;
        }

        int[] next() {
            return start() || increment() ? digits.clone() : null;
        }

        private boolean start() {
            if (started) return false;
            started = true;
            for (int i = 0; i < digits.length; ++i)
                digits[i] = i;
            return true;
        }

        private boolean increment() {
            for (int i = digits.length - 1; i >= 0; --i) {
                if (digits[i] < base - digits.length + i) {
                    digits[i] += 1;
                    for (++i; i < digits.length; ++i)
                        digits[i] = digits[i - 1] + 1;
                    return true;
                }
            }
            return false;
        }
    }
}
