package com.tydbits.google.foobar.L2_2_dont_get_volunteered;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class Answer {

    public static int answer(int src, int dest) {
        int[] visited = new int[64];
        visited[src] = 1;
        Queue<Move> to_visit = new ArrayDeque<>();
        to_visit.add(new Move(src, 0));
        do {
            Move move = to_visit.poll();
            if (move.dest == dest) return move.depth;
            for (int next : possibleMoves(move.dest)) {
                if (visited[next]++ > 0) continue;
                to_visit.add(new Move(next, move.depth + 1));
            }
        } while (!to_visit.isEmpty());
        return 0;
    }

    private static class Move {
        final int depth;
        final int dest;

        Move(int dest, int depth) {
            this.dest = dest;
            this.depth = depth;
        }
    }

    // -------------------------
    // | 0| 1| 2| 3| 4| 5| 6| 7|
    // -------------------------
    // | 8| 9|10|11|12|13|14|15|
    // -------------------------
    // |16|17|18|19|20|21|22|23|
    // -------------------------
    // |24|25|26|27|28|29|30|31|
    // -------------------------
    // |32|33|34|35|36|37|38|39|
    // -------------------------
    // |40|41|42|43|44|45|46|47|
    // -------------------------
    // |48|49|50|51|52|53|54|55|
    // -------------------------
    // |56|57|58|59|60|61|62|63|
    // -------------------------
    private static final int[][] knightMoves = {
            {-1, -2}, {-2, -1}, {-2, +1}, {-1, +2},
            {+1, +2}, {+2, +1}, {+2, -1}, {+1, -2},
    };

    public static ArrayList<Integer> possibleMoves(int src) {
        int[] pos = fromBoard(src);
        ArrayList<Integer> moves = new ArrayList<>();
        for (int[] offset : knightMoves) {
            int[] newPos = changePos(pos, offset);
            if (isValid(newPos)) continue;
            moves.add(toBoard(newPos));
        }
        return moves;
    }

    private static int[] changePos(int[] pos, int[] offset) {
        return new int[]{pos[0] + offset[0], pos[1] + offset[1]};
    }

    private static boolean isValid(int[] pos) {
        return pos[0] < 0 || pos[0] > 7 || pos[1] < 0 || pos[1] > 7;
    }

    private static int[] fromBoard(int src) {
        return new int[] {src / 8, src % 8};
    }

    private static int toBoard(int[] pos) {
        return pos[0] * 8 + pos[1];
    }
}
