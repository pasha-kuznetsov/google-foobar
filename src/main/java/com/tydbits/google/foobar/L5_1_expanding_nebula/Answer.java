package com.tydbits.google.foobar.L5_1_expanding_nebula;

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

            boolean restricted;
            do {
                restricted = false;
                for (int column = 1; column < columns.size(); ++column)
                    if (restrict(columns.get(column - 1), columns.get(column)))
                        restricted = true;
            } while (restricted);
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
                generateColumns(cell, cell.patterns);
                return columns;
            }

            private void generateColumns(Cell cell, HashSet<Pattern> patterns) {
                if (patterns == null) return;
                for (Pattern pattern : patterns)
                    generateColumns(cell, pattern);
            }

            private void generateColumns(Cell cell, Pattern pattern) {
                if (!isFirstColumn(cell) && !leftFrom(cell).validRight(pattern)) return;
                if (!isLastColumn(cell) && !rightFrom(cell).validLeft(pattern)) return;
                setLeft(cell.row, pattern.left);
                setRight(cell.row, pattern.right);
                if (isLastRow(cell)) {
                    columns.add(newColumn());
                }
                else {
                    Cell next = nextRow(cell);
                    generateColumns(next, next.topPatterns.get(pattern.bottom));
                }
            }

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

        private boolean restrict(Columns left, Columns right) {
            HashSet<Integer> common = intersect(left.right.keySet(), right.left.keySet());
            if (common.size() == left.right.size() && common.size() == right.left.size())
                return false;
            left.removeRight(diff(left.right.keySet(), common));
            right.removeLeft(diff(right.left.keySet(), common));
            return true;
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

            void removeLeft(Set<Integer> remove) {
                for (Integer key : remove) {
                    for (Column c : left.get(key)) {
                        right.removeValue(c.right, c);
                    }
                }
                left.keySet().removeAll(remove);
            }

            void removeRight(Set<Integer> remove) {
                for (Integer key : remove) {
                    for (Column c : right.get(key)) {
                        left.removeValue(c.left, c);
                    }
                }
                right.keySet().removeAll(remove);
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
            final HashSet<Pattern> patterns;
            final Multimap<Integer, Pattern> topPatterns;
            final Multimap<Integer, Pattern> leftPatterns;
            final Multimap<Integer, Pattern> rightPatterns;

            Cell(int row, int column, ArrayList<Pattern> patterns) {
                this.row = row;
                this.column = column;
                this.patterns = new HashSet<>();
                this.topPatterns = new Multimap<>();
                this.leftPatterns = new Multimap<>();
                this.rightPatterns = new Multimap<>();
                for (Pattern pattern : patterns) {
                    this.patterns.add(pattern);
                    topPatterns.addValue(pattern.top, pattern);
                    leftPatterns.addValue(pattern.left, pattern);
                    rightPatterns.addValue(pattern.right, pattern);
                }
            }

            boolean validLeft(Pattern pattern) {
                return leftPatterns.containsKey(pattern.right);
            }

            boolean validRight(Pattern pattern) {
                return rightPatterns.containsKey(pattern.left);
            }
        }

        private boolean isFirstColumn(Cell cell) { return cell.column <= 0; }
        private boolean isLastColumn(Cell cell) { return cell.column + 1 >= cells[cell.row].length; }
        private boolean isLastRow(Cell cell) { return cell.row >= cells.length - 1; }
        private Cell leftFrom(Cell cell) { return cells[cell.row][cell.column - 1]; }
        private Cell rightFrom(Cell cell) { return cells[cell.row][cell.column + 1]; }
        private Cell nextRow(Cell cell) { return cells[cell.row + 1][cell.column]; }
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

    static <T> HashSet<T> intersect(Set<T> left, Set<T> right) {
        HashSet<T> common = new HashSet<>(left);
        common.retainAll(right);
        return common;
    }

    static <T> HashSet<T> diff(Set<T> set, Set<T> remove) {
        HashSet<T> diff = new HashSet<>(set);
        diff.removeAll(remove);
        return diff;
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

        @Override
        public String toString() {
            return ((top & 0b10) == 0 ? "." : "o") + ((top & 0b01) == 0 ? '.' : 'o') + ' ' +
                    ((bottom & 0b10) == 0 ? "." : "o") + ((bottom & 0b01) == 0 ? '.' : 'o');
        }
    }
}
