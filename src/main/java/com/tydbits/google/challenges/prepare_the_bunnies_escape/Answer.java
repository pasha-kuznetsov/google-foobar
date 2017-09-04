package com.tydbits.google.challenges.prepare_the_bunnies_escape;

import java.util.*;

public class Answer {
    public static int answer(int[][] maze) {
        int rows = maze.length;
        int columns = maze[0].length;

        int[] start = {0, 0};
        int[] finish = {rows - 1, columns - 1};

        Query fromStart = new Query(maze, rows, columns);
        int pathLen = fromStart.run(start, finish);

        Query fromFinish = new Query(maze, rows, columns);
        fromFinish.run(finish, start);

        for (int startDistance : fromStart.wallsByDistance.keySet()) {
            for (int id : fromStart.wallsByDistance.get(startDistance)) {
                Integer finishDistance = fromFinish.wallsById.get(id);
                if (finishDistance == null) continue;
                int wallPathLen = startDistance + finishDistance - 1;
                if (pathLen <= 0 || wallPathLen < pathLen)
                    pathLen = wallPathLen;
            }
        }

        return pathLen;
    }

    private static class Query {
        private final int[][] maze;
        private final int rows;
        private final int columns;
        final TreeMap<Integer /* distance */, List<Integer> /* id */> wallsByDistance;
        final HashMap<Integer /* id */, Integer /* distance */> wallsById;

        Query(int[][] maze, int rows, int columns) {
            this.maze = maze;
            this.rows = rows;
            this.columns = columns;
            wallsByDistance = new TreeMap<>();
            wallsById = new HashMap<>();
        }

        int run(int[] start, int[] finish) {
            HashSet<Integer> visited = new HashSet<>();
            Queue<Cell> to_visit = new ArrayDeque<>();
            Cell startCell = new Cell(start, false, 1);
            visited.add(startCell.id);
            to_visit.add(startCell);
            do {
                Cell cell = to_visit.poll();
                if (Arrays.equals(cell.pos, finish)) return cell.distance;
                for (Cell next : nextCells(cell)) {
                    if (!visited.add(next.id)) continue;
                    if (next.wall) {
                        List<Integer> walls = wallsByDistance.get(next.distance);
                        if (walls == null) wallsByDistance.put(next.distance, walls = new ArrayList<>());
                        walls.add(next.id);
                        wallsById.put(next.id, next.distance);
                    } else {
                        to_visit.add(next);
                    }
                }
            } while (!to_visit.isEmpty());
            return -1;
        }

        private static int[][] moves = new int[][] {{-1, 0}, {0, -1}, {+1, 0}, {0, +1}};

        private ArrayList<Cell> nextCells(Cell cell) {
            ArrayList<Cell> next = new ArrayList<>();
            for (int[] move : moves) {
                int i = cell.pos[0] + move[0];
                int j = cell.pos[1] + move[1];
                if (i < 0 || i >= rows) continue;
                if (j < 0 || j >= columns) continue;
                next.add(new Cell(new int[]{i, j}, maze[i][j] != 0, cell.distance + 1));
            }
            return next;
        }

        private static class Cell {
            final int id;
            final int[] pos;
            final boolean wall;
            final int distance;

            private Cell(int[] pos, boolean wall, int distance) {
                this.id = (pos[0] * 10000) + pos[1];
                this.pos = pos;
                this.wall = wall;
                this.distance = distance;
            }
        }
    }
}
