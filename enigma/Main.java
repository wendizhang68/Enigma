package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static enigma.TestUtils.*;

import static enigma.EnigmaException.*;

/**
 * Enigma simulator.
 *
 * @author Wendi Zhang
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
    private void process() {
        _counter += 1;
        if (_counter == 1) {
            machine = readConfig();
            String firstLine = _input.nextLine();
            setUp(machine, firstLine);
            if (!firstLine.split(" ")[0].equals("*")) {
                throw error("wrong format of input");
            }
        } else {
            if (!tmp.split(" ")[0].equals("*")) {
                throw error("wrong format of input");
            }
            setUp(machine, tmp);
        }
        while (_input.hasNextLine()) {
            tmp = _input.nextLine();
            if (tmp.isEmpty()) {
                printMessageLine(tmp);
            } else {
                if (!tmp.split(" ")[0].equals("*")) {
                    String result = "";
                    result += machine.convert(tmp.replaceAll
                            ("\\s+", "").toUpperCase());
                    printMessageLine(result);
                } else {
                    process();
                }
            }
        }
    }

    /**
     * Return an Enigma machine configured from the contents of configuration
     * file _config.
     */
    private Machine readConfig() {
        int numPawls;
        int numRotor;
        boolean normalChar = true;
        List<Rotor> collection = new ArrayList<>();
        try {
            String[] alphabet = _config.nextLine().split("");
            for (int i = 0; i < alphabet.length; i += 1) {
                if (alphabet[i].equals("-")) {
                    normalChar = true;
                    break;
                } else {
                    normalChar = false;
                }
            }
            if (normalChar) {
                char startrange = alphabet[0].charAt(0);
                char endrange = alphabet[alphabet.length - 1].charAt(0);
                _alphabet = new CharacterRange(startrange, endrange);
            } else {
                for (int j = 0; j < alphabet.length; j += 1) {
                    _myAlphabet.add(alphabet[j].charAt(0));
                }
                _alphabet = new MyAlphabet(_myAlphabet);
            }
            for (int i = 0; i < alphabet.length; i += 1) {
                if (alphabet[i].equals(" ")) {
                    throw error("no space in setting");
                }
                for (int j = i + 1; j < alphabet.length; j += 1) {
                    if (alphabet[i].equals(alphabet[j])) {
                        throw error("no repeat element");
                    }
                }
            }
            if (!_config.hasNextInt()) {
                throw error("wrong format of configuration file");
            } else {
                numRotor = _config.nextInt();
                numPawls = _config.nextInt();
                _config.nextLine();
            }
            while (_config.hasNextLine()) {
                rotorline = _config.nextLine();
                if (rotorline.replaceAll(" ", "").isEmpty()) {
                    throw error("wrong format of config");
                }
                Rotor currrotor = readRotor();
                collection.add(currrotor);
                _findrotor.put(currrotor.name(), currrotor);
            }
            return new Machine(_alphabet, numRotor, numPawls, collection);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /**
     * Return a rotor, reading its description from _config.
     */
    private Rotor readRotor() {
        try {
            String cycles = "";
            String rotorname, rotortype, typeAndNotch, notches = "";
            while (true) {
                String rotorLine = rotorline.trim();
                String[] rotorInfo = rotorLine.split("\\s+");
                if (rotorInfo[0].charAt(0) != '(') {
                    rotorname = rotorInfo[0];
                    typeAndNotch = rotorInfo[1];
                    rotortype = typeAndNotch.substring(0, 1);
                    if (typeAndNotch.length() > 1
                            && (rotortype.equals("R")
                            || rotortype.equals("N"))) {
                        throw error("R and N have no notch");
                    }
                    if (typeAndNotch.length() > 1) {
                        notches = typeAndNotch.substring(1);
                    }
                    if (rotorInfo[2].charAt(0) != '(') {
                        throw error("wrong format of cycle");
                    }
                    for (int i = 2; i < rotorInfo.length; i += 1) {
                        cycles += rotorInfo[i];
                    }
                    String last = rotorInfo[rotorInfo.length - 1];
                    if (last.charAt(last.length() - 1) != ')') {
                        throw error("cycles should be closed");
                    }
                    int fullyderange = _alphabet.size() / 2;
                    if (rotortype.equals("R")
                            && rotorInfo.length < (2 + fullyderange)) {
                        String rline = _config.nextLine().trim();
                        String[] permInfo = rline.split("\\s+");
                        for (int i = 0; i < permInfo.length; i += 1) {
                            cycles += permInfo[i];
                        }
                    }
                } else {
                    throw error("wrong format of config file");
                }
                Permutation rotorPerm = new Permutation(cycles, _alphabet);
                if (rotortype.equals("M")) {
                    return new MovingRotor(rotorname, rotorPerm, notches);
                } else if (rotortype.equals("N")) {
                    return new FixedRotor(rotorname, rotorPerm);
                } else if (rotortype.equals("R")) {
                    return new Reflector(rotorname, rotorPerm);
                } else {
                    throw error("rotor type not found");
                }
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /**
     * Set M according to the specification given on SETTINGS,
     * which must have the format specified in the assignment.
     */
    private void setUp(Machine M, String settings) {
        List<String> rotorsName = new ArrayList<>();
        int mRotors = M.numRotors();
        int nPawls = M.numPawls();
        int intpos = 0;
        int countmoving = 0;
        settings.trim();
        String[] setArr = settings.split("\\s+");
        if (!setArr[0].equals("*")) {
            throw error("wrong setting line format");
        }
        Rotor reflector = _findrotor.get(setArr[1]);
        if (!reflector.reflecting()) {
            throw error("reflector in wrong place");
        }
        for (int i = 1; i < setArr.length; i += 1) {
            if (_findrotor.containsKey(setArr[i])) {
                rotorsName.add(setArr[i]);
            } else {
                intpos = i;
                break;
            }
        }
        if (rotorsName.size() != mRotors) {
            throw error("wrong number of rotors");
        }
        for (int i = 0; i < rotorsName.size(); i += 1) {
            Rotor currRotor = _findrotor.get(rotorsName.get(i));
            if (currRotor.rotates()) {
                countmoving += 1;
            }
        }
        if (countmoving != nPawls) {
            throw error("wrong number of moving rotors");
        }
        String setRotor = setArr[intpos];
        if (setRotor.length() != mRotors - 1) {
            throw error("wrong format of setting");
        }
        String plugCycles = "";
        for (int j = intpos + 1; j < setArr.length; j += 1) {
            plugCycles += setArr[j];
        }
        String[] rotors = new String[rotorsName.size()];
        rotors = rotorsName.toArray(rotors);
        for (int i = 0; i < setRotor.length(); i += 1) {
            if (!_alphabet.contains(setRotor.charAt(i))) {
                throw error("wrong initial position");
            }
        }
        if (setArr.length > (mRotors + 2)
                && setArr[mRotors + 2].charAt(0) != '(') {
            throw error("wrong format of cycle");
        }
        M.insertRotors(rotors);
        M.setRotors(setRotor);
        M.setPlugboard(new Permutation(plugCycles, _alphabet));
    }

    /**
     * Print MSG in groups of five (except that the last group may
     * have fewer letters).
     */
    private void printMessageLine(String msg) {
        String message = "";
        for (int i = 0; i < msg.length(); i += 1) {
            if ((i + 1) % 5 == 0) {
                message += msg.charAt(i);
                message += " ";
            } else {
                message += msg.charAt(i);
            }
        }
        _output.println(message);
    }

    /**
     * Alphabet used in this machine.
     */
    private Alphabet _alphabet;

    /**
     * MyAlphabet used in this machine.
     */
    private ArrayList<Character> _myAlphabet = new ArrayList<>();

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

    /**
     * Set a counter for the function process().
     */
    private int _counter;

    /**
     * Set tmp string for process().
     */
    private String tmp = "";

    /**
     * Set machine for process.
     */
    private Machine machine = new Machine(_alphabet, 0, 0, allRotor1);

    /**
     * Hashmap for matching rotor name with rotors.
     */
    private static HashMap<String, Rotor> _findrotor = new HashMap<>();

    /**
     * Rotorline for checking empty.
     */
    private String rotorline = "";
}
