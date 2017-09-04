package com.tydbits.google.challenges.L4_1_distract_the_guards;

import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;

public class Answer {
    private static final HashSet<Pair> finite = new HashSet<>();
    private static final HashSet<Pair> infinite = new HashSet<>();

    public static int answer(int[] banana_list) {
        return new Query(banana_list).minFinite();
    }

    static class Query {
        private final int[] bananas;
        private final HashMap<BitSet /* mask */, BitSet /* pairing */> bestPairings;

        Query(int[] bananas) {
            this.bananas = bananas;
            this.bestPairings = new HashMap<>();
        }

        int minFinite() {
            BitSet bestPairing = bestPairing(new BitSet(bananas.length), 0);
            return bananas.length - bestPairing.cardinality();
        }

        private BitSet bestPairing(BitSet mask, int start) {
            BitSet pairing = bestPairings.get(mask);
            if (pairing == null)
                bestPairings.put(mask, pairing = newBestPairing(mask, start));
            return pairing;
        }

        private BitSet newBestPairing(BitSet mask, int start) {
            BitSet bestPairing = mask;
            for (int i = mask.nextClearBit(start); i < bananas.length - 1 && !mask.get(i); mask.nextClearBit(++i)) {
                for (int j = mask.nextClearBit(i + 1); j < bananas.length && !mask.get(j); mask.nextClearBit(++j)) {
                    if (!isFiniteMatch(new Pair(bananas[i], bananas[j]))) {
                        BitSet pairing = (BitSet) bestPairing(markPaired(mask, i, j), i + 1).clone();
                        pairing.clear(0, i);
                        pairing.or(mask);
                        int cardinality = pairing.cardinality();
                        if (cardinality >= bananas.length)
                            return pairing;
                        if (cardinality > bestPairing.cardinality())
                            bestPairing = pairing;
                    }
                }
            }
            return bestPairing;
        }

        private BitSet markPaired(BitSet mask, int i, int j) {
            BitSet m = (BitSet) mask.clone();
            m.set(0, i + 1);
            m.set(j);
            return m;
        }
    }

    static boolean isFiniteMatch(Pair pair) {
        HashSet<Pair> seen = new HashSet<>();
        for (; canMatch(pair) && !finite.contains(pair); pair = match(pair)) {
            if (infinite.contains(pair) || !seen.add(pair)) {
                infinite.addAll(seen);
                return false;
            }
        }
        finite.addAll(seen);
        return true;
    }

    private static boolean canMatch(Pair pair) {
        return pair.larger != pair.smaller && pair.larger > 0 && pair.smaller > 0;
    }

    private static Pair match(Pair pair) {
        return new Pair(pair.smaller * 2, pair.larger - pair.smaller);
    }

    static class Pair implements Comparable<Pair> {
        private final int smaller;
        private final int larger;

        Pair(int a, int b) {
            if (a > b){
                int d = gcd(b, a);
                this.larger = a / d;
                this.smaller = b / d;
            } else {
                int d = gcd(a, b);
                this.larger = b / d;
                this.smaller = a / d;
            }
        }

        private static int gcd(int larger, int smaller) {
            return smaller == 0 ? larger : gcd(smaller, larger % smaller);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Pair && (obj == this || compareTo((Pair) obj) == 0);
        }

        @Override
        public int compareTo(Pair o) {
            int aCmp = Integer.compare(smaller, o.smaller);
            if (aCmp != 0) return aCmp;
            return Integer.compare(larger, o.larger);
        }

        @Override
        public int hashCode() {
            return new Long(((long) smaller << 32) | larger).hashCode();
        }

        @Override
        public String toString() {
            return "(" + smaller + ", " + larger + ")";
        }
    }
}
