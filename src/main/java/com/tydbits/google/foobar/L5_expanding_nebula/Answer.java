package com.tydbits.google.foobar.L5_expanding_nebula;

import java.util.ArrayList;
import java.util.HashMap;

public class Answer {
    public static int answer(boolean[][] g) {
        return (int) new Query(g).count();
    }

    static class Query {
        private final boolean[][] g;
        private final ArrayList<Cell> gasCells;
        private final ArrayList<Cell> emptyCells;
        private ArrayList<HashMap<Long, Long>> counts;

        Query(boolean[][] g) {
            this.g = g;
            this.gasCells = new ArrayList<>();
            this.emptyCells = new ArrayList<>();
            for (int top : new int[] {0b00, 0b01, 0b10, 0b11}) {
                for (int bottom : new int[] {0b00, 0b01, 0b10, 0b11}) {
                    // empty cell -> 1 non-empty predecessor
                    // non-empty -> other number of non-empty predecessors
                    Cell cell = new Cell(top, bottom);
                    (cell.count == 1 ? gasCells : emptyCells).add(cell);
                }
            }

            this.counts = new ArrayList<>();
            for (int column = 0; column < g[0].length; ++column)
                counts.add(new HashMap<Long, Long>());
        }

        long count() {
            return countColumns(0, null);
        }

        private long countColumns(int index, Cell[] left) {
            Long key = left == null ? -1 : nextColumnKey(left);
            Long count = counts.get(index).get(key);
            if (count == null) {
                count = countColumns(0, index, new Cell[g.length], left);
                counts.get(index).put(key, count);
            }
            return count;
        }

        private long nextColumnKey(Cell[] cells) {
            long mask = 0;
            for (int i = 0; i < cells.length; ++i)
                mask |= cells[i].right << (i * 2);
            return mask;
        }

        private long countColumns(int row, int column, Cell[] cells, Cell[] left) {
            long count = 0;
            for (Cell cell : (g[row][column] ? gasCells : emptyCells)) {
                if (left != null && cell.left != left[row].right)
                    continue;
                if (row > 0 && cell.top != cells[row - 1].bottom)
                    continue;
                cells[row] = cell;
                if (isLastRow(row)) {
                    if (isLastColumn(column))
                        count += 1;
                    else
                        count += countColumns(column + 1, cells);
                }
                else {
                    count += countColumns(row + 1, column, cells, left);
                }
            }
            return count;
        }

        private boolean isLastRow(int row) { return row >= g.length - 1; }
        private boolean isLastColumn(int column) { return column >= g[0].length - 1; }
    }

    static class Cell {
        int top;
        int bottom;
        int left;
        int right;
        int count;

        Cell(int top, int bottom) {
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
