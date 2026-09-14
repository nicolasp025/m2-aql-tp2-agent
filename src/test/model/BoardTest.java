package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    // --- initial state ---

    @Test
    void newBoardStartsInProgressWithXFirstAndNoWinner() {
        assertTrue(board.isInProgressMode());
        assertFalse(board.isInFinishedMode());
        assertEquals(Player.X, board.getCurrentTurn());
        assertNull(board.getWinner());
    }

    // --- basic marking / turn alternation ---

    @Test
    void markingAValidCellFlipsTheTurn() {
        board.mark(0, 0);

        assertEquals(Player.O, board.getCurrentTurn());
        assertNull(board.getWinner());
        assertTrue(board.isInProgressMode());
    }

    @Test
    void turnKeepsAlternatingAcrossSeveralMoves() {
        board.mark(0, 0); // X
        board.mark(0, 1); // O
        board.mark(1, 1); // X

        assertEquals(Player.O, board.getCurrentTurn());
    }

    @Test
    void setCurrentTurnOverridesWhoseTurnItIs() {
        board.setCurrentTurn(Player.O);

        assertEquals(Player.O, board.getCurrentTurn());
    }

    // --- invalid moves are no-ops ---

    @Test
    void markingOutOfBoundsRowOrColumnIsIgnored() {
        board.mark(-1, 0);
        board.mark(3, 0);
        board.mark(0, -1);
        board.mark(0, 3);

        assertEquals(Player.X, board.getCurrentTurn());
        assertTrue(board.isInProgressMode());
    }

    @Test
    void markingAnAlreadyPlayedCellIsIgnored() {
        board.mark(0, 0); // X plays (0,0), turn becomes O

        board.mark(0, 0); // O tries the same cell -> no-op

        assertEquals(Player.O, board.getCurrentTurn());
    }

    @Test
    void markingAfterGameIsFinishedIsIgnored() {
        // X wins the top row
        board.mark(0, 0); // X
        board.mark(1, 0); // O
        board.mark(0, 1); // X
        board.mark(1, 1); // O
        board.mark(0, 2); // X wins

        assertTrue(board.isInFinishedMode());

        // further moves must not change winner/turn nor crash
        board.mark(2, 2);

        assertEquals(Player.X, board.getWinner());
        assertEquals(Player.X, board.getCurrentTurn());
        assertTrue(board.isInFinishedMode());
    }

    // --- winning combinations ---

    @Test
    void detectsRowWin() {
        board.mark(0, 0); // X
        board.mark(1, 0); // O
        board.mark(0, 1); // X
        board.mark(1, 1); // O
        board.mark(0, 2); // X completes row 0

        assertTrue(board.isInFinishedMode());
        assertEquals(Player.X, board.getWinner());
    }

    @Test
    void detectsColumnWin() {
        board.mark(0, 0); // X
        board.mark(0, 1); // O
        board.mark(1, 0); // X
        board.mark(1, 1); // O
        board.mark(2, 0); // X completes column 0

        assertTrue(board.isInFinishedMode());
        assertEquals(Player.X, board.getWinner());
    }

    @Test
    void detectsMainDiagonalWin() {
        board.mark(0, 0); // X
        board.mark(1, 0); // O
        board.mark(1, 1); // X
        board.mark(2, 0); // O
        board.mark(2, 2); // X completes diagonal (0,0)-(1,1)-(2,2)

        assertTrue(board.isInFinishedMode());
        assertEquals(Player.X, board.getWinner());
    }

    @Test
    void detectsAntiDiagonalWin() {
        board.mark(0, 2); // X
        board.mark(0, 0); // O
        board.mark(1, 1); // X
        board.mark(0, 1); // O
        board.mark(2, 0); // X completes diagonal (0,2)-(1,1)-(2,0)

        assertTrue(board.isInFinishedMode());
        assertEquals(Player.X, board.getWinner());
    }

    // --- draw: this implementation has no explicit draw detection ---

    @Test
    void fullBoardWithNoLineLeavesGameInProgressWithNoWinner() {
        // X O X
        // X O O
        // O X X
        board.mark(0, 0); // X
        board.mark(0, 1); // O
        board.mark(0, 2); // X
        board.mark(1, 1); // O
        board.mark(1, 0); // X
        board.mark(1, 2); // O
        board.mark(2, 1); // X
        board.mark(2, 0); // O
        board.mark(2, 2); // X

        assertNull(board.getWinner());
        assertTrue(board.isInProgressMode());
        assertFalse(board.isInFinishedMode());
    }

    // --- restart ---

    @Test
    void restartResetsTurnWinnerAndState() {
        board.mark(0, 0);
        board.mark(1, 0);
        board.mark(0, 1);
        board.mark(1, 1);
        board.mark(0, 2); // X wins

        board.restart();

        assertEquals(Player.X, board.getCurrentTurn());
        assertNull(board.getWinner());
        assertTrue(board.isInProgressMode());
    }

    @Test
    void restartClearsThePreviouslyPlayedCells() {
        board.mark(0, 0); // X plays (0,0)

        board.restart();

        // (0,0) must be free again: X can play it and the turn must flip to O
        board.mark(0, 0);
        assertEquals(Player.O, board.getCurrentTurn());
    }
}
