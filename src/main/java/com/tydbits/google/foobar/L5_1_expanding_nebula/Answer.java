package com.tydbits.google.foobar.L5_1_expanding_nebula;

import java.util.*;

public class Answer {
    public static int answer(boolean[][] g) {
        return new Query(g).count();
    }

    static class Query {
        private final ArrayList<Pattern> gasPatterns;
        private final ArrayList<Pattern> emptyPatterns;
        private final Cell[][] cells;
        private final ArrayList<Rows> rows;
        private int terminal;

        Query(boolean[][] g) {
            this.gasPatterns = new ArrayList<>();
            this.emptyPatterns = new ArrayList<>();
            for (String top : new String[]{"..", ".o", "o.", "oo"}) {
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

            this.rows = new ArrayList<>();
            for (int row = 0; row < cells.length; ++row)
                rows.add(generateRows(new Rows(row), "", "", null, cells[row][0]));

            boolean restricted;
            do {
                restricted = false;
                for (int row = 1; row < rows.size(); ++row)
                    if (restrict(rows.get(row - 1), rows.get(row)))
                        restricted = true;
            } while (restricted);
        }

        private Rows generateRows(Rows rows, String top, String bottom, Pattern left, Cell cell) {
            HashSet<Pattern> patterns = left == null ? cell.patterns : cell.patternsByLeft.get(left.right);
            if (patterns != null) {
                for (Pattern pattern : patterns) {
                    generateRows(rows, top, bottom, cell, pattern);
                }
            }
            return rows;
        }

        private void generateRows(Rows rows, String top, String bottom, Cell cell, Pattern pattern) {
            if (cell.column == cells[0].length - 1)
                rows.add(new Row(top + pattern.top, bottom + pattern.bottom));
            else
                generateRows(rows,
                        top + pattern.top.charAt(0),
                        bottom + pattern.bottom.charAt(0),
                        pattern,
                        cells[cell.row][cell.column + 1]);
        }

        private boolean restrict(Rows left, Rows right) {
            HashSet<String> common = intersect(left.bottom.keySet(), right.top.keySet());
            if (common.size() == left.bottom.size() && common.size() == right.top.size())
                return false;
            left.removeBottom(diff(left.bottom.keySet(), common));
            right.removeTop(diff(right.top.keySet(), common));
            return true;
        }

        int count() {
            this.terminal = 0;
            countRows(rows.get(0));
            return terminal;
        }

        private void countRows(Rows rows) {
            for (HashSet<Row> right : rows.bottom.values()) {
                for (Row row : right) {
                    countRows(rows, row);
                }
            }
        }

        private void countRows(Rows c, Row row) {
            if (c.row == rows.size() - 1)
                terminal += 1;
            else
                countRows(rows.get(c.row + 1), row.bottom);
        }

        private void countRows(Rows c, String top) {
            HashSet<Row> set = c.top.get(top);
            if (set == null)
                return;
            for (Row row : set)
                countRows(c, row);
        }

        static class Rows {
            private final int row;
            final Multimap<String, Row> top;
            final Multimap<String, Row> bottom;

            Rows(int row) {
                this.row = row;
                this.top = new Multimap<>();
                this.bottom = new Multimap<>();
            }

            void add(Row row) {
                top.addValue(row.top, row);
                bottom.addValue(row.bottom, row);
            }

            void removeTop(Set<String> remove) {
                for (String key : remove) {
                    for (Row c : top.get(key)) {
                        bottom.removeValue(c.bottom, c);
                    }
                }
                top.keySet().removeAll(remove);
            }

            void removeBottom(Set<String> remove) {
                for (String key : remove) {
                    for (Row c : bottom.get(key)) {
                        top.removeValue(c.top, c);
                    }
                }
                bottom.keySet().removeAll(remove);
            }
        }

        static class Row {
            String top = "";
            String bottom = "";

            Row(String top, String bottom) {
                this.top = top;
                this.bottom = bottom;
            }
        }

        static class Cell {
            int row;
            int column;
            final HashSet<Pattern> patterns;
            final Multimap<String, Pattern> patternsByLeft;

            Cell(int row, int column, ArrayList<Pattern> patterns) {
                this.row = row;
                this.column = column;
                this.patterns = new HashSet<>();
                this.patternsByLeft = new Multimap<>();
                for (Pattern pattern : patterns) {
                    this.patterns.add(pattern);
                    patternsByLeft.addValue(pattern.left, pattern);
                }
            }

        }
    }

    static class Multimap<K, V> extends HashMap<K, HashSet<V>> {

        void addValue(K key, V value) {
            HashSet<V> set = get(key);
            if (set == null) super.put(key, set = new HashSet<>());
            set.add(value);
        }

        void removeValue(K key, V value) {
            HashSet<V> set = get(key);
            if (set == null)
                return;
            set.remove(value);
            if (set.isEmpty())
                remove(key);
        }
    }

    static HashSet<String> intersect(Set<String> left, Set<String> right) {
        HashSet<String> common = new HashSet<>(left);
        common.retainAll(right);
        return common;
    }

    static HashSet<String> diff(Set<String> set, Set<String> remove) {
        HashSet<String> diff = new HashSet<>(set);
        diff.removeAll(remove);
        return diff;
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
