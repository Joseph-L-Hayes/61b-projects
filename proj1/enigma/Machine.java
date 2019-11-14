package enigma;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Collection;

import static enigma.EnigmaException.*;

/**
 * Class that represents a complete enigma machine.
 *
 * @author josephhayes
 */
class Machine {

    /**
     * A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     * and 0 <= PAWLS < NUMROTORS pawls. ALLROTORS contains all the
     * available rotors.
     */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        this._alphabet = alpha;
        this._numRotors = numRotors;
        this._pawls = pawls;
        addRotors(allRotors);
    }

    /**
     * Adds rotors in ROTORS collection to a HashMap with the names of rotors
     * as keys and rotors as values.
     */
    private void addRotors(Collection<Rotor> rotors) {

        for (Rotor rtr : rotors) {
            this._allRotors.put(rtr.name(), rtr);
        }
    }

    /**
     * Return the number of rotor slots this machine has.
     */
    int numRotors() {

        return this._numRotors;
    }

    /**
     * Return the number pawls (and thus rotating rotors) this machine has.
     */
    int numPawls() {

        return this._pawls;
    }

    /**
     * Set my rotor slots to the rotors named ROTORS from this machine's set of
     * available rotors (ROTORS[0] names the reflector).
     * Initially, all rotors are set at their 0 setting.
     */
    void insertRotors(String[] rotors) {
        if (!_slots.isEmpty()) {
            _slots.clear();
        }

        for (String r : rotors) {
            if (_allRotors.containsKey(r)
                    && !_slots.contains(_allRotors.get(r))) {
                _slots.add(_allRotors.get(r));
            }
        }
        if (Collections.frequency(_slots, "MovingRotor") > _pawls) {
            throw error("Too many moving rotors in machine.");
        }
    }

    /**
     * Set my rotors according to SETTING, which must be a string of
     * numRotors()-1 upper-case letters. The first letter refers to the
     * leftmost rotor setting (not counting the reflector).
     */
    void setRotors(String setting) throws EnigmaException {
        setting = setting.toUpperCase();

        if (setting.length() > numRotors() - 1) {
            throw error("Too many characters in setting", setting);
        }
        for (int i = 0; i < setting.length(); i++) {
            _slots.get(i + 1).set(setting.charAt(i));
        }
    }

    /**
     * Set the plugboard to PLUGBOARD.
     */
    void setPlugboard(Permutation plugboard) {

        this._plugBoard = plugboard;
    }

    /**
     * Returns a Reflector with PLUGBOARD as its permutation if a permutation
     * for the PLUGBOARD is given. Otherwise it returns an empty plug board.
     */

    private Reflector createPlugBoard() {
        if (_plugBoard != null) {
            return new Reflector("Plug Board", _plugBoard);
        } else {
            return new Reflector("No Plugboard",
                    new Permutation("", _alphabet));
        }
    }

    /**
     * Advances the machine according to notch positions for each key entry.
     */
    private void machineAdvance() {

        Rotor moving = _slots.get(_slots.size() - 1);

        for (int i = _slots.size() - 1; i > 0; i--) {
            if (_slots.get(i).atNotch()) {
                _slots.get(i - 1).advance();
            }
        }
        moving.advance();
    }

    /**
     * Returns the result of converting the input character C (as an
     * index in the range 0..alphabet size - 1), after first advancing
     * the machine.
     */
    int convert(int c) {
        int converter = 0;

        if (c > _alphabet.size()) {
            char temp = Character.toUpperCase((char) c);
            converter = _alphabet.toInt(temp);
        } else {
            converter = c;
        }

        machineAdvance();

        Rotor plugBoard = createPlugBoard();

        converter = plugBoard.convertForward(converter);

        for (int i = _slots.size() - 1; i >= 0; i--) {
            converter = _slots.get(i).convertForward(converter);
        }

        for (int j = 1; j < _slots.size(); j++) {
            converter = _slots.get(j).convertBackward(converter);
        }

        converter = plugBoard.convertForward(converter);

        return converter;
    }

    /**
     * Returns the encoding/decoding of MSG, updating the state of
     * the rotors accordingly.
     */
    String convert(String msg) {

        msg = msg.toUpperCase();
        msg = msg.replaceAll("\\s+", "");
        String convertedMsg = "";

        char alphaIndex;

        for (int i = 0; i < msg.length(); i++) {
            if (i % 5 == 0 && i > 0) {
                convertedMsg += " ";
            }
            alphaIndex = _alphabet.toChar(convert(msg.charAt(i)));
            convertedMsg += Character.toString(alphaIndex);
        }
        return convertedMsg;
    }

    /**
     * Common alphabet of my rotors.
     */
    private final Alphabet _alphabet;
    /**
     * A hashmap to store ROTORS from Collection ALLROTORS.
     */
    private HashMap<String, Rotor> _allRotors = new HashMap<>();
    /**
     * The permutation for PLUGBOARD of this machine.
     */
    private Permutation _plugBoard;
    /**
     * The number of ROTORS in this Machine.
     */
    private int _numRotors = 0;
    /**
     * The number of PAWLS and thus number of moving ROTORS in this Machine.
     */
    private int _pawls;
    /**
     * An ArrayList SLOTS that holds the ROTORS for this for this Machine.
     */
    private ArrayList<Rotor> _slots = new ArrayList<>(_numRotors + 1);

}
