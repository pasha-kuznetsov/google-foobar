package com.tydbits.google.foobar.L5_1_expanding_nebula;

import java.util.ArrayList;
import java.util.Stack;

public class Answer {
    public static int answer(boolean[][] g) {
        return new Query(g).walk();
    }

    static class Query {
        private final boolean[][] g;
        private final ArrayList<Pattern> patterns;
        private final char[][] nebula;
        boolean print;

        Query(boolean[][] g) {
            this.g = g;

            this.patterns = new ArrayList<>();
            for (String top : new String[] {"..", ".o", "o.", "oo"}) {
                for (String bottom : new String[]{"..", ".o", "o.", "oo"}) {
                    patterns.add(new Pattern(top, bottom));
                }
            }

            for (Pattern pattern : patterns) {
                for (Pattern next : patterns) {
                    // if (next == pattern) continue;
                    if (pattern.bottom.equals(next.top)) pattern.nextBottom.add(next);
                    if (pattern.top.equals(next.bottom)) pattern.nextTop.add(next);
                    if (pattern.right.equals(next.left)) pattern.nextRight.add(next);
                }
            }

            this.nebula = new char[g.length + 1][g.length == 0 ? 1 : g[0].length + 1];
        }

        int walk() {
            int terminal = 0;

            Stack<Cell> stack = new Stack<>();
            stack.push(new Cell(0, 0, patterns));
            while (!stack.isEmpty()) {
                Cell cell = stack.pop();
                if (!updateNebula(cell))
                    continue;
                if (cell.hasMorePatterns())
                    stack.push(cell);

                // snake-walk the cells:
                // 1 6 7
                // 2 5 8
                // 3 4 9 ...

                if (isEven(cell.column) && !isLastRow(cell))
                    stack.push(new Cell(cell.row + 1, cell.column, cell.pattern.nextBottom));
                else if (isOdd(cell.column) && !isFirstRow(cell))
                    stack.push(new Cell(cell.row - 1, cell.column, cell.pattern.nextTop));
                else if (!isLastColumn(cell))
                    stack.push(new Cell(cell.row, cell.column + 1, cell.pattern.nextRight));
                else {
                    do {
                        ++terminal;
                        if (print) printNebula();
                    } while (updateNebula(cell));
                }
            }
            return terminal;
        }

        private boolean updateNebula(Cell cell) {
            for (Pattern pattern = cell.nextPattern(); pattern != null; pattern = cell.nextPattern()) {
                if (!matchesGenerationRules(cell.row, cell.column, pattern)) continue;
                if (cell.column == 0) {
                    nebula[cell.row][cell.column] = pattern.left.charAt(0);
                    nebula[cell.row + 1][cell.column] = pattern.left.charAt(1);
                } else {
                    if (nebula[cell.row][cell.column] != pattern.left.charAt(0)) continue;
                    if (nebula[cell.row + 1][cell.column] != pattern.left.charAt(1)) continue;
                }
                nebula[cell.row][cell.column + 1] = pattern.right.charAt(0);
                nebula[cell.row + 1][cell.column + 1] = pattern.right.charAt(1);
                return true;
            }
            return false;
        }

        // empty cell -> 1 non-empty predecessor
        // non-empty -> other number of non-empty predecessors
        private boolean matchesGenerationRules(int row, int column, Pattern pattern) {
            return (pattern.count == 1) == g[row][column];
        }

        private boolean isEven(int column) { return (column & 0b1) == 0; }
        private boolean isOdd(int column) { return (column & 0b1) == 1; }
        private boolean isFirstRow(Cell cell) { return cell.row == 0; }
        private boolean isLastRow(Cell cell) { return cell.row == g.length - 1; }
        private boolean isLastColumn(Cell cell) { return cell.column == g[cell.row].length - 1; }

        class Cell {
            int row;
            int column;
            private ArrayList<Pattern> patterns;
            private Pattern pattern;
            private int index;

            Cell(int row, int column, ArrayList<Pattern> patterns) {
                this.row = row;
                this.column = column;
                this.patterns = patterns;
                this.index = -1;
            }

            Pattern nextPattern() { return index + 1 < patterns.size() ? pattern = patterns.get(++index) : null; }

            boolean hasMorePatterns() { return index < patterns.size(); }

            @Override
            public String toString() {
                return "(" + row + ", " + column + ") [" +
                        (pattern == null ? "" : pattern.toString()) +
                        "] + " + (patterns.size() - index - 1);
            }
        }

        private void printNebula() {
            for (char[] row : nebula) {
                for (char c : row)
                    System.out.write(c);
                System.out.println();
            }
            System.out.println();
        }
    }

    static class Pattern {
        String top;
        String bottom;
        String left;
        String right;
        int count;

        ArrayList<Pattern> nextBottom;
        ArrayList<Pattern> nextTop;
        ArrayList<Pattern> nextRight;

        Pattern(String top, String bottom) {
            this.top = top;
            this.bottom = bottom;
            this.left = top.substring(0, 1) + bottom.substring(0, 1);
            this.right = top.substring(1, 2) + bottom.substring(1, 2);
            this.count = (top + bottom).replace(".", "").length();
            this.nextBottom = new ArrayList<>();
            this.nextTop = new ArrayList<>();
            this.nextRight = new ArrayList<>();
        }

        @Override
        public String toString() {
            return top + ' ' + bottom;
        }
    }
}
