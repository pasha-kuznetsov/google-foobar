package com.tydbits.google.challenges.L2_1_gearing_up_for_destruction;

public class Rational
{
    private final long num;
    private final long denom;

    public Rational(long num, long denom) {
        this.num = num;
        this.denom = denom;
        if (denom <= 0) throw new ArithmeticException("denom <= 0");
    }

    public Rational(long num) {
        this.num = num;
        this.denom = 1;
    }

    public Rational() {
        this.num = 0;
        this.denom = 1;
    }

    public long getNum() {
        return num;
    }

    public long getDenom() {
        return denom;
    }

    public Rational add(Rational other) {
        return new Rational(num * other.denom + other.num * denom, denom * other.denom);
    }

    public Rational add(long other) {
        return new Rational(num + other * denom, denom);
    }

    public Rational sub(Rational other) {
        return new Rational(num * other.denom - other.num * denom, denom * other.denom);
    }

    public Rational sub(long other) {
        return new Rational(num - other * denom, denom);
    }

    public Rational div(Rational other) {
        int sign = other.num < 0 ? -1 : 1;
        return new Rational(num * other.denom * sign).div(other.num * denom * sign);
    }

    public Rational div(long other) {
        return new Rational(num, denom * other);
    }

    public Rational mul(Rational other) {
        return new Rational(num * other.num, denom * other.denom);
    }

    public int compareTo(Rational other) {
        return Long.compare(num * other.denom, other.num * denom);
    }

    public int compareTo(long other) {
        return Long.compare(num, other * denom);
    }

    public Rational Simplify() {
        if (num == 0) return new Rational();
        long d = num > denom ? gcd(num, denom) : gcd(denom, num);
        return new Rational(num/d,denom/d);
    }

    private long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    @Override
    public String toString() {
        return num + "/" + denom;
    }
}
