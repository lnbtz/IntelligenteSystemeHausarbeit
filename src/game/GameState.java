package game;

import game.player.Player;

import java.util.Stack;

/**
 * Represents an instance of a game board.
 */
public class GameState {
    private final int numberOfRows, numberOfColumns;
    private final Player startingPlayer;
    private final Player[][] board;
    private int numOfPiecesPlayed = 0;
    private Player lastMovedPlayer;
    Stack<Integer> pastGameMoves = new Stack<>();
    private int lastMoveColumn;
    private int lastMoveRow;

    private GameState(int numberOfRows, int numberOfColumns, Player[][] board, Player startingPlayer) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.board = board;
        this.startingPlayer = startingPlayer;
    }

    public int getLastMoveColumn() {
        return lastMoveColumn;
    }

    /**
     * New Game Constructor
     */
    public GameState(int numberOfRows, int numberOfColumns, Player startingPlayer) {
        this(numberOfRows,
                numberOfColumns,
                new Player[numberOfRows][numberOfColumns],
                startingPlayer
        );
    }

    /**
     * Copy Constructor
     */
    public GameState(GameState gameState) {
        this(gameState.getNumberOfRows(),
                gameState.getNumberOfColumns(),
                gameState.getBoardCopy(),
                gameState.getStartingPlayer()
        );
    }

    public void setNumOfPiecesPlayed(int numOfPiecesPlayed) {
        this.numOfPiecesPlayed = numOfPiecesPlayed;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public Player getStartingPlayer() {
        return startingPlayer;
    }

    public Player getLastMovedPlayer() {
        return lastMovedPlayer;
    }

    /**
     * @return a deep copy of the game board
     */
    public Player[][] getBoardCopy() {
        Player[][] boardCopy = new Player[numberOfRows][numberOfColumns];
        for (int row = 0; row < numberOfRows; row++) {
            System.arraycopy(board[row], 0, boardCopy[row], 0, numberOfColumns);
        }
        return boardCopy;
    }

    public boolean isBoardFull() {
        return numOfPiecesPlayed >= numberOfRows * numberOfColumns;
    }

    public boolean isColumnNotFull(int column) {
        return board[0][column] == null;
    }

    /**
     * Inserts a piece into the specified column.
     *
     * @param column        The column to insert the piece into
     * @param currentPlayer The current player
     * @return The row that the new piece was inserted into
     */
    public int insertPiece(int column, Player currentPlayer) {

        // row is already full
        if (board[0][column] != null) {
            System.err.println("Can't insert a piece into a row that is full!");
            System.exit(-1);
        }

        int row = 1;

        // find first empty slot in the column
        while (row < numberOfRows && board[row][column] == null) {
            row++;
        }

        // insert the piece
        board[row - 1][column] = currentPlayer;

        //update status
        numOfPiecesPlayed++;
        this.lastMovedPlayer = currentPlayer;

        lastMoveRow = row - 1;
        lastMoveColumn = column;
        pastGameMoves.push(lastMoveRow);
        pastGameMoves.push(lastMoveColumn);

        return row - 1;
    }

    /**
     * Checks if the last move, made by the lastMovedPlayer led
     * to win.
     */
    public boolean checkPlayerWon() {
        if (lastMovedPlayer == null || numOfPiecesPlayed <= 0) {
            throw new IllegalStateException("No piece has been played yet.");
        }
        return checkForHorizontalLine(lastMoveRow, lastMovedPlayer) ||
                checkForVerticalLine(lastMoveColumn, lastMovedPlayer) ||
                checkForDiagonalLine(lastMoveRow, lastMoveColumn, lastMovedPlayer);
    }

    /**
     * Searches the specified row for a line of at least length 4 for the specified player.
     */
    private boolean checkForHorizontalLine(int row, Player player) {
        int lineLength = 0;

        for (int column = 0; column < numberOfColumns; column++) {
            if (board[row][column] == player) {
                lineLength++;
                if (lineLength >= 4)
                    return true;
            } else
                lineLength = 0;
        }

        return false;
    }

    /**
     * Searches the specified column for a line of at least length 4 for the specified player.
     */
    private boolean checkForVerticalLine(int column, Player player) {
        int lineLength = 0;

        for (int row = 0; row < numberOfRows; row++) {
            if (board[row][column] == player) {
                lineLength++;
                if (lineLength >= 4)
                    return true;
            } else
                lineLength = 0;
        }

        return false;
    }

    /**
     * Searches both diagonals of the given row and column for a line of at least length 4 for the specified player.
     */
    private boolean checkForDiagonalLine(int row, int column, Player player) {

        /* top left to bottom right */

        int r = row;
        int c = column;

        // find top left of diagonal
        while (r > 0 && c > 0) {
            r--;
            c--;
        }

        // search diagonal
        int lineLength = 0;
        for (; r < numberOfRows && c < numberOfColumns; r++, c++) {
            if (board[r][c] == player) {
                lineLength++;
                if (lineLength >= 4)
                    return true;
            } else
                lineLength = 0;
        }

        /* bottom left to top right */

        r = row;
        c = column;

        // find bottom left of diagonal
        while (r < numberOfRows - 1 && c > 0) {
            r++;
            c--;
        }

        // search diagonal
        lineLength = 0;
        for (; r >= 0 && c < numberOfColumns; r--, c++) {
            if (board[r][c] == player) {
                lineLength++;
                if (lineLength >= 4)
                    return true;
            } else
                lineLength = 0;
        }

        return false;
    }

    public Player[][] getBoard() {
        return board;
    }

    public void printBoard() {
        System.out.println();

        for (int i = 0; i < numberOfColumns; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int row = 0; row < numberOfRows; row++) {
            for (int column = 0; column < numberOfColumns; column++) {
                if (board[row][column] == GameConfig.PLAYER_A)
                    System.out.print("A ");
                else if (board[row][column] == GameConfig.PLAYER_B)
                    System.out.print("B ");
                else
                    System.out.print("_ ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
