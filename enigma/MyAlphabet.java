package enigma;

import java.util.ArrayList;

import static enigma.EnigmaException.*;

/**
 * An Alphabet consisting of the Unicode characters in a certain range in
 * order.
 *
 * @author Wendi Zhang
 */
class MyAlphabet extends Alphabet {

    /**
     * @param alphabet Create my alphabet.
     */
    MyAlphabet(ArrayList<Character> alphabet) {
        if (alphabet.isEmpty()) {
            throw error("empty range of characters");
        }
        _myalphabet = alphabet;
    }

    @Override
    int size() {
        return _myalphabet.size();
    }

    @Override
    boolean contains(char ch) {
        return _myalphabet.contains(ch);
    }

    @Override
    char toChar(int index) {
        if (index > size()) {
            throw error("character index out of range");
        }
        return _myalphabet.get(index);
    }

    @Override
    int toInt(char ch) {
        if (!contains(ch)) {
            throw error("character out of range");
        }
        return _myalphabet.indexOf(ch);
    }

    /**
     * Arraylist taking in to construct the alphabet.
     */
    private ArrayList<Character> _myalphabet;

}
