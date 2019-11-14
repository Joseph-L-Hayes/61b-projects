package amazons;

import org.junit.Test;
import ucb.junit.textui;

import static amazons.Piece.*;
import static org.junit.Assert.*;

/**
 * The suite of all JUnit tests for the amazons package.
 *
 * @author JosephHayes
 */
public class UnitTest {

    /**
     * Run the JUnit tests in this package. Add xxxTest.class entries to
     * the arguments of runClasses to run other JUnit tests.
     */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class, IteratorTests.class);
    }

    /**
     * Tests basic correctness of put and get on the initialized board.
     */
    @Test
    public void testBasicPutGet() {
        Board b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        assertEquals(b.get(3, 5), BLACK);
        b.put(WHITE, Square.sq(9, 9));
        assertEquals(b.get(9, 9), WHITE);
        b.put(EMPTY, Square.sq(3, 5));
        assertEquals(b.get(3, 5), EMPTY);
        b.put(BLACK, 2, 2);
        assertEquals(b.get(2, 2), BLACK);
        assertEquals(b.get('c', '3'), BLACK);
    }

    /**
     * Tests proper identification of legal/illegal queen moves.
     */
    @Test
    public void testIsQueenMove() {
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(1, 5)));
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(2, 7)));
        assertFalse(Square.sq(0, 0).isQueenMove(Square.sq(5, 1)));

        assertTrue(Square.sq(1, 1).isQueenMove(Square.sq(9, 9)));
        assertTrue(Square.sq("b2").isQueenMove(Square.sq("j10")));
        assertTrue(Square.sq(2, 7).isQueenMove(Square.sq(8, 7)));
        assertTrue(Square.sq(3, 0).isQueenMove(Square.sq(3, 4)));
        assertTrue(Square.sq(7, 9).isQueenMove(Square.sq(0, 2)));
        assertTrue(Square.sq("a2").isQueenMove(Square.sq("b1")));
        assertTrue(Square.sq("d4").isQueenMove(Square.sq("d10")));
    }

    /**
     * Tests toString for initial board state and a smiling board state. :)
     */
    @Test
    public void testToString() {
        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeSmile(b);
        assertEquals(SMILE, b.toString());
    }

    @Test
    public void squareConstructorTest() {

        assertEquals("d3", Square.sq(23).toString());
        assertEquals("a1", Square.sq(0).toString());
        assertEquals("j10", Square.sq(99).toString());
        assertEquals("f5", Square.sq(45).toString());
        assertEquals("b1", Square.sq(1).toString());
        assertEquals("a10", Square.sq(90).toString());
    }

    @Test
    public void sqIntIntTest() {
        assertEquals("c3", Square.sq(2, 2).toString());
        assertEquals("d3", Square.sq(3, 2).toString());
        assertEquals("j10", Square.sq(9, 9).toString());
    }

    @Test
    public void sqStringPosnTest() {
        assertEquals("a1", Square.sq("a1").toString());
        assertEquals("j10", Square.sq("j10").toString());
    }

    @Test
    public void sqStringStringTest() {
        assertEquals("d3", Square.sq("d", "3").toString());
        assertEquals("j10", Square.sq("j", "10").toString());
        assertEquals("a10", Square.sq("a", "10").toString());
    }

    @Test
    public void queenMoveTest() {
        Square from1 = Square.sq("a1");

        assertEquals(Square.sq("a2"), from1.queenMove(0, 1));
        assertEquals(Square.sq("b2"), from1.queenMove(1, 1));
        assertEquals(Square.sq("e5"), from1.queenMove(1, 4));
        assertEquals(Square.sq("b1"), from1.queenMove(2, 1));
        assertEquals(Square.sq("d1"), from1.queenMove(2, 3));

        Square from2 = Square.sq("j10");
        assertEquals(Square.sq("j9"), from2.queenMove(4, 1));
        assertEquals(Square.sq("i10"), from2.queenMove(6, 1));
        assertEquals(Square.sq("h8"), from2.queenMove(5, 2));

        Square from3 = Square.sq("i10");
        assertEquals(Square.sq("j9"), from3.queenMove(3, 1));
        assertEquals(null, from3.queenMove(0, 1));

        Square from4 = Square.sq("j1");
        assertEquals(Square.sq("i2"), from4.queenMove(7, 1));
        assertEquals(null, from4.queenMove(8, 1));
    }

    @Test
    public void directionTest() {
        Square from = Square.sq("a1");
        Square to1 = Square.sq("b1");
        Square to2 = Square.sq("a2");
        Square to3 = Square.sq("b2");

        assertEquals(0, from.direction(to2));
        assertEquals(1, from.direction(to3));
        assertEquals(2, from.direction(to1));
        assertEquals(3, to2.direction(to1));
        assertEquals(4, to2.direction(from));
        assertEquals(5, to3.direction(from));
        assertEquals(6, to1.direction(from));
        assertEquals(7, to1.direction(to2));
    }

    @Test
    public void isLegalSquareTest() {
        Board legal = new Board();
        legal.init();
        legal.put(SPEAR, Square.sq("e7"));
        legal.put(SPEAR, Square.sq("a4"));
        legal.put(SPEAR, Square.sq("b4"));

        legal.put(WHITE, Square.sq("g4"));

        legal.put(WHITE, Square.sq("d3"));
        legal.put(WHITE, Square.sq("j10"));

        assertFalse(legal.isLegal(Square.sq("a4")));
        assertFalse(legal.isLegal(Square.sq("b4")));
        assertFalse(legal.isLegal(Square.sq("e7")));

        assertTrue(legal.isLegal(Square.sq("d3")));
        assertTrue(legal.isLegal(Square.sq("g4")));
        assertTrue(legal.isLegal(Square.sq("j10")));

        assertFalse(legal.isLegal(Square.sq("a4"), Square.sq("a10")));
        assertFalse(legal.isLegal(Square.sq("j10"), Square.sq("d1")));
        assertFalse(legal.isLegal(Square.sq("d4"), Square.sq("d10")));

        assertTrue(legal.isLegal(Square.sq("d3"), Square.sq("d4")));
        assertFalse(legal.isLegal(Square.sq("d1"), Square.sq("d4")));

        assertTrue(legal.isLegal(Square.sq("d3"), Square.sq("d4"),
                Square.sq("d9")));
        assertTrue(legal.isLegal(Square.sq("d3"), Square.sq("d4"),
                Square.sq("b6")));
        assertTrue(legal.isLegal(Square.sq("d3"), Square.sq("d4"),
                Square.sq("d8")));
        assertTrue(legal.isLegal(Square.sq("d3"), Square.sq("d4"),
                Square.sq("g7")));

        legal.put(SPEAR, Square.sq("a7"));
        assertFalse(legal.isLegal(Square.sq("d4"), Square.sq("b4"),
                Square.sq("f7")));
        assertFalse(legal.isLegal(Square.sq("d3"), Square.sq("d4"),
                Square.sq("c1")));
        assertFalse(legal.isLegal(Square.sq("d3"), Square.sq("d4"),
                Square.sq("j9")));
        assertFalse(legal.isLegal(Square.sq("d3"), Square.sq("d4"),
                Square.sq("a7")));

    }

    @Test
    public void isLegalMoveTest() {
        Board legalMove = new Board();
        Move d1a1g1 = Move.mv(Square.sq("d1"), Square.sq("a1"),
                Square.sq("g1"));
        legalMove.put(SPEAR, Square.sq("a1"));

        Move d10Tod4 = Move.mv(Square.sq("d10"), Square.sq("d4"),
                Square.sq("d10"));
        Move a7Toi7 = Move.mv(Square.sq("a7"), Square.sq("i7"),
                Square.sq("i1"));
        Move d10Toj4 = Move.mv(Square.sq("d10"), Square.sq("j4"),
                Square.sq("j1"));
        Move a4Toa1 = Move.mv(Square.sq("a4"), Square.sq("a1"),
                Square.sq("a2"));

        legalMove.put(WHITE, Square.sq("d10"));
        legalMove.put(WHITE, Square.sq("a7"));
        assertTrue(legalMove.isLegal(d10Tod4));
        assertTrue(legalMove.isLegal(a7Toi7));

        legalMove.put(SPEAR, Square.sq("i1"));
        legalMove.put(SPEAR, Square.sq("d8"));

        assertFalse(legalMove.isLegal(a7Toi7));
        assertFalse(legalMove.isLegal(d10Tod4));
        assertFalse(legalMove.isLegal(d10Toj4));
        assertFalse(legalMove.isLegal(a4Toa1));

    }

    @Test
    public void isUnblockedMoveTest() {
        Board board = new Board();
        board.init();

        assertFalse(board.isUnblockedMove(Square.sq("g1"), Square.sq("c1"),
                null));

        assertTrue(board.isUnblockedMove(Square.sq("g1"), Square.sq("e1"),
                null));
        assertTrue(board.isUnblockedMove(Square.sq("g1"), Square.sq("e1"),
                Square.sq("j10")));
        assertTrue(board.isUnblockedMove(Square.sq("g1"), Square.sq("c1"),
                Square.sq("d1")));
        assertTrue(board.isUnblockedMove(Square.sq("g1"), Square.sq("b6"),
                Square.sq("d1")));
        assertTrue(board.isUnblockedMove(Square.sq("d10"), Square.sq("d1"),
                Square.sq("d1")));

        board.put(BLACK, Square.sq("a1"));
        assertTrue(board.isUnblockedMove(Square.sq("a1"), Square.sq("j10"),
                null));
        board.put(WHITE, Square.sq("j10"));
        assertTrue(board.isUnblockedMove(Square.sq("j10"), Square.sq("a1"),
                Square.sq("a1")));
        board.put(WHITE, Square.sq("a10"));
        assertTrue(board.isUnblockedMove(Square.sq("a10"), Square.sq("j1"),
                Square.sq("a1")));

        board.init();

        assertTrue(board.isUnblockedMove(Square.sq("g1"), Square.sq("b6"),
                Square.sq("a4")));
        board.put(SPEAR, Square.sq("e3"));
        assertFalse(board.isUnblockedMove(Square.sq("g1"), Square.sq("b6"),
                Square.sq("j10")));
    }

    @Test
    public void makeMoveUndoTest() {
        Board moveTest = new Board();
        Move d1h5 = Move.mv("d1-h5(d1)");
        Move g1b6 = Move.mv("g1-b6(b10)");

        moveTest.makeMove(d1h5);

        assertEquals(SPEAR, moveTest.get(Square.sq("d1")));
        assertEquals(SPEAR, moveTest.get(Square.sq("d1")));
        assertEquals(WHITE, moveTest.get(Square.sq("h5")));

        moveTest.undo();

        assertEquals(BLACK, moveTest.get(Square.sq("d10")));
        assertEquals(EMPTY, moveTest.get(Square.sq("h4")));
        assertEquals(EMPTY, moveTest.get(Square.sq("c9")));

        moveTest.makeMove(g1b6);

        moveTest.undo();

        assertEquals(WHITE, moveTest.get(Square.sq("g1")));
        assertEquals(EMPTY, moveTest.get(Square.sq("b10")));
        assertEquals(EMPTY, moveTest.get(Square.sq("b6")));
    }

    @Test
    public void changeTurnTest() {
        Board turnTest = new Board();
        Move d1d5 = Move.mv("d1-d5(d6)");
        Move d10e9 = Move.mv("d10-e9(f9)");

        assertEquals(WHITE, turnTest.turn());
        assertFalse(turnTest.turn() == BLACK);

        turnTest.makeMove(d1d5);
        assertEquals(BLACK, turnTest.turn());

        turnTest.makeMove(d10e9);
        assertEquals(WHITE, turnTest.turn());

    }

    @Test
    public void copyTest() {
        Board a = new Board();
        Board b = new Board();
        makeSmile(a);

        b.copy(a);
        a.init();
    }
    @Test
    public void surroundTest() {
        Board winner = new Board();
        makeSmile(winner);
        winner.put(WHITE, Square.sq("c8"));
        assertTrue(winner.checkSurround(Square.sq("c8")));
        assertFalse(winner.checkSurround(Square.sq("e5")));

        assertFalse(winner.checkSurround(Square.sq("h8")));
        assertFalse(winner.checkSurround(Square.sq("a1")));
        assertFalse(winner.checkSurround(Square.sq("c4")));
        assertFalse(winner.checkSurround(Square.sq("d3")));
        assertFalse(winner.checkSurround(Square.sq("a10")));
        assertFalse(winner.checkSurround(Square.sq("j10")));
        assertFalse(winner.checkSurround(Square.sq("j1")));

        winner.put(BLACK, Square.sq("h8"));
        winner.put(BLACK, Square.sq("a10"));
        winner.put(SPEAR, Square.sq("a9"));
        winner.put(SPEAR, Square.sq("b10"));

        assertTrue(winner.checkSurround(Square.sq("h8")));
        assertTrue(winner.checkSurround(Square.sq("a10")));

        assertFalse(winner.checkSurround(Square.sq("d6")));
        assertFalse(winner.checkSurround(Square.sq("j10")));
        assertFalse(winner.checkSurround(Square.sq("j1")));
        assertFalse(winner.checkSurround(Square.sq("i7")));
        assertFalse(winner.checkSurround(Square.sq("e10")));


        winner.put(BLACK, Square.sq("c10"));
        winner.put(BLACK, Square.sq("d10"));
        winner.put(SPEAR, Square.sq("e10"));
        winner.put(SPEAR, Square.sq("e9"));
        assertEquals(WHITE, winner.winner());

    }

    @Test
    public void winnerTest() {

    }

    private void makeSmile(Board b) {
        b.put(EMPTY, Square.sq(0, 3));
        b.put(EMPTY, Square.sq(0, 6));
        b.put(EMPTY, Square.sq(9, 3));
        b.put(EMPTY, Square.sq(9, 6));
        b.put(EMPTY, Square.sq(3, 0));
        b.put(EMPTY, Square.sq(3, 9));
        b.put(EMPTY, Square.sq(6, 0));
        b.put(EMPTY, Square.sq(6, 9));

        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            b.put(WHITE, Square.sq(lip, 2));
        }
        b.put(WHITE, Square.sq(2, 3));
        b.put(WHITE, Square.sq(7, 3));
    }


    static final String INIT_BOARD_STATE =
            "   - - - B - - B - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   B - - - - - - - - B\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   W - - - - - - - - W\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - W - - W - - -\n";

    static final String SMILE =
            "   - - - - - - - - - -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - S - S - - S - S -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - W - - - - W - -\n"
                    + "   - - - W W W W - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n";


}

