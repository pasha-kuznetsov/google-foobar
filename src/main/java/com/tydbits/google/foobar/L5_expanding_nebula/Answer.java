package com.tydbits.google.foobar.L5_expanding_nebula;

import java.util.*;

public class Answer {
    public static int answer(boolean[][] g) {
        return new Query(g).count();
    }

    static class Query {
        private final Cell[][] cells;
        private final ArrayList<Columns> columns;

        Query(boolean[][] g) {
            ArrayList<Pattern> gasPatterns = new ArrayList<>();
            ArrayList<Pattern> emptyPatterns = new ArrayList<>();
            for (int top : new int[] {0b00, 0b01, 0b10, 0b11}) {
                for (int bottom : new int[] {0b00, 0b01, 0b10, 0b11}) {
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
                columns.add(new ColumnBuilder(column).generateColumns());
        }

        int count() {
            return countColumns(columns.get(0));
        }

        private int countColumns(Columns columns) {
            int count = 0;
            for (HashSet<Column> right : columns.right.values()) {
                for (Column c : right) {
                    count += countColumns(columns, c);
                }
            }
            return count;
        }

        private int countColumns(Columns c, Column column) {
            if (column.count >= 0)
                return column.count;
            column.count = c.index == columns.size() - 1
                    ? 1
                    : countColumns(columns.get(c.index + 1), column.right);
            return column.count;
        }

        private int countColumns(Columns c, int left) {
            HashSet<Column> set = c.left.get(left);
            if (set == null)
                return 0;
            int count = 0;
            for (Column column : set)
                count += countColumns(c, column);
            return count;
        }

        class ColumnBuilder {
            private final Columns columns;
            private int left;
            private int right;

            ColumnBuilder(int index) {
                this.columns = new Columns(index);
            }

            Columns generateColumns() {
                Cell cell = cells[0][columns.index];
                for (Pattern pattern : cell.patterns)
                    generateColumns(cell, pattern);
                return columns;
            }

            private void generateColumns(Cell cell, Pattern pattern) {
                setLeft(cell.row, pattern.left);
                setRight(cell.row, pattern.right);
                if (isLastRow(cell)) {
                    columns.add(newColumn());
                }
                else {
                    Cell next = nextRow(cell);
                    for (Pattern nextPattern : next.patterns) {
                        if (nextPattern.top == pattern.bottom)
                            generateColumns(next, nextPattern);
                    }
                }
            }

            private boolean isLastRow(Cell cell) { return cell.row >= cells.length - 1; }

            private Cell nextRow(Cell cell) { return cells[cell.row + 1][cell.column]; }

            void setLeft(int row, int value) {
                left &= ~(0b11 << (row * 2));
                left |= value << (row * 2);
            }

            void setRight(int row, int value) {
                right &= ~(0b11 << (row * 2));
                right |= value << (row * 2);
            }

            private Column newColumn() {
                return new Column(left, right);
            }
        }

        static class Columns {
            private final int index;
            final Multimap<Integer, Column> left;
            final Multimap<Integer, Column> right;

            Columns(int column) {
                this.index = column;
                this.left = new Multimap<>();
                this.right = new Multimap<>();
            }

            void add(Column column) {
                left.addValue(column.left, column);
                right.addValue(column.right, column);
            }
        }

        static class Column {
            int left;
            int right;
            int count = -1;

            Column(int left, int right) {
                this.left = left;
                this.right = right;
            }
        }

        static class Cell {
            int row;
            int column;
            final ArrayList<Pattern> patterns;

            Cell(int row, int column, ArrayList<Pattern> patterns) {
                this.row = row;
                this.column = column;
                this.patterns = patterns;
            }
        }
    }

    static class Pattern {
        int top;
        int bottom;
        int left;
        int right;
        int count;

        Pattern(int top, int bottom) {
            this.top = top;
            this.bottom = bottom;
            this.left = ((top & 0b10)) | ((bottom & 0b10) >> 1);
            this.right = ((top & 0b01) << 1) | ((bottom & 0b01));
            this.count = (top & 0b01) + ((top & 0b10) >> 1) + (bottom & 0b01) + ((bottom & 0b10) >> 1);
        }
    }

    static class Multimap<K, V> extends HashMap<K, HashSet<V>> {
        void addValue(K key, V value) {
            HashSet<V> set = get(key);
            if (set == null) super.put(key, set = new HashSet<>());
            set.add(value);
        }
    }
}
