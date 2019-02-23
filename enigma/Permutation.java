package enigma;

import java.util.HashMap;
import java.util.ArrayList;

import static enigma.EnigmaException.*;

/**
 * Represents a permutation of a range of integers starting at 0 corresponding
 * to the characters of an alphabet.
 *
 * @author Wendi Zhang
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
        ArrayList<String> cycle = new ArrayList<>();
        _cycles = cycles;
        _alphabet = alphabet;
        for (int i = 0; i < _cycles.length(); i += 1) {
            String each = "";
            if (_cycles.charAt(i) == '(') {
                i += 1;
                while (_cycles.charAt(i) != ')') {
                    each += _cycles.charAt(i);
                    i += 1;
                }
                if (each.equals("") || each.matches("\\s+")) {
                    throw error("wrong format of permutation:"
                            + "added cycle cannot be empty");
                }
                cycle.add(each);
            }
        }
        for (int i = 0; i < cycle.size(); i += 1) {
            String eachcycle = cycle.get(i);
            for (int j = 0; j < eachcycle.length() - 1; j++) {
                _findForward.put(eachcycle.charAt(j), eachcycle.charAt(j + 1));
                _findBackward.put(eachcycle.charAt(j + 1), eachcycle.charAt(j));
            }
            _findForward.put(eachcycle.charAt(eachcycle.length() - 1),
                    eachcycle.charAt(0));
            _findBackward.put(eachcycle.charAt(0),
                    eachcycle.charAt(eachcycle.length() - 1));
        }
        for (int a = 0; a < _alphabet.size(); a++) {
            if (!_findForward.containsKey(_alphabet.toChar(a))) {
                _findForward.put(_alphabet.toChar(a), _alphabet.toChar(a));
            }
            if (!_findBackward.containsKey(_alphabet.toChar(a))) {
                _findBackward.put(_alphabet.toChar(a), _alphabet.toChar(a));
            }
        }
    }

    /**
     * Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     * c0c1...cm.
     */
    private void addCycle(String cycle) {
        _cycles += cycle;
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
        char findf = _findForward.get(_alphabet.toChar(wrap(p)));
        return _alphabet.toInt(findf);
    }

    /**
     * Return the result of applying the inverse of this permutation
     * to  C modulo the alphabet size.
     */
    int invert(int c) {
        char findb = _findBackward.get(_alphabet.toChar(wrap(c)));
        return _alphabet.toInt(findb);
    }

    /**
     * Return the result of applying this permutation to the index of P
     * in ALPHABET, and converting the result to a character of ALPHABET.
     */
    char permute(char p) {
        char findf1 = _alphabet.toChar(wrap(_alphabet.toInt(p)));
        return _findForward.get(findf1);
    }

    /**
     * Return the result of applying the inverse of this permutation to C.
     */
    int invert(char c) {
        char findb1 = _alphabet.toChar(wrap(_alphabet.toInt(c)));
        return _findBackward.get(findb1);
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
        for (int a = 0; a < _alphabet.size(); a++) {
            if (_alphabet.toChar(a)
                    == _findForward.get(_alphabet.toChar(a))
                    || _alphabet.toChar(a)
                    == _findBackward.get(_alphabet.toChar(a))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Alphabet of this permutation.
     */
    private Alphabet _alphabet;

    /**
     * Cycles of this permutation.
     */
    private String _cycles;

    /**
     * HashMap for finding forward.
     */
    private HashMap<Character, Character> _findForward = new HashMap<>();

    /**
     * HashMap for finding backward.
     */
    private HashMap<Character, Character> _findBackward = new HashMap<>();
}
