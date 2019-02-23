package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Machine class.
 *
 * @author Wendi Zhang
 */

public class MachineTest {
    /**
     * Testing time limit.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);


    /* ***** TESTING UTILITIES ***** */
    private Machine machine1;
    private Machine machine2;
    private Machine machine3;
    private Machine machine4;
    private Machine machine5;
    private Machine machine6;

    /* ***** TESTS ***** */
    @Test
    public void checkMachine1() {
        machine1 = new Machine(UPPER, 5, 3, allRotor1);
        assertEquals(5, machine1.numRotors());
        String[] rotorname = {"B", "BETA", "III", "IV", "I"};
        machine1.insertRotors(rotorname);
        machine1.setRotors("AXLE");
        machine1.setPlugboard(new Permutation("(YF)(HZ)", UPPER));
        assertEquals("Z", machine1.convert("Y"));
    }

    @Test
    public void checkMachine2() {
        machine2 = new Machine(UPPER, 3, 2, allRotor1);
        String[] rotorname = {"C", "I", "II"};
        machine2.insertRotors(rotorname);
        machine2.setRotors("AD");
        machine2.setPlugboard(new Permutation("", UPPER));
        assertEquals("N", machine2.convert("B"));
    }

    @Test
    public void checkMachine3() {
        machine3 = new Machine(UPPER, 6, 4, allRotor1);
        String[] rotorname = {"C", "GAMMA", "I", "IV", "VI", "II"};
        machine3.insertRotors(rotorname);
        machine3.setRotors("DHILD");
        machine3.setPlugboard(new Permutation("", UPPER));
        assertEquals("G", machine3.convert("E"));
    }

    @Test
    public void checkMachine4() {
        machine4 = new Machine(UPPER, 5, 3, allRotor1);
        String[] rotorname = {"B", "BETA", "III", "IV", "I"};
        machine4.insertRotors(rotorname);
        machine4.setRotors("AXLE");
        machine4.setPlugboard(new Permutation("(HQ) (EX)"
                + " (IP) (TR) (BY)", UPPER));
        assertEquals("QVPQSOKOILPUBKJZPISFXDW",
                machine4.convert("FROMHISSHOULDERHIAWATHA"));
        assertEquals("BHCNSCXNUOAATZXSRCFYDGU",
                machine4.convert("TOOKTHECAMERAOFROSEWOOD"));
        assertEquals("FLPNXGXIXTYJUJRCAUGEUNCFMKUF",
                machine4.convert("MADEOFSLIDINGFOLDINGROSEWOOD"));
        assertEquals("WJFGKCIIRGXODJGVCGPQOH",
                machine4.convert("NEATLYPUTITALLTOGETHER"));
        assertEquals("ALWEBUHTZMOXIIVXUEFPRPR",
                machine4.convert("INITSCASEITLAYCOMPACTLY"));
        assertEquals("KCGVPFPYKIKITLBURVGTSFU",
                machine4.convert("FOLDEDINTONEARLYNOTHING"));
        assertEquals("SMBNKFRIIMPDOFJVTTUGRZM",
                machine4.convert("BUTHEOPENEDOUTTHEHINGES"));
        assertEquals("UVCYLFDZPGIBXREWXUEBZQJOYMHIPGRRE",
                machine4.convert("PUSHEDANDPULLEDTHEJOINTSANDHINGES"));
        assertEquals("GOHETUXDTWLCMMWAVNVJVHOUFANTQACK",
                machine4.convert("TILLITLOOKEDALLSQUARESANDOBLONGS"));
    }

    @Test
    public void checkMachine5() {
        machine5 = new Machine(UPPER, 5, 3, allRotor1);
        String[] rotorname = {"B", "BETA", "III", "IV", "I"};
        machine5.insertRotors(rotorname);
        machine5.setRotors("AXLE");
        machine5.setPlugboard(new Permutation("(HQ) (EX)"
                + " (IP) (TR) (BY)", UPPER));
        assertEquals("FROMHISSHOULDERHIAWATHA",
                machine5.convert("QVPQSOKOILPUBKJZPISFXDW"));
        assertEquals("TOOKTHECAMERAOFROSEWOOD",
                machine5.convert("BHCNSCXNUOAATZXSRCFYDGU"));
        assertEquals("MADEOFSLIDINGFOLDINGROSEWOOD",
                machine5.convert("FLPNXGXIXTYJUJRCAUGEUNCFMKUF"));
        assertEquals("NEATLYPUTITALLTOGETHER",
                machine5.convert("WJFGKCIIRGXODJGVCGPQOH"));
        assertEquals("INITSCASEITLAYCOMPACTLY",
                machine5.convert("ALWEBUHTZMOXIIVXUEFPRPR"));
        assertEquals("FOLDEDINTONEARLYNOTHING",
                machine5.convert("KCGVPFPYKIKITLBURVGTSFU"));
        assertEquals("BUTHEOPENEDOUTTHEHINGES",
                machine5.convert("SMBNKFRIIMPDOFJVTTUGRZM"));
        assertEquals("PUSHEDANDPULLEDTHEJOINTSANDHINGES",
                machine5.convert("UVCYLFDZPGIBXREWXUEBZQJOYMHIPGRRE"));
        assertEquals("TILLITLOOKEDALLSQUARESANDOBLONGS",
                machine5.convert("GOHETUXDTWLCMMWAVNVJVHOUFANTQACK"));
    }

    @Test
    public void checkMachine6() {
        machine6 = new Machine(UPPER, 5, 3, allRotor1);
        String[] rotorname = {"B", "GAMMA", "II", "V", "VII"};
        machine6.insertRotors(rotorname);
        machine6.setRotors("AAAA");
        machine6.setPlugboard(new Permutation("(MT) (NF) (WH) (LB)", UPPER));
        assertEquals("FVURXRPWUWCSXJPNWYHBRQRHF",
                machine6.convert("THISISACHALLENGINGPROJECT"));
    }

    @Test
    public void testDoubleStep() {
        Alphabet ac = new CharacterRange('A', 'D');
        Rotor one = new Reflector("R1", new Permutation("(AC) (BD)", ac));
        Rotor two = new MovingRotor("R2", new Permutation("(ABCD)", ac), "C");
        Rotor three = new MovingRotor("R3", new Permutation("(ABCD)", ac), "C");
        Rotor four = new MovingRotor("R4", new Permutation("(ABCD)", ac), "C");
        String setting = "AAA";
        Rotor[] machineRotors = {one, two, three, four};
        String[] rotors = {"R1", "R2", "R3", "R4"};
        Machine mach = new Machine(ac, 4, 3,
                new ArrayList<>(Arrays.asList(machineRotors)));
        mach.insertRotors(rotors);
        mach.setRotors(setting);

        assertEquals("AAAA", getSetting(ac, machineRotors));
        mach.convert('a');
        mach.convert('a');
        mach.convert('a');
        mach.convert('a');
        assertEquals("AABA", getSetting(ac, machineRotors));
        mach.convert('a');
        mach.convert('a');
        mach.convert('a');
        mach.convert('a');
        assertEquals("ABDA", getSetting(ac, machineRotors));
        mach.convert('a');
        mach.convert('a');
        mach.convert('a');
        mach.convert('a');
        assertEquals("ABAA", getSetting(ac, machineRotors));
        mach.convert('a');
        mach.convert('a');
        mach.convert('a');
        mach.convert('a');
        mach.convert('a');
        mach.convert('a');
        mach.convert('a');
        assertEquals("ABCD", getSetting(ac, machineRotors));
        mach.convert('a');
        mach.convert('a');
        assertEquals("ACDB", getSetting(ac, machineRotors));
        mach.convert('a');
        mach.convert('a');
        mach.convert('a');
        mach.convert('a');
        mach.convert('a');
        assertEquals("ACAC", getSetting(ac, machineRotors));
        mach.convert('a');
        mach.convert('a');
        mach.convert('a');
        assertEquals("ACBB", getSetting(ac, machineRotors));
        mach.convert('a');
        mach.convert('a');
        assertEquals("ACCD", getSetting(ac, machineRotors));
    }

    /**
     * Helper method to get the String
     * representation of the current Rotor settings
     */
    private String getSetting(Alphabet alph, Rotor[] machineRotors) {
        String currSetting = "";
        for (Rotor r : machineRotors) {
            currSetting += alph.toChar(r.setting());
        }
        return currSetting;
    }
}
