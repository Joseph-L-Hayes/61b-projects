package enigma;

import java.util.ArrayList;

import static enigma.EnigmaException.*;

/**
 * Represents a permutation of a range of integers starting at 0 corresponding
 * to the characters of an alphabet.
 *
 * @author josephhayes
 */
class Permutation {

    /**
     * Set this Permutation to that specified by CYCLES, a string in the
     * form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     * is interpreted as a permutation in cycle notation.  Characters in the
     * alphabet that are not included in any cycle map to themselves.
     * Whitespace is ignored.
     */
    Permutation(String cycles, Alphabet alphabet) {
        this._alphabet = alphabet;
        splitCycles(cycles);
    }

    /**
     * Splits the String of CYCLES into an individual CYCLE at ")" character.
     * Removes whitespace, "(", and ")" characters from the string.
     */
    private void splitCycles(String cycles) {
        cycles = cycles.replaceAll("\\s", "").replaceAll("\\)", "-");

        String[] splitCycle = cycles.split("\\-");

        for (int i = 0; i < splitCycle.length; i++) {
            String tempString = splitCycle[i].replaceAll("\\(", "");
            addCycle(tempString);
        }
    }

    /**
     * Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     * c0c1...cm.
     */
    private void addCycle(String cycle) {
        this._cycles.add(cycle);
    }

    /**
     * Return the value of P modulo the size of this permutation.
     */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }
    /** Return the value of C modulo the size of this cycle.*/
    /**@param c the current position of this cycle.
     * @param cycleSize the size of this cycle.
     * @return the value of C modulo the size of this cycle.*/
    private int wrapCycle(int c, int cycleSize) {
        int r = c % cycleSize;
        if (r < 0) {
            r += cycleSize;
        }
        return r;
    }
    /**
     * Returns the size of the alphabet I permute.
     */
    int size() {
        return _alphabet.size();
    }

    /**
     * Return the result of applying this permutation to P modulo the
     * alphabet size.
     */
    int permute(int p) {

        char character = _alphabet.toChar(wrap(p));

        for (String cycle : _cycles) {
            int position = cycle.indexOf(character);

            if (position != -1) {
                position = wrapCycle(position + 1, cycle.length());
                return _alphabet.toInt(cycle.charAt(position));
            }
        }
        return p;
    }

    /**
     * Return the result of applying the inverse of this permutation
     * to C modulo the alphabet size.
     */
    int invert(int c) {
        char character = _alphabet.toChar(wrap(c));

        for (String cycle : _cycles) {
            int position = cycle.indexOf(character);

            if (position != -1) {
                position = wrapCycle(position - 1, cycle.length());
                return _alphabet.toInt(cycle.charAt(position));
            }
        }
        return c;
    }

    /**
     * Return the result of applying this permutation to the index of P
     * in ALPHABET, and converting the result to a character of ALPHABET.
     */
    char permute(char p) {
        return _alphabet.toChar(permute(_alphabet.toInt(p)));
    }

    /**
     * Return the result of applying the inverse of this permutation to C.
     */
    int invert(char c) {
        return _alphabet.toChar(invert(_alphabet.toInt(c)));
    }

    /**
     * Return the alphabet used to initialize this Permutation.
     */
    Alphabet alphabet() {
        return _alphabet;
    }

    /**
     * Return true iff this permutation is a derangement (i.e., a
     * permutation for which no value maps to itself).
     */
    boolean derangement() {
        for (String cycle : _cycles) {
            if (cycle.length() == 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Alphabet of this permutation.
     */
    private Alphabet _alphabet;
    /**
     * a list containing individual CYCLE linked lists.
     */
    private ArrayList<String> _cycles = new ArrayList<>();
}
