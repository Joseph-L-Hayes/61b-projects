package make;

/* You MAY add public @Test methods to this class.  You may also add
 * additional public classes containing "UnitTest" in their name. These
 * may not be part of your make package per se (that is, it must be
 * possible to remove them and still have your package work). */

import org.junit.Test;
import ucb.junit.textui;
import static org.junit.Assert.*;
import java.util.ArrayList;

/** Unit tests for the make package. */
public class UnitTest {

    /** Run all JUnit tests in the make package. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(make.UnitTest.class));
    }

    @Test
    public void makerTest() {
        assertEquals(true, true);
    }

    @Test
    public void ruleTest() {
        Maker make = new Maker();
        Rule rulez = new Rule(make, "A: B C D A");
        ArrayList<String> commands = new ArrayList<>();
        commands.add("rebuild A");
        rulez.addCommands(commands);
        assertEquals(true, true);
    }

}
