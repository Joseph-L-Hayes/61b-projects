package enigma;

import static enigma.EnigmaException.*;

/**
 * Class that represents a rotating rotor in the enigma machine.
 *
 * @author josephhayes
 */
class MovingRotor extends Rotor {

    /**
     * A rotor named NAME whose permutation in its default setting is
     * PERM, and whose notches are at the positions indicated in NOTCHES.
     * The Rotor is initially in its 0 setting (first character of its
     * alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        this.setNotches(notches);
    }

    /**
     * Checks that notches is not empty and removes the first letter of the
     * string for notches if it is 'M' indicating that it is a moving notch.
     *
     * @param notches A string containing the notches for this rotor.
     */
    private void setNotches(String notches) {
        if (!notches.isEmpty() && notches.charAt(0) == 'M') {
            this._notches = notches.substring(1);
        } else {
            this._notches = notches;
        }
    }

    @Override
    boolean atNotch() {
        char notch = permutation().alphabet().toChar(this.setting());

        if (_notches.indexOf(notch) != -1) {
            return true;
        }
        return false;
    }

    @Override
    void advance() {
        this.set(setting() + 1);
    }

    @Override
    boolean rotates() {
        return true;
    }

    /** Stores the NOTCHES for this rotor after removing the style of rotor.*/
    private String _notches;
}
