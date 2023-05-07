package org.tcaud.grid;

import org.tcaud.display.DisplayStrategy;
import org.tcaud.display.DisplayStrategyConsole;

import java.util.Arrays;

public class BineroGrid extends Grid {

    public BineroGrid(int[][] grid, DisplayStrategy displayStrategy) {
        checkDimensions(grid);
        this.grid = grid;
        this.displayStrategy = displayStrategy;
    }

    public BineroGrid(int[][] grid) {
        checkDimensions(grid);
        this.grid = grid;
        this.displayStrategy = new DisplayStrategyConsole();
    }

    private void checkDimensions(int[][] grid) {
        if (grid.length != grid[0].length) {
            throw new IllegalArgumentException("Grid must be square");
        }
        if (grid.length % 2 == 1) {
            throw new IllegalArgumentException("Grid must have an even number of rows and columns");
        }
    }

    @Override
    public boolean isValid() {
        var dimension = getDimension();

        for (int i = 0; i < dimension; i++) {
            if (!isRowValid(i)) {
                return false;
            }
            if (!isColumnValid(i)) {
                return false;
            }
        }

        return !existsTwoFulfilledRowsIdentical() && !existsTwoFulfilledColumnsIdentical();
    }

    @Override
    public int[] getPossibleValues() {
        return new int[]{0, 1};
    }

    @Override
    public GAME getGame() {
        return GAME.BINERO;
    }

    private boolean isRowValid(int row) {
        return getNumberOfOnRow(row, 0) <= getDimension() / 2
                && getNumberOfOnRow(row, 1) <= getDimension() / 2
                && !existsMoreThanTwoIdenticalValueInARow(row);
    }

    private boolean existsTwoFulfilledRowsIdentical() {
        for (int i = 0; i < getDimension(); i++) {
            for (int j = i + 1; j < getDimension(); j++) {
                if (Arrays.equals(grid[i], grid[j]) && Arrays.stream(grid[i]).noneMatch(cell -> cell == VALUE_EMPTY_CELL)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean existsTwoFulfilledColumnsIdentical() {
        for (int i = 0; i < getDimension(); i++) {
            for (int j = i + 1; j < getDimension(); j++) {
                if (Arrays.equals(getColumn(i), getColumn(j)) && Arrays.stream(getColumn(i)).noneMatch(cell -> cell == VALUE_EMPTY_CELL)) {
                    return true;
                }
            }
        }
        return false;
    }

    private int[] getColumn(int column) {
        return Arrays.stream(grid).mapToInt(row -> row[column]).toArray();
    }

    private int getNumberOfOnRow(int row, int value) {
        return Arrays.stream(grid[row]).filter(cell -> cell == value).toArray().length;
    }

    private boolean existsMoreThanTwoIdenticalValueInARow(int row) {
        var previousValue = grid[row][0];
        var count = 1;
        for (int i = 1; i < getDimension(); i++) {
            if (grid[row][i] == previousValue) {
                count++;
            } else {
                count = 1;
            }
            if (count > 2 && previousValue != VALUE_EMPTY_CELL) {
                return true;
            }
            previousValue = grid[row][i];
        }
        return false;
    }

    private boolean isColumnValid(int column) {
        return getNumberOfOnColumn(column, 0) <= getDimension() / 2
                && getNumberOfOnColumn(column, 1) <= getDimension() / 2
                && !existsMoreThanTwoIdenticalValueInAColumn(column);
    }

    private int getNumberOfOnColumn(int column, int value) {
        return Arrays.stream(grid).mapToInt(row -> row[column]).filter(cell -> cell == value).toArray().length;
    }

    private boolean existsMoreThanTwoIdenticalValueInAColumn(int column) {
        var previousValue = grid[0][column];
        var count = 1;
        for (int i = 1; i < getDimension(); i++) {
            if (grid[i][column] == previousValue) {
                count++;
            } else {
                count = 1;
            }
            if (count > 2 && previousValue != VALUE_EMPTY_CELL) {
                return true;
            }
            previousValue = grid[i][column];
        }
        return false;
    }
}
