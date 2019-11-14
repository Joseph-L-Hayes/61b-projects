package amazons;

import java.util.Iterator;
import static amazons.Piece.*;

/** A Player that automatically generates moves.
 * @author JosephHayes
 */
class AI extends Player {

    /** A position magnitude indicating a win (for white if positive, black
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A factor used to calculate maxDepth. */
    private static final int DEPTHFACTOR = 20;

    /** A new AI with no piece or controller (intended to produce
     *  a template). */
    AI() {
        this(null, null);
    }

    /** A new AI playing PIECE under control of CONTROLLER. */
    AI(Piece piece, Controller controller) {
        super(piece, controller);
    }

    @Override
    Player create(Piece piece, Controller controller) {
        return new AI(piece, controller);
    }

    @Override
    String myMove() {
        Move move = findMove();
        _controller.reportMove(move);

        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(board());
        if (_myPiece == WHITE) {
            findMove(b, maxDepth(b), true, 1, -INFTY, INFTY);
        } else {
            findMove(b, maxDepth(b), true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to one of the ...FindMove methods
     *  below. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _lastFoundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _lastMoveFound. */
    private int findMove(Board board, int depth, boolean saveMove, 
      int sense, int alpha, int beta) {

        if (depth == 0 || board.winner() != null) {

            return staticScore(board);
        }
        if (sense == -1) {
            Iterator<Move> playMin = board.legalMoves();
            Move currentMove;
            int moveValue = 0;
            int minimumValue = INFTY;

            while (playMin.hasNext()) {

                currentMove = playMin.next();

                board.makeMove(currentMove);

                moveValue = findMove(board, depth - 1, false, 1, alpha, beta);
                board.undo();

                if ((moveValue < minimumValue) && saveMove) {
                    _lastFoundMove = currentMove;
                    minimumValue = moveValue;
                }

                beta = Math.min(beta, moveValue);

                if (alpha >= beta) {
                    break;
                }
            }
            return minimumValue;

        } else {
            Iterator<Move> playMax = board.legalMoves();
            Move currentMove;
            int moveValue;
            int maximumValue = -INFTY;

            while (playMax.hasNext()) {

                currentMove = playMax.next();

                board.makeMove(currentMove);

                moveValue = findMove(board, depth - 1, false, -1, alpha, beta);
                board.undo();

                if ((moveValue > maximumValue) && saveMove) {
                    _lastFoundMove = currentMove;
                }

                maximumValue = Math.max(moveValue, maximumValue);
                if (beta <= maximumValue) {
                    return maximumValue;
                }
            }
            return maximumValue;
        }
    }

    /** Return a heuristically determined maximum search depth
     *  based on characteristics of BOARD. */
    private int maxDepth(Board board) {
        int N = board.numMoves();
        return N / DEPTHFACTOR + 1;
    }

    /** Return a heuristic value for BOARD. */
    private int staticScore(Board board) {
        Piece winner = board.winner();
        int whiteMoves = 0;
        int blackMoves = 0;

        if (winner == BLACK) {
            return -WINNING_VALUE;
        } else if (winner == WHITE) {
            return WINNING_VALUE;
        } else {

            Iterator white = board.legalMoves(WHITE);
            Iterator black = board.legalMoves(BLACK);

            while (white.hasNext()) {
                white.next();
                whiteMoves++;
            }

            while (black.hasNext()) {
                black.next();
                blackMoves++;
            }
        }

        return blackMoves - whiteMoves;
    }
}
