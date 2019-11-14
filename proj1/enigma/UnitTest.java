package enigma;

import org.junit.Test;
import ucb.junit.textui;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static enigma.EnigmaException.error;

/**
 * The suite of all JUnit tests for the enigma package.
 *
 * @author JosephHayes
 */
public class UnitTest {

    /**
     * Run the JUnit tests in this package. Add xxxTest.class entries to
     * the arguments of runClasses to run other JUnit tests.
     */
    public static void main(String[] ignored) {
        textui.runClasses(PermutationTest.class, MovingRotorTest.class,
                MachineTest.class, MoreEnigmaTests.class);
    }

    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    @Test
    public void scannerTest() {
        Scanner config = getInput("testing/correct/default.conf");

        String getAlpha = config.nextLine().trim();
        System.out.println(getAlpha);
        Alphabet newAlpha = new CharacterRange(getAlpha.charAt(0),
                getAlpha.charAt(2));
        String numRotors = config.next();
        String numPawls = config.next();

        while (config.hasNextLine()) {


            String name = "Name: " + config.next().toUpperCase();
            String mynotches = "Notches: " + config.next().trim();
            String cycles = "Cycles: " + config.nextLine().trim();

            if (mynotches.contains("R")) {
                String extra = config.nextLine().trim();
                cycles = cycles + extra;
            }
            System.out.println(name);
            System.out.println(mynotches);
            System.out.println(cycles);
        }
    }
}



