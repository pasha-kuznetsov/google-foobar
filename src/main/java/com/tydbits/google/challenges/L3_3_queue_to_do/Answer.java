package com.tydbits.google.challenges.L3_3_queue_to_do;

public class Answer {
    public static int answer(int start, int length) {
        int checksum = 0;
        for (int line = 0; line < length; ++line) {
            for (int w = 0; w < length - line; ++w) {
                int worker = start + line * length + w;
                checksum = checksum ^ worker;
            }
        }
        return checksum;
    }
}
