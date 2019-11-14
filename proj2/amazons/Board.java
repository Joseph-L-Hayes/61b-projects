package amazons;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.Collections;

import static amazons.Move.mv;
import static amazons.Piece.*;


/**
 * The state of an Amazons Game.
 *
 * @author JosephHayes
 */
class Board {

    /**
     * The number of squares on a side of the board.
     */
    static final int SIZE = 10;

    /**
     * Initializes a game board with SIZE squares on a side in the initial
     * position.
     */
    Board() {
        init();
    }

    /**
     * Initializes a copy of MODEL.
     */
    Board(Board model) {
        copy(model);
    }

    /**
     * Copies MODEL into me.
     */
    void copy(Board model) {

        this._turn = model.turn();
        this._gameBoard = new HashMap<Square, Piece>(model._gameBoard);
        this._gameBoard.putAll(model._gameBoard);
        this._moves = model._moves;
        this._winner = model.winner();
        this._numMoves = model.numMoves();
    }

    /**
     * Clears the board to the initial position.
     */
    void init() {
        _gameBoard = new HashMap<>();

        buildBoard(IteratorTests.REACHABLEFROMINITBOARD);

        _turn = WHITE;
        _winner = null;
        _moves = new Stack<>();
        _numMoves = _moves.size();
    }
    /** Returns the size of this board. */

    int boardSize() {
        return _gameBoard.size();
    }

    /** Builds board according to 2D array TARGET. Taken from IteratorTests.
     * @author 61b course staff */
    private void buildBoard(Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - row - 1][col];
                put(piece, Square.sq(col, row));
            }
        }
    }

    /**
     * Return the Piece whose move it is (WHITE or BLACK).
     */
    Piece turn() {
        return _turn;
    }

    /**
     * Return the number of moves (that have not been undone) for this
     * board.
     */
    int numMoves() {
        return _moves.size();
    }

    /** Returns true if this square is surrounded by pieces, false otherwise.
     * Square must contain a piece.
     * @param s a legal square. */
    boolean checkSurround(Square s) {
        if (get(s) == EMPTY) {
            return false;
        } else {

            for (int d = 0; d < 8; d++) {
                Piece nextTo = get(s.queenMove(d, 1));

                if (nextTo == EMPTY) {
                    return false;
                }
            }
            return true;
        }


    }
    /**
     * Return the winner in the current position, or null if the game is
     * not yet finished.
     */
    Piece winner() {

        if (_winner == null) {
            Iterator<Move> amazonWin = legalMoves(turn().opponent());
            int numMoves = 0;

            while (amazonWin.hasNext()) {
                Move myMove = amazonWin.next();
                numMoves++;
            }

            if (numMoves == 0) {
                _winner = turn();
                return _winner;
            }
        }
        return _winner;
    }

    /**
     * Return the contents the square at S.
     */
    final Piece get(Square s) {
        return _gameBoard.get(s);
    }

    /**
     * Return the contents of the square at (COL, ROW), where
     * 0 <= COL, ROW <= 9.
     */
    final Piece get(int col, int row) {
        return _gameBoard.get(Square.sq(col, row));
    }

    /**
     * Return the contents of the square at COL ROW.
     */
    final Piece get(char col, char row) {

        return get(col - 'a', row - '1');
    }

    /**
     * Set square S to P.
     */
    final void put(Piece p, Square s) {
        _gameBoard.put(s, p);
    }

    /**
     * Set square (COL, ROW) to P.
     */
    final void put(Piece p, int col, int row) {
        _gameBoard.put(Square.sq(col, row), p);
        _winner = null;
    }

    /**
     * Set square COL ROW to P.
     */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /**
     * Return true iff FROM - TO is an unblocked queen move on the current
     * board, ignoring the contents of ASEMPTY, if it is encountered.
     * For this to be true, FROM-TO must be a queen move and the
     * squares along it, other than FROM and ASEMPTY, must be
     * empty. ASEMPTY may be null, in which case it has no effect.
     */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        if (get(to) == null || !from.isQueenMove(to) || get(asEmpty) == SPEAR) {
            return false;
        }

        int direction = from.direction(to);

        int steps = 0;
        if (from.col() == to.col()) {
            steps = Math.abs(from.row() - to.row());
        } else if (from.row() == to.row()) {
            steps = Math.abs(from.col() - to.col());
        } else {
            int dx = Math.abs(from.col() - to.col());
            int dy = Math.abs(from.row() - to.row());
            steps = Math.min(dx, dy);
        }

        for (int i = 1; i <= steps; i++) {
            Square visitor = from.queenMove(direction, i);

            if (get(visitor) != EMPTY && !visitor.equals(asEmpty)) {
                return false;
            } else if (visitor == to) {
                return true;
            }
        }
        return true;
    }

    /**
     * Return true iff FROM is a valid starting square for a move.
     */
    boolean isLegal(Square from) {

        return get(from).equals(turn());
    }

    /**
     * Return true iff FROM-TO is a valid first part of move, ignoring
     * spear throwing.
     */
    boolean isLegal(Square from, Square to) {

        return isLegal(from) && isUnblockedMove(from, to, null);
    }

    /**
     * Return true iff FROM-TO(SPEAR) is a legal move in the current
     * position.
     */
    boolean isLegal(Square from, Square to, Square spear) {
        if (spear == null) {
            return isLegal(from, to);
        }
        return isLegal(from, to) && isUnblockedMove(to, spear, from);
    }

    /**
     * Return true iff MOVE is a legal move in the current
     * position.
     */
    boolean isLegal(Move move) {
        if (move == null) {
            return false;
        } else {
            return isLegal(move.from(), move.to(), move.spear());
        }
    }

    /**
     * Move FROM-TO(SPEAR), assuming this is a legal move.
     */
    void makeMove(Square from, Square to, Square spear) {

        if (isLegal(from, to, spear)) {

            _moves.push(mv(from, to, spear));
            put(get(from), to);
            put(EMPTY, from);
            put(SPEAR, spear);
            _turn = turn().opponent();
        }
    }

    /**
     * Move according to MOVE, assuming it is a legal move.
     */
    void makeMove(Move move) {
        if (isLegal(move)) {
            makeMove(move.from(), move.to(), move.spear());
        }
    }

    /**
     * Undo one move. Has no effect on the initial board.
     */
    void undo() {
        if (_moves.size() > 0) {
            Move undoMove = _moves.pop();
            Piece toUndo = get(undoMove.to());

            put(EMPTY, undoMove.spear());
            put(EMPTY, undoMove.to());
            put(toUndo, undoMove.from());
            _turn = get(undoMove.from()).opponent();
        }
    }

    /**
     * Return an Iterator over the Squares that are reachable by an
     * unblocked queen move from FROM. Does not pay attention to what
     * piece (if any) is on FROM, nor to whether the game is finished.
     * Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     * feature is useful when looking for Moves, because after moving a
     * piece, one wants to treat the Square it came from as empty for
     * purposes of spear throwing.)
     */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /**
     * Return an Iterator over all legal moves on the current board.
     */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /**
     * Return an Iterator over all legal moves on the current board for
     * SIDE (regardless of whose turn it is).
     */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /**
     * An iterator used by reachableFrom.
     */
    private class ReachableFromIterator implements Iterator<Square> {

        /**
         * Iterator of all squares reachable by queen move from FROM,
         * treating ASEMPTY as empty.
         */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = 0;
            _steps = 0;
            _asEmpty = asEmpty;
            toNext();
        }

        @Override
        public boolean hasNext() {
            if (_from == null && _next == null) {
                return false;
            }
            return _dir < 8;
        }

        @Override
        public Square next() {

            _last = _next;
            toNext();
            return _last;
        }

        /**
         * Advance _dir and _steps, so that the next VALID Square is
         * _steps steps in direction _dir from _from.
         */
        private void toNext() {
            _steps++;

            _next = _from.queenMove(_dir, _steps);

            while (_next == null || !isUnblockedMove(_from, _next, _asEmpty)
                    && hasNext()) {
                _dir++;
                _steps = 1;

                if (!hasNext()) {
                    break;
                }
                _next = _from.queenMove(_dir, _steps);
            }
        }
        /** The last square from this iterator. */
        private Square _last;
        /**
         * Starting square.
         */
        private Square _from;
        /**
         * Current direction.
         */
        private int _dir;
        /**
         * Current distance.
         */
        private int _steps;
        /**
         * Square treated as empty.
         */
        private Square _asEmpty;
        /** The next square for this iterator. */
        private Square _next;
    }

    /**
     * An iterator used by legalMoves.
     */
    private class LegalMoveIterator implements Iterator<Move> {

        /**
         * All legal moves for SIDE (WHITE or BLACK).
         */
        LegalMoveIterator(Piece side) {

            _fromPiece = side;
            _startingSquares = Square.iterator();
            _pieceMoves = NO_SQUARES;
            _spearThrows = NO_SQUARES;

            toNext();
        }

        @Override
        public boolean hasNext() {

            return _spearThrows.hasNext();
        }

        @Override
        public Move next() {

            Move last = Move.mv(_start, _nextSquare, _spearThrows.next());
            toNext();

            return last;
        }

        /**
         * Advance so that the next valid Move is
         * _start-_nextSquare(sp), where sp is the next value of
         * _spearThrows.
         */
        private void toNext() {
            while (true) {
                if (_spearThrows.hasNext()) {
                    break;
                } else if (_pieceMoves.hasNext()) {
                    _nextSquare = _pieceMoves.next();
                    _spearThrows = reachableFrom(_nextSquare, _start);
                } else if (_startingSquares.hasNext()) {
                    _start = _startingSquares.next();

                    if (get(_start) == _fromPiece) {
                        _pieceMoves = reachableFrom(_start, null);
                    }
                } else {
                    break;
                }
            }
        }

        /**
         * Color of side whose moves we are iterating.
         */
        private Piece _fromPiece;

        /**
         * Current spear.
         */
        private Square _spear;

        /**
         * Current starting square.
         */
        private Square _start;

        /**
         * Current piece's new position.
         */
        private Square _nextSquare;

        /**
         * Remaining starting squares to consider.
         */
        private Iterator<Square> _startingSquares;

        /**
         * Remaining moves from _start to consider.
         */
        private Iterator<Square> _pieceMoves;
        /**
         * Remaining spear throws from _piece to consider.
         */
        private Iterator<Square> _spearThrows;
    }

    @Override
    public String toString() {
        String boardString = "";

        for (int row = SIZE - 1; row >= 0; row--) {
            boardString = boardString + "   ";

            for (int col = 0; col < SIZE; col++) {
                Square position = Square.sq(col, row);

                if (!_gameBoard.containsKey(position) && col == 9) {
                    boardString += EMPTY;
                    boardString += "\n";
                } else if (!_gameBoard.containsKey(position)) {
                    boardString += EMPTY + " ";
                } else if (col == 9) {
                    boardString += get(col, row).toString();
                    boardString += "\n";
                } else {
                    boardString += get(col, row).toString() + " ";
                }

            }
        }
        return boardString;
    }


    /**
     * An empty iterator for initialization.
     */
    private static final Iterator<Square> NO_SQUARES =
            Collections.emptyIterator();

    /**
     * Piece whose turn it is (BLACK or WHITE).
     */
    private Piece _turn;
    /**
     * Cached value of winner on this board, or EMPTY if it has not been
     * computed.
     */
    private Piece _winner;
    /** A hashmap of all the squares for this board and their contents. */
    private HashMap<Square, Piece> _gameBoard;
    /** A stack list of all the moves performed on this board. */
    private Stack<Move> _moves;
    /** The number of moves performed on this board. */
    private int _numMoves;
}
