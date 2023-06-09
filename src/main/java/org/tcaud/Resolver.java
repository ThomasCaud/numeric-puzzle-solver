package org.tcaud;

import org.tcaud.display.DisplayStrategy;
import org.tcaud.grid.Board;
import org.tcaud.grid.GameName;
import org.tcaud.gridvalidator.GridValidator;
import org.tcaud.gridvalidator.GridValidatorBinero;
import org.tcaud.gridvalidator.GridValidatorSudoku;

import java.util.List;

public class Resolver {
    private static final List<GridValidator> gridValidatorList = List.of(
            new GridValidatorBinero(),
            new GridValidatorSudoku()
    );

    public static boolean resolve(GameName gameName, Board board, DisplayStrategy displayStrategy) {
        var gridValidator = getGridValidator(gameName);
        return resolve(gridValidator, board, new Cell(0, 0), displayStrategy);
    }

    private static GridValidator getGridValidator(GameName gameName) {
        var optionalGridValidator = gridValidatorList.stream().filter(maybeGridValidator -> maybeGridValidator.isEligible(gameName)).findFirst();
        if (optionalGridValidator.isEmpty()) {
            throw new IllegalStateException("No eligible GridValidator found for " + gameName);
        }
        return optionalGridValidator.get();
    }

    private static boolean resolve(GridValidator gridValidator, Board board, Cell cell, DisplayStrategy displayStrategy) {
        board.display(displayStrategy);

        if (cell.row() >= board.getDimension()) {
            return true;
        }

        var nextCell = getNextCell(board, cell.row(), cell.col());

        if (board.isFulfilled(cell.row(), cell.col())) {
            return resolve(gridValidator, board, nextCell, displayStrategy);
        }

        int[] possibleValues = gridValidator.getPossibleValues();
        for (int possibleValue : possibleValues) {
            board.updateCell(cell.row(), cell.col(), possibleValue);
            if (gridValidator.isValid(board) && resolve(gridValidator, board, nextCell, displayStrategy)) {
                return true;
            }
        }

        board.reinitializeCell(cell.row(), cell.col());
        return false;
    }

    private static Cell getNextCell(Board board, int row, int col) {
        if (col == board.getDimension() - 1) {
            return new Cell(row + 1, 0);
        } else {
            return new Cell(row, col + 1);
        }
    }
}
