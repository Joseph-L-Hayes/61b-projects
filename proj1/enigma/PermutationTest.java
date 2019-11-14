package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author josephhayes
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
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

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkPermutation() {
        String rotorI = "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)";
        String rotorII = "(FIXVYOMW) (CDKLHUP) (ESZ) (BJ) (GR) (NT) (A) (Q)";
        String rotorIII = "(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)";
        String rotorIV = "(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)";
        String rotorV = "(AVOLDRWFIUQ)(BZKSMNHYC) (EGTJPX)";
        String rotorVI = "(AJQDVLEOZWIYTS) (CGMNHFUX) (BPRK)";
        String rotorVII = "(ANOUPFRIMBZTLWKSVEGCJYDHXQ)";
        String rotorVIII = "(AFLSETWUNDHOZVICQ) (BKJ) (GXY) (MPR)";
        String rotorBeta = "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)";
        String rotorGamma = "(AFNIRLBSQWVXGUZDKMTPCOYJHE)";
        String reflectorB = "(AE) (BN) (CK) (DQ) (FU) (GY) (HW) "
                + "(IJ) (LO) (MP) (RX) (SZ) (TV)";
        String reflectorC = "(AR) (BD) (CO) (EJ) (FN) (GT) (HK) "
                + "(IV) (LM) (PW) (QZ) (SX) (UY)";

        Permutation permI = new Permutation(rotorI, UPPER);

        assertEquals('E', permI.permute('A'));
        assertEquals('C', permI.permute('Y'));
        assertEquals('S', permI.permute('S'));
        assertEquals('A', permI.permute('U'));
        assertEquals('V', permI.permute('I'));
        assertEquals('I', permI.invert('V'));
        assertEquals('J', permI.invert('Z'));
        assertEquals('F', permI.invert('G'));
        assertEquals('G', permI.invert('D'));
        assertEquals(true, permI.derangement());

        Permutation permII = new Permutation(rotorII, UPPER);

        assertEquals('D', permII.permute('C'));
        assertEquals('Q', permII.permute('Q'));
        assertEquals('E', permII.permute('Z'));
        assertEquals('H', permII.permute('L'));
        assertEquals('X', permII.permute('I'));
        assertEquals('A', permII.invert('A'));
        assertEquals('A', permII.invert('A'));
        assertEquals('R', permII.invert('G'));
        assertEquals('D', permII.invert('K'));
        assertEquals(true, permII.derangement());
        Permutation permReflectB = new Permutation(reflectorB, UPPER);
        assertEquals(false, permReflectB.derangement());
        assertEquals('Q', permReflectB.permute('D'));
        assertFalse(permReflectB.permute('A') == 'V');
    }

}
