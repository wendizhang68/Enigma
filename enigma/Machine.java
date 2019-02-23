package enigma;

import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;

import static enigma.EnigmaException.*;

/**
 * Class that represents a complete enigma machine.
 *
 * @author Wendi Zhang
 */
class Machine {

    /**
     * A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     * and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     * available rotors.
     */

    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _numRotors = numRotors;
        _pawls = pawls;
        _alphabet = alpha;
        _allRotor = new ArrayList<>(allRotors);
        for (int i = 0; i < _allRotor.size(); i += 1) {
            Rotor rotor = _allRotor.get(i);
            String rotorName = rotor.name();
            _findrotor.put(rotorName, rotor);
        }
    }

    /**
     * Return the number of rotor slots I have.
     */
    int numRotors() {
        return _numRotors;
    }

    /**
     * Return the number pawls (and thus rotating rotors) I have.
     */
    int numPawls() {
        return _pawls;
    }

    /**
     * Set my rotor slots to the rotors named ROTORS from my set of
     * available rotors (ROTORS[0] names the reflector).
     * Initially, all rotors are set at their 0 setting.
     */
    void insertRotors(String[] rotors) {
        _rotorsList = new ArrayList<>();
        for (int i = 0; i < rotors.length; i += 1) {
            String currName = rotors[i];
            if (_findrotor.containsKey(currName)) {
                Rotor currRotor = _findrotor.get(currName);
                _rotorsList.add(currRotor);
            } else {
                throw error("wrong name of rotor: Rotor name not found");
            }
        }
        if (!_rotorsList.get(0).reflecting()) {
            throw error("the first rotor should be a reflector");
        }
        if (_rotorsList.size() != numRotors()) {
            throw error("number of rotors does not match"
                    + "number of rotor slots");
        }
    }

    /**
     * Set my rotors according to SETTING, which must be a string of
     * numRotors()-1 upper-case letters. The first letter refers to the
     * leftmost rotor setting (not counting the reflector).
     */
    void setRotors(String setting) {
        char[] settingArr = setting.toCharArray();
        if (settingArr.length != numRotors() - 1) {
            throw error("wrong length of setting");
        }
        for (int i = 0; i < settingArr.length; i += 1) {
            Rotor currRotor = _rotorsList.get(i + 1);
            currRotor.set(settingArr[i]);
        }
    }

    /**
     * Set the plugboard to PLUGBOARD.
     */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /**
     * Returns the result of converting the input character C (as an
     * index in the range 0..alphabet size - 1), after first advancing
     * the machine.
     */
    int convert(int c) {
        int counter = 0;
        Rotor[] allrotor = new Rotor[numRotors()];
        allrotor = _rotorsList.toArray(allrotor);
        int input = c;
        if (_plugboard != null) {
            input = _plugboard.permute(c);
        }
        Rotor freeMoving = allrotor[numRotors() - 1];
        if (freeMoving.atNotch()
                && (!allrotor[numRotors() - 2].reflecting())) {
            if (allrotor[numRotors() - 2].atNotch()
                    && (!allrotor[numRotors() - 3].reflecting())) {
                allrotor[numRotors() - 3].advance();
            }
            allrotor[numRotors() - 2].advance();
            counter += 1;
        }
        freeMoving.advance();
        input = freeMoving.convertForward(input);

        for (int i = numRotors() - 2; i > (numRotors() - numPawls()); i -= 1) {
            if (allrotor[i].atNotch()
                    && (!allrotor[i].reflecting())) {
                if ((i == (numRotors() - 2) && counter == 0)
                        || i != (numRotors() - 2)) {
                    allrotor[i - 1].advance();
                    allrotor[i].advance();
                }
            }
            input = allrotor[i].convertForward(input);
        }
        Rotor leftMost = allrotor[numRotors() - numPawls()];
        if (leftMost != freeMoving) {
            input = leftMost.convertForward(input);
        }
        for (int i = numRotors() - numPawls() - 1; i > -1; i -= 1) {
            input = allrotor[i].convertForward(input);
        }
        for (int i = 1; i < numRotors(); i += 1) {
            input = allrotor[i].convertBackward(input);
        }
        int output = input;
        if (_plugboard != null) {
            output = _plugboard.permute(output);
        }
        return output;
    }

    /**
     * Returns the encoding/decoding of MSG, updating the state of
     * the rotors accordingly.
     */
    String convert(String msg) {
        String resultStr = "";
        for (int i = 0; i < msg.length(); i += 1) {
            char msgChar = msg.charAt(i);
            int msgNum = _alphabet.toInt(msgChar);
            int afterConvert = convert(msgNum);
            char after = _alphabet.toChar(afterConvert);
            resultStr += Character.toString(after);
        }
        return resultStr;
    }

    /**
     * Common alphabet of my rotors.
     */
    private final Alphabet _alphabet;

    /**
     * Number of rotors of my machine.
     */
    private int _numRotors;

    /**
     * Number of pawls of my machine.
     */
    private int _pawls;

    /**
     * Collection of all rotors.
     */
    private ArrayList<Rotor> _allRotor;

    /**
     * Rotors that I inserted.
     */
    private ArrayList<Rotor> _rotorsList;

    /**
     * The plugboard of my machine.
     */
    private Permutation _plugboard;

    /**
     * Hashmap that maps name to rotor.
     */
    private static HashMap<String, Rotor> _findrotor = new HashMap<>();
}
