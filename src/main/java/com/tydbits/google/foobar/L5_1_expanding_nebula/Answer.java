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
        private final ArrayList<Columns> columns;
        private int terminal;

        Query(boolean[][] g) {
            this.gasPatterns = new ArrayList<>();
            this.emptyPatterns = new ArrayList<>();
            for (String top : new String[] {"..", ".o", "o.", "oo"}) {
                for (String bottom : new String[] {"..", ".o", "o.", "oo"}) {
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
            for (int column = 0; column < cells[0].length; ++column)
                columns.add(generateColumns(new Columns(column), "", "", null, cells[0][column]));

            boolean restricted;
            do {
                restricted = false;
                for (int column = 1; column < columns.size(); ++column)
                    if (restrict(columns.get(column - 1), columns.get(column)))
                        restricted = true;
            } while (restricted);
        }

        private Columns generateColumns(Columns columns, String left, String right, Pattern top, Cell cell) {
            HashSet<Pattern> patterns = top == null ? cell.patterns : cell.topPatterns.get(top.bottom);
            if (patterns != null) {
                for (Pattern pattern : patterns) {
                    if (cell.column > 0) {
                        Cell prev = cells[cell.row][cell.column - 1];
                        if (!prev.rightPatterns.containsKey(pattern.left))
                            continue;
                    }
                    if (cell.column + 1 < cells[cell.row].length) {
                        Cell next = cells[cell.row][cell.column + 1];
                        if (!next.leftPatterns.containsKey(pattern.right))
                            continue;
                    }
                    generateColumns(columns, left, right, cell, pattern);
                }
            }
            return columns;
        }

        private void generateColumns(Columns columns, String left, String right, Cell cell, Pattern pattern) {
            if (cell.row == cells.length - 1)
                columns.add(new Column(left + pattern.left, right + pattern.right));
            else
                generateColumns(columns,
                        left + pattern.left.charAt(0),
                        right + pattern.right.charAt(0),
                        pattern,
                        cells[cell.row + 1][cell.column]);
        }

        private boolean restrict(Columns left, Columns right) {
            HashSet<String> common = intersect(left.right.keySet(), right.left.keySet());
            if (common.size() == left.right.size() && common.size() == right.left.size())
                return false;
            left.removeRight(diff(left.right.keySet(), common));
            right.removeLeft(diff(right.left.keySet(), common));
            return true;
        }

        int count() {
            this.terminal = 0;
            countColumns(columns.get(0));
            return terminal;
        }

        private void countColumns(Columns columns) {
            for (HashSet<Column> right : columns.right.values()) {
                for (Column c : right) {
                    countColumns(columns, c);
                }
            }
        }

        private void countColumns(Columns c, Column column) {
            if (c.column == columns.size() - 1)
                terminal += 1;
            else
                countColumns(columns.get(c.column + 1), column.right);
        }

        private void countColumns(Columns c, String left) {
            HashSet<Column> set = c.left.get(left);
            if (set == null)
                return;
            for (Column column : set)
                countColumns(c, column);
        }

        static class Columns {
            private final int column;
            final Multimap<String, Column> left;
            final Multimap<String, Column> right;

            Columns(int row) {
                this.column = row;
                this.left = new Multimap<>();
                this.right = new Multimap<>();
            }

            void add(Column row) {
                left.addValue(row.left, row);
                right.addValue(row.right, row);
            }

            void removeLeft(Set<String> remove) {
                for (String key : remove) {
                    for (Column c : left.get(key)) {
                        right.removeValue(c.right, c);
                    }
                }
                left.keySet().removeAll(remove);
            }

            void removeRight(Set<String> remove) {
                for (String key : remove) {
                    for (Column c : right.get(key)) {
                        left.removeValue(c.left, c);
                    }
                }
                right.keySet().removeAll(remove);
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
            final HashSet<Pattern> patterns;
            final Multimap<String, Pattern> topPatterns;
            final Multimap<String, Pattern> bottomPatterns;
            final Multimap<String, Pattern> leftPatterns;
            final Multimap<String, Pattern> rightPatterns;

            Cell(int row, int column, ArrayList<Pattern> patterns) {
                this.row = row;
                this.column = column;
                this.patterns = new HashSet<>();
                this.topPatterns = new Multimap<>();
                this.bottomPatterns = new Multimap<>();
                this.leftPatterns = new Multimap<>();
                this.rightPatterns = new Multimap<>();
                for (Pattern pattern : patterns) {
                    this.patterns.add(pattern);
                    topPatterns.addValue(pattern.top, pattern);
                    bottomPatterns.addValue(pattern.bottom, pattern);
                    leftPatterns.addValue(pattern.left, pattern);
                    rightPatterns.addValue(pattern.right, pattern);
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
