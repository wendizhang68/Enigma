package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/**
 * The suite of all JUnit tests for the Permutation class.
 *
 * @author Wendi Zhang
 */
public class PermutationTest {

    /**
     * Testing time limit.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm, perm1, perm2, perm3;
    private String alpha = UPPER_STRING;

    /**
     * Check that perm has an alphabet whose size is that of
     * FROMALPHA and TOALPHA and that maps each character of
     * FROMALPHA to the corresponding character of FROMALPHA, and
     * vice-versa. TESTID is used in error messages.
     */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, perm.invert(ei));
        }
    }

    private void checkPerm1(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm1.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm1.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, perm1.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm1.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, perm1.invert(ei));
        }
    }

    private void checkPerm2(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm2.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm2.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, perm2.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm2.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, perm2.invert(ei));
        }
    }

    private void checkPerm3(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm3.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                    e, perm3.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                    c, perm3.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                    ei, perm3.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                    ci, perm3.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
        perm1 = new Permutation("(AD)(FG)(YH)", UPPER);
        checkPerm1("perm1check", UPPER_STRING, TEST1_STRING);
        perm2 = new Permutation("(ACV)(FGRE)", UPPER);
        checkPerm2("perm2check", UPPER_STRING, TEST2_STRING);
        perm3 = new Permutation("(B)(AGRE)", UPPER);
        checkPerm3("perm2check", UPPER_STRING, TEST3_STRING);
    }

    @Test
    public void testInvertChar() {
        Permutation p = new Permutation("(PNH) (ABDFIKLZYXW)(JC)",
                new CharacterRange('A', 'Z'));
        assertEquals(p.invert('B'), 'A');
        assertEquals(p.invert('G'), 'G');
        assertEquals(p.invert('C'), 'J');
        assertEquals(p.invert('J'), 'C');
        assertEquals(p.invert('P'), 'H');
        assertEquals(p.invert('Y'), 'Z');
    }

    @Test
    public void testInvertChar2() {
        Permutation p = new Permutation("(QWERTYU)(ASDFGH)(ZXC)(JKL)",
                new CharacterRange('A', 'Z'));
        assertEquals(p.invert('E'), 'W');
        assertEquals(p.invert('L'), 'K');
        assertEquals(p.invert('J'), 'L');
        assertEquals(p.invert('P'), 'P');
        assertEquals(p.invert('Q'), 'U');
        assertEquals('H', p.invert('A'));
    }

    @Test
    public void testPermuteChar() {
        Permutation p = new Permutation("(PNH)(ABDFIKLZYXW) (JC)",
                new CharacterRange('A', 'Z'));
        assertEquals(p.permute('D'), 'F');
        assertEquals(p.permute('W'), 'A');
        assertEquals(p.permute('C'), 'J');
        assertEquals(p.permute('J'), 'C');
        assertEquals(p.permute('O'), 'O');
        assertEquals(p.permute('H'), 'P');
    }

    @Test
    public void testDerangement() {
        Permutation p1 = new Permutation("(PNH)(ABDFIKLZYXW) (JC)",
                new CharacterRange('A', 'Z'));
        assertEquals(false, p1.derangement());
        Permutation p2 = new Permutation("(ABCED)",
                new CharacterRange('A', 'E'));
        assertEquals(true, p2.derangement());
        Permutation p3 = new Permutation("(ABCED)(F)(G)",
                new CharacterRange('A', 'G'));
        assertEquals(false, p3.derangement());
        Permutation p4 = new Permutation("(ABCED)(FG)(HK)(IJL)",
                new CharacterRange('A', 'L'));
        assertEquals(true, p4.derangement());
    }
}
