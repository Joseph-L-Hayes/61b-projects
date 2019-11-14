package amazons;
import org.junit.Test;
import static org.junit.Assert.*;
import ucb.junit.textui;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** Junit tests for our Board iterators.
 *  @author JosephHayes
 */
public class IteratorTests {

    /** Run the JUnit tests in this package. */
    public static void main(String[] ignored) {
        textui.runClasses(IteratorTests.class);
    }

    /** Tests reachableFromIterator to make sure it returns all reachable
     *  Squares. This method may need to be changed based on
     *   your implementation. */
    @Test
    public void testReachableFrom() {
        Board b = new Board();
        buildBoard(b, REACHABLEFROMTESTBOARD);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = b.reachableFrom(Square.sq("f5"), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            assertTrue(REACHABLEFROMTESTSQUARES.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(REACHABLEFROMTESTSQUARES.size(), numSquares);
        assertEquals(REACHABLEFROMTESTSQUARES.size(), squares.size());
    }

    @Test
    public void testReachableFromInitBoard() {
        Board c = new Board();
        buildBoard(c, REACHABLEFROMINITBOARD);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = c.reachableFrom(Square.sq("d1"), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            assertTrue(REACHABLEFROMTESTSQUARESINIT.contains(s));
            numSquares += 1;
            squares.add(s);
        }
        assertEquals(REACHABLEFROMTESTSQUARESINIT.size(), numSquares);
        assertEquals(REACHABLEFROMTESTSQUARESINIT.size(), squares.size());
    }

    @Test
    public void testReachableFromMiddle() {
        Board m = new Board();
        buildBoard(m, REACHABLEFROMMIDDLEBOARD);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = m.reachableFrom(Square.sq("d6"), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            assertTrue(TESTREACHABLEFROMD6.contains(s));
            numSquares += 1;
            squares.add(s);
        }

        assertEquals(TESTREACHABLEFROMD6.size(), numSquares);
        assertEquals(TESTREACHABLEFROMD6.size(), squares.size());
    }
    @Test
    public void testReachableFromBlockedIn() {
        Board i = new Board();
        buildBoard(i, REACHABLEFROMBLOCKEDIN);
        int numSquares = 0;
        Set<Square> squares = new HashSet<>();
        Iterator<Square> reachableFrom = i.reachableFrom(Square.sq("g6"), null);
        while (reachableFrom.hasNext()) {
            Square s = reachableFrom.next();
            assertFalse(REACHABLEFROMBLOCKED.contains(s));
            numSquares += 1;
            squares.add(s);
        }

        assertEquals(0, numSquares);
        assertEquals(0, squares.size());
    }
    /** Tests legalMovesIterator to make sure it returns all legal Moves.
     *  This method needs to be finished and may need to be changed
     *  based on your implementation. */
    @Test
    public void testLegalMovesWHITE() {
        Board b = new Board();
        buildBoard(b, REACHABLEFROMINITBOARD);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = b.legalMoves(Piece.WHITE);

        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            assertTrue(b.isLegal(m));
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(moves.size(), numMoves);
        assertEquals(2176, moves.size());
    }
    @Test
    public void testLegalMovesBLACK() {
        Board c = new Board();
        buildBoard(c, REACHABLEFROMINITBOARD);
        int numMoves = 0;
        Set<Move> moves = new HashSet<>();
        Iterator<Move> legalMoves = c.legalMoves(Piece.BLACK);
        while (legalMoves.hasNext()) {
            Move m = legalMoves.next();
            numMoves += 1;
            moves.add(m);
        }
        assertEquals(moves.size(), numMoves);
        assertEquals(2176, moves.size());
    }


    private void buildBoard(Board b, Piece[][] target) {
        for (int col = 0; col < Board.SIZE; col++) {
            for (int row = Board.SIZE - 1; row >= 0; row--) {
                Piece piece = target[Board.SIZE - row - 1][col];
                b.put(piece, Square.sq(col, row));
            }
        }
    }

    static final Piece E = Piece.EMPTY;

    static final Piece W = Piece.WHITE;

    static final Piece B = Piece.BLACK;

    static final Piece S = Piece.SPEAR;

    static final Piece[][] REACHABLEFROMTESTBOARD = {
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, W, W },
            { E, E, E, E, E, E, E, S, E, S },
            { E, E, E, S, S, S, S, E, E, S },
            { E, E, E, S, E, E, E, E, B, E },
            { E, E, E, S, E, W, E, E, B, E },
            { E, E, E, S, S, S, B, W, B, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E } };

    static final Piece[][] REACHABLEFROMINITBOARD = {
            { E, E, E, B, E, E, B, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { B, E, E, E, E, E, E, E, E, B },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { W, E, E, E, E, E, E, E, E, W },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, W, E, E, W, E, E, E }, };

    static final Piece[][] AIERROR = {
            { S, E, S, S, B, S, E, E, S, B },
            { E, E, S, S, S, S, E, S, E, S },
            { E, B, E, S, E, E, E, B, S, S },
            { S, E, S, S, E, E, E, E, S, S },
            { S, S, S, S, E, E, E, E, E, S },
            { E, E, W, E, E, E, E, S, S, W },
            { E, E, E, E, E, E, S, S, W, S },
            { E, E, E, E, E, E, S, E, S, S },
            { E, E, E, E, E, E, S, S, S, S },
            { E, E, E, E, E, S, E, S, E, W }, };
    static final Piece[][] REACHABLEFROMMIDDLEBOARD = {
            { E, E, E, B, E, E, B, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { B, E, E, E, E, E, E, E, E, B },
            { E, E, E, W, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { W, E, E, E, E, E, E, E, E, W },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, W, E, E, E } };
    static final Piece[][] REACHABLEFROMBLOCKEDIN = {
            { E, E, E, B, E, E, B, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { B, E, E, E, E, S, W, S, E, B },
            { E, E, E, E, E, S, W, S, E, E },
            { E, E, E, E, E, B, S, S, E, E },
            { W, E, E, E, E, E, E, E, E, W },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, W, E, E, E, E, E, E } };

    static final Piece[][] WHITEWINS = {
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, W, W },
            { E, E, E, E, E, E, E, S, E, S },
            { E, E, E, S, S, S, S, E, E, S },
            { E, E, E, S, B, B, W, E, S, E },
            { E, E, E, S, B, B, E, W, E, E },
            { E, E, E, S, S, S, S, W, S, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E },
            { E, E, E, E, E, E, E, E, E, E } };

    static final Set<Square> REACHABLEFROMTESTSQUARES =
            new HashSet<>(Arrays.asList(
                    Square.sq(5, 5),
                    Square.sq(4, 5),
                    Square.sq(4, 4),
                    Square.sq(6, 4),
                    Square.sq(7, 4),
                    Square.sq(6, 5),
                    Square.sq(7, 6),
                    Square.sq(8, 7)));

    static final Set<Square> REACHABLEFROMTESTSQUARESINIT =
            new HashSet<>(Arrays.asList(
                    Square.sq("e2"),
                    Square.sq("f3"),
                    Square.sq("g4"),
                    Square.sq("h5"),
                    Square.sq("i6"),
                    Square.sq("c2"),
                    Square.sq("b3"),
                    Square.sq("d2"),
                    Square.sq("d3"),
                    Square.sq("d4"),
                    Square.sq("d5"),
                    Square.sq("d6"),
                    Square.sq("d7"),
                    Square.sq("d8"),
                    Square.sq("d9"),
                    Square.sq("c1"),
                    Square.sq("b1"),
                    Square.sq("a1"),
                    Square.sq("e1"),
                    Square.sq("f1")));

    static final Set<Square> REACHABLEFROMBLOCKED =
            new HashSet<>(Arrays.asList());

    static final Set<Square> TESTREACHABLEFROMD6 =
            new HashSet<>(Arrays.asList(
                    Square.sq("e5"),
                    Square.sq("f4"),
                    Square.sq("g3"),
                    Square.sq("h2"),
                    Square.sq("i1"),

                    Square.sq("d1"),
                    Square.sq("d2"),
                    Square.sq("d3"),
                    Square.sq("d4"),
                    Square.sq("d5"),
                    Square.sq("d7"),
                    Square.sq("d8"),
                    Square.sq("d9"),

                    Square.sq("a6"),
                    Square.sq("b6"),
                    Square.sq("c6"),
                    Square.sq("e6"),
                    Square.sq("f6"),
                    Square.sq("g6"),
                    Square.sq("h6"),
                    Square.sq("i6"),
                    Square.sq("j6"),

                    Square.sq("c5"),
                    Square.sq("b4"),
                    Square.sq("a3"),


                    Square.sq("c7"),
                    Square.sq("b8"),
                    Square.sq("a9"),

                    Square.sq("e7"),
                    Square.sq("f8"),
                    Square.sq("g9"),
                    Square.sq("h10")));
}
