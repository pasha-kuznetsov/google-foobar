package com.tydbits.google.foobar.L5_1_expanding_nebula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Answer {
    public static int answer(boolean[][] g) {
        return new Query(g).count();
    }

    static class Query {
        private final ArrayList<Pattern> gasPatterns;
        private final ArrayList<Pattern> emptyPatterns;
        private final Cell[][] cells;
        private final ArrayList<Columns> columns;
        private int terminal;

        Query(boolean[][] g) {
            this.gasPatterns = new ArrayList<>();
            this.emptyPatterns = new ArrayList<>();
            for (String top : new String[] {"..", ".o", "o.", "oo"}) {
                for (String bottom : new String[]{"..", ".o", "o.", "oo"}) {
                    // empty cell -> 1 non-empty predecessor
                    // non-empty -> other number of non-empty predecessors
                    Pattern pattern = new Pattern(top, bottom);
                    (pattern.count == 1 ? gasPatterns : emptyPatterns).add(pattern);
                }
            }

            this.cells = new Cell[g.length][g.length == 0 ? 1 : g[0].length];
            for (int i = 0; i < cells.length; ++i) {
                for (int j = 0; j < cells[i].length; ++j) {
                    cells[i][j] = new Cell(i, j, g[i][j] ? gasPatterns : emptyPatterns);
                }
            }

            this.columns = new ArrayList<>();
            for (int j = 0; j < cells[0].length; ++j)
                columns.add(generateColumns(new Columns(j), "", "", null, cells[0][j]));
        }

        private Columns generateColumns(Columns columns, String left, String right, Pattern top, Cell cell) {
            for (Pattern pattern : cell.patterns) {
                if (top != null && !pattern.top.equals(top.bottom))
                    continue;
                if (cell.row == cells.length - 1) {
                    Column column = new Column(left + pattern.left, right + pattern.right);
                    columns.add(column);
                } else {
                    generateColumns(columns,
                            left + pattern.left.charAt(0),
                            right + pattern.right.charAt(0),
                            pattern,
                            cells[cell.row + 1][cell.column]);
                }
            }
            return columns;
        }

        int count() {
            this.terminal = 0;
            countColumns(columns.get(0));
            return terminal;
        }

        private void countColumns(Columns c) {
            for (HashSet<Column> right : c.right.values()) {
                for (Column column : right) {
                    countColumns(c, column);
                }
            }
        }

        private void countColumns(Columns c, Column column) {
            if (c.column == columns.size() - 1) {
                terminal += 1;
            }
            else
                countColumns(columns.get(c.column + 1), column.right);
        }

        private void countColumns(Columns c, String leftKey) {
            HashSet<Column> set = c.left.get(leftKey);
            if (set == null)
                return;
            for (Column column : set)
                countColumns(c, column);
        }

        static class Columns {
            private final int column;
            final HashMap<String, HashSet<Column>> left;
            final HashMap<String, HashSet<Column>> right;

            Columns(int column) {
                this.column = column;
                this.left = new HashMap<>();
                this.right = new HashMap<>();
            }

            public void add(Column column) {
                add(left, column.left, column);
                add(right, column.right, column);
            }

            private void add(HashMap<String, HashSet<Column>> map, String key, Column column) {
                HashSet<Column> set = map.get(key);
                if (set == null) map.put(key, set = new HashSet<>());
                set.add(column);
            }
        }

        static class Column {
            String left = "";
            String right = "";

            Column(String left, String right) {
                this.left = left;
                this.right = right;
            }
        }

        static class Cell {
            int row;
            int column;
            private ArrayList<Pattern>/*TODO Set<Pattern>*/ patterns;

            Cell(int row, int column, ArrayList<Pattern> patterns) {
                this.row = row;
                this.column = column;
                this.patterns = new ArrayList<>(patterns);
            }
        }
    }

    static class Pattern {
        String top;
        String bottom;
        String left;
        String right;
        int count;

        Pattern(String top, String bottom) {
            this.top = top;
            this.bottom = bottom;
            this.left = top.substring(0, 1) + bottom.substring(0, 1);
            this.right = top.substring(1, 2) + bottom.substring(1, 2);
            this.count = (top + bottom).replace(".", "").length();
        }
    }
}
