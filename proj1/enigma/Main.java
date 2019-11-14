package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import static enigma.EnigmaException.*;

/**
 * Enigma simulator.
 *
 * @author JosephHayes
 */
public final class Main {

    /**
     * Process a sequence of encryptions and decryptions, as
     * specified by ARGS, where 1 <= ARGS.length <= 3.
     * ARGS[0] is the name of a configuration file.
     * ARGS[1] is optional; when present, it names an input file
     * containing messages.  Otherwise, input comes from the standard
     * input.  ARGS[2] is optional; when present, it names an output
     * file for processed messages.  Otherwise, output goes to the
     * standard output. Exits normally if there are no errors in the input;
     * otherwise with code 1.
     */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /**
     * Check ARGS and open the necessary files (see comment on main).
     * Format of ARGS[0], ARGS[1], ARGS[2].
     */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /**
     * Return a Scanner reading from the file named NAME.
     */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Return a PrintStream writing to the file named NAME.
     */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /**
     * Configure an Enigma machine from the contents of configuration
     * file _config and apply it to the messages in _input, sending the
     * results to _output.
     */
    private void process() throws EnigmaException {
        try {
            String currentLine;
            String inputConfig;
            _theEnigma = readConfig();

            while (_input.hasNextLine()) { //FIXME need to check for * on first line

                currentLine = _input.nextLine().trim();

                if (currentLine.contains("*")) {
                    inputConfig = currentLine;
                    setUp(_theEnigma, inputConfig);
                    currentLine = _input.nextLine();
                }
                if (currentLine.contains("*")) { //FIXME takes care of format2.inp error SLOPPY
                    currentLine = _input.next();
                }
                _output.append(_theEnigma.convert(currentLine) + "\n");
            }
        } catch (NoSuchElementException excp) {
            System.exit(0);
        } catch (IndexOutOfBoundsException excp) {
            System.exit(0);
        }
    }
    /** A method for pulling apart my INPUT. */
    private void processInput() throws EnigmaException {
        //IDEA: take the string from process() and make the decisions here.
        String currentLine;
        String inputConfig;

            currentLine = _input.nextLine();

            if (currentLine.contains("*")) { //set the rotor setting


            }
    }

    /**
     * Return an Enigma machine configured from the contents of configuration
     * file _config.
     */
    private Machine readConfig() {
        try {
            String getAlpha = _config.nextLine().trim();

            _alphabet = new CharacterRange(getAlpha.charAt(0),
                    getAlpha.charAt(2));
            _numRotors = Integer.parseInt(_config.next());
            _numPawls = Integer.parseInt(_config.next());

            while (_config.hasNextLine()) {
                _allRotors.add(readRotor());
            }
            return new Machine(_alphabet, _numRotors, _numPawls, _allRotors);

        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /**
     * Return a rotor, reading its description from _config.
     */
    private Rotor readRotor() {
        try {
            String name = _config.next().toUpperCase();
            String notches = _config.next();
            String cycles = _config.nextLine();

            if (notches.charAt(0) == 'R') {
                cycles += _config.nextLine();
            }

            Permutation permutation = new Permutation(cycles, _alphabet);
            if (notches.charAt(0) == 'M') {
                return new MovingRotor(name, permutation, notches);
            } else if (notches.charAt(0) == 'N') {
                return new FixedRotor(name, permutation);
            } else if (notches.charAt(0) == 'R') {
                return new Reflector(name, permutation);
            } else {
                return null;
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }
    /** Parses the SETTINGS line from the setUp method and stores in the
     * appropriate instance variables. */
    private void createSettings(String settings) throws EnigmaException {
        try {

            settings = settings.replace("*", "").trim();
            ArrayList<String> rotorSetLine = new ArrayList<String>();

            Scanner input = new Scanner(settings);

            for (int i = 1; i <= _numRotors; i++) { //adding rotor pieces to ArrayList
                rotorSetLine.add(input.next());
            }
            _rotorSetUp = input.next();

            if (input.hasNext()) {
                _plugBoardCycles = input.nextLine();
            }
            _rotorInserts = rotorSetLine.toArray(_rotorInserts);

        } catch (NoSuchElementException excp) {
            throw error("No rotor settings in input file");
        }
    }

    /**
     * Set M according to the specification given in SETTINGS,
     * which must have the format specified in the assignment.
     */
    private void setUp(Machine M, String settings) {

        if (!settings.contains("*")) {
            throw error("No configuration in file. ");
        }
        createSettings(settings);
        _plugBoard = new Permutation(_plugBoardCycles, _alphabet);
        M.insertRotors(_rotorInserts);
        M.setRotors(_rotorSetUp);
        M.setPlugboard(_plugBoard);
    }

    /**
     * Print MSG in groups of five (except that the last group may
     * have fewer letters).
     */
    private void printMessageLine(String msg) {

    }

    /**
     * Alphabet used in this machine.
     */
    private Alphabet _alphabet;
    /**
     * Source of input messages.
     */
    private Scanner _input;
    /**
     * Source of machine configuration.
     */
    private Scanner _config;

    /**
     * File for encoded/decoded messages.
     */
    private PrintStream _output;

    /** MACHINE instance for MAIN. */
    private Machine _theEnigma;

    /**
     * Collection of ROTORS for MACHINE.
     */
    private ArrayList<Rotor> _allRotors = new ArrayList<Rotor>();

    /** Stores the PLUGBOARD permutation for a MACHINE instance. */
    private Permutation _plugBoard;
    /**
     * The number of rotors from _config.
     */
    private int _numRotors;
    /** The number of moving rotors for this MACHINE instance. */
    private int _numPawls;

    /** Stores the ROTORSETUP string for this MACHINE. */
    private String _rotorSetUp;
    /**
     * String of cycles for the PLUGBOARD permutation.
     */
    private String _plugBoardCycles = "";

    /**
     * String array for rotor insert positions in machine.
     */
    private String[] _rotorInserts = new String[_numRotors];
}
