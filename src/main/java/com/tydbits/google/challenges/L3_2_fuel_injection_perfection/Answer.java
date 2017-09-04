package com.tydbits.google.challenges.L3_2_fuel_injection_perfection;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Answer {
    private static final BigInteger one = BigInteger.ONE;
    private static final BigInteger two = new BigInteger("2");

    public static int answer(String n) {
        HashSet<BigInteger> seen = new HashSet<>();
        PriorityQueue<Step> queue = new PriorityQueue<>();
        queue.add(new Step(new BigInteger(n), 0));
        do {
            Step s = queue.poll();
            if (s.num.compareTo(one) <= 0)
                return s.step;
            if (!seen.add(s.num))
                continue;
            BigInteger half = s.num.divide(two);
            if (s.isEven()) {
                queue.add(new Step(half, s.step + 1));          //  num -> num/2
            } else {
                queue.add(new Step(half, s.step + 2));          //  num -> num-1 -> num-1 / 2
                queue.add(new Step(half.add(one), s.step + 2)); //  num -> num+1 -> num+1 / 2
            }
        } while (!queue.isEmpty());
        return Integer.MAX_VALUE;                               // ???
    }

    private static class Step implements Comparable<Step> {
        BigInteger num;
        int step;

        Step(BigInteger num, int fromStart) {
            this.num = num;
            this.step = fromStart;
        }

        @Override
        public int compareTo(Step o) {
            int stepCmp = Integer.compare(step, o.step);
            if (stepCmp != 0) return stepCmp;
            int evenCmp = Integer.compare(isEven() ? -1 : +1, o.isEven() ? -1 : +1);
            if (evenCmp != 0) return evenCmp;
            return num.compareTo(o.num);
        }

        private boolean isEven() {
            return !num.testBit(0);
        }
    }
}
