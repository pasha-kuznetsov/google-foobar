package com.tydbits.google.challenges.L2_1_gearing_up_for_destruction;

public class Answer {
    private static final int[] invalid = {-1, -1};

    public static int[] answer(int[] pegs) {
        // find out lastGear for firstGear == 1
        Rational firstGear1 = new Rational(1);
        Rational[] gears1 = run(firstGear1, pegs);
        Rational lastGear1 = gears1[gears1.length - 1];

        // find the firstGear from the above lastGear found for firstGear == 1
        Rational firstGear = pegs.length % 2 == 0
                ? evenFirstGear(firstGear1, lastGear1)
                : oddFirstGear(firstGear1, lastGear1);

        // now run the found firstGear to make sure the other gears are valid, i.e. (0; distance)
        Rational[] gears = run(firstGear, pegs);
        for (int i = 0; i < gears.length; i++) {
            Rational gear = gears[i];
            if (gear.compareTo(1) < 0) return invalid; // not specified in readme for gears except first
            if (i > 0 && gear.compareTo(pegs[i] - pegs[i - 1]) >= 0) return invalid;
            if (i < gears.length - 1 && gear.compareTo(pegs[i + 1] - pegs[i]) >= 0) return invalid;
        }

        return toArray(firstGear);
    }

    // even number of pegs
    // lastGear = N - firstGear
    // N = lastGear + firstGear
    // firstGear = 2 * lastGear -- the firstGear we're looking for
    // firstGear = 2 * N - 2 * firstGear
    // firstGear = 2/3 * N
    private static Rational evenFirstGear(Rational firstGear, Rational lastGear) {
        Rational n = lastGear.add(firstGear);
        return new Rational(2, 3).mul(n);
    }

    // odd number of pegs
    // lastGear = firstGear - N
    // N = firstGear - lastGear -- can find out for fixed firstGear, say 1
    // firstGear = 2 * lastGear -- the firstGear we're looking for
    // firstGear = 2 * firstGear - 2 * N
    // firstGear = 2 * N
    private static Rational oddFirstGear(Rational firstGear, Rational lastGear) {
        Rational n = firstGear.sub(lastGear);
        return new Rational(2).mul(n);
    }

    private static Rational[] run(Rational firstGear, int[] pegs) {
        Rational[] gears = new Rational[pegs.length];
        gears[0] = firstGear;
        for (int i = 1; i < pegs.length; i++)
            gears[i] = new Rational(pegs[i] - pegs[i - 1]).sub(gears[i - 1]);
        return gears;
    }

    private static int[] toArray(Rational rational) {
        if (rational == null) return invalid;
        Rational simplified = rational.Simplify();
        return new int[]{(int) simplified.getNum(), (int) simplified.getDenom()};
    }
}
