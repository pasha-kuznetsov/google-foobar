package com.tydbits.google.challenges.L4_1_distract_the_guards;

import java.util.*;

// everything has to be in one class for Google FooBar
public class Answer {
    public static int answer(int[] banana_list) {
        return banana_list.length - new Query(banana_list).maxMatching();
    }

    static class Query {
        final Guard[] guards;

        Query(int[] bananas) {
            this.guards = new GuardsData(bananas).sortGuards(new GuardPairsSizeCmp());
        }

        // sort by number of connections, trying to minimize freeUp calls
        private class GuardPairsSizeCmp implements Comparator<Guard> {
            @Override
            public int compare(Guard o1, Guard o2) {
                return Integer.compare(o1.matches.size(), o2.matches.size());
            }
        }

        int maxMatching() {
            for (Guard guard : guards) {
                if (guard.pair == null)
                    new MatchMaker().pairUp(guard);
            }
            return countPaired();
        }

        private int countPaired() {
            int paired = 0;
            for (Guard guard : guards) {
                if (guard.pair != null)
                    paired = paired + 1;
            }
            return paired;
        }
    }

    private static class MatchMaker {
        private HashSet<Guard> seen = new HashSet<>();

        boolean pairUp(Guard guard) {
            if (!seen.add(guard)) return false;
            Guard pair = findFreePair(guard);
            if (pair == null) pair = freeUpPair(guard);
            if (pair == null) return false;
            guard.pair = pair;
            pair.pair = guard;
            return true;
        }

        private Guard findFreePair(Guard guard) {
            for (Guard pair : guard.matches) {
                if (pair.pair != null) continue;
                if (!seen.add(pair)) continue;
                return pair;
            }
            return null;
        }

        private Guard freeUpPair(Guard guard) {
            for (Guard pair : guard.matches) {
                if (!seen.add(pair)) continue;
                if (!pairUp(pair.pair)) continue;
                pair.pair = null;
                return pair;
            }
            return null;
        }
    }

    static class Guard {
        ArrayList<Guard> matches = new ArrayList<>();
        Guard pair;
    }

    static class Match {
        private final int a;
        private final int b;

        Match(int a, int b) {
            this.a = Math.max(a, b);
            this.b = Math.min(a, b);
        }

        static boolean isInfinite(Match m) {
            for (; ; ) {
                if (m.a == m.b || m.a <= 0 && m.b <= 0) return false;
                if ((m.a + m.b) % 2 == 1) return true;
                int gcd = gcd(m.a, m.b);
                m = new Match((m.a - m.b) / gcd, m.b / gcd * 2);
            }
        }

        private static int gcd(int a, int b) {
            while (b > 0) {
                int temp = b;
                b = a % b;
                a = temp;
            }
            return a;
        }
    }

    private static class GuardsData {
        private HashMap<Integer, Guard> map = new HashMap<>();

        GuardsData(int[] bananas) {
            for (int i = 0; i < bananas.length - 1; i++) {
                for (int j = i + 1; j < bananas.length; j++) {
                    if (Match.isInfinite(new Match(bananas[i], bananas[j])))
                        connectGuards(guard(i), guard(j));
                }
            }
        }

        private void connectGuards(Guard guard1, Guard guard2) {
            guard1.matches.add(guard2);
            guard2.matches.add(guard1);
        }

        private Guard guard(int id) {
            Guard guard = map.get(id);
            if (guard == null) map.put(id, guard = new Guard());
            return guard;
        }

        Guard[] sortGuards(Comparator<Guard> cmp) {
            Guard[] guards = map.values().toArray(new Guard[map.size()]);
            Arrays.sort(guards, cmp);
            for (Guard guard : guards)
                Collections.sort(guard.matches, cmp);
            return guards;
        }
    }
}
