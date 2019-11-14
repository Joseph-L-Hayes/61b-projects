package enigma;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import static enigma.TestUtils.*;

public class MachineTest {

    private Alphabet alpha = UPPER;
    private int numRotors = 5;
    private int numPawls = 3;
    private ArrayList<Rotor> allRotors = new ArrayList<Rotor>(numRotors);

    /**Permutation Set*/
    Permutation permI = new Permutation(NAVALA.get("I"), alpha);
    Permutation permII = new Permutation(NAVALA.get("II"), alpha);
    Permutation permIII = new Permutation(NAVALA.get("III"), alpha);
    Permutation permIV = new Permutation(NAVALA.get("IV"), alpha);
    Permutation permV = new Permutation(NAVALA.get("V"), alpha);
    Permutation permVI = new Permutation(NAVALA.get("VI"), alpha);
    Permutation permVII = new Permutation(NAVALA.get("VII"), alpha);
    Permutation permVIII = new Permutation(NAVALA.get("VIII"), alpha);
    Permutation permBeta = new Permutation(NAVALA.get("Beta"), alpha);
    Permutation permGamma = new Permutation(NAVALA.get("Gamma"), alpha);
    Permutation permB = new Permutation(NAVALA.get("B"), alpha);
    Permutation permC = new Permutation(NAVALA.get("C"), alpha);

    /**Rotor Set*/
    Rotor i = new MovingRotor("I", permI, "MQ");
    Rotor ii = new MovingRotor("II", permII, "ME");
    Rotor iii = new MovingRotor("III", permIII, "MV");
    Rotor iv = new MovingRotor("IV", permIV, "MJ");
    Rotor v = new MovingRotor("V", permV, "Z");
    Rotor vi = new MovingRotor("VI", permVI, "MZM");
    Rotor vii = new MovingRotor("VII", permVII, "MZM");
    Rotor viii = new MovingRotor("VIII", permVIII, "MZM");
    Rotor _beta = new Rotor("BETA", permBeta);
    Rotor _gamma = new Rotor("GAMMA", permGamma);
    Rotor _b = new Reflector("B", permB);
    Rotor _c = new Reflector("C", permC);

    public void setMachine(Machine enigma, String[] rotorSet, String setting,
                           Permutation plugBoard) {

        enigma.insertRotors(rotorSet);
        enigma.setRotors(setting);
        enigma.setPlugboard(plugBoard);
    }

    /**Use this if no plugboard is given*/
    public void setMachine(Machine enigma, String[] rotorSet, String setting) {

        enigma.insertRotors(rotorSet);
        enigma.setRotors(setting);
    }

    public void createRotorSet() {

        allRotors.add(i);
        allRotors.add(ii);
        allRotors.add(iii);
        allRotors.add(iv);
        allRotors.add(v);
        allRotors.add(vi);
        allRotors.add(vii);
        allRotors.add(viii);
        allRotors.add(_b);
        allRotors.add(_c);
        allRotors.add(_beta);
        allRotors.add(_gamma);
    }

    @Test
    public void machineExampleTest() {
        createRotorSet();
        Permutation testBoard = new Permutation("(YF) (ZH)", alpha);
        String[] rotorSetA = {"B", "BETA", "III", "IV", "I"};
        String setting = "AXLE";

        Machine enigma = new Machine(alpha, 5, 3, allRotors);
        setMachine(enigma, rotorSetA, setting, testBoard);

        assertEquals(5, enigma.numRotors());
        assertEquals(alpha.toInt('Z'), enigma.convert('Y'));
        assertEquals(alpha.toInt('S'), enigma.convert('Y'));
        assertEquals("JB", enigma.convert("YY"));
    }
    @Test
    public void machineTrivialOneTest() {
        createRotorSet();
        Permutation plugBoardTrivial = new Permutation("(AQ) (EP)", alpha);
        String[] rotorSetTrivial = {"B", "BETA", "I", "II", "III"};
        String settingTrivial = "AAAA";

        Machine enigmaTrivial = new Machine(alpha, 5, 3, allRotors);
        setMachine(enigmaTrivial, rotorSetTrivial, settingTrivial,
                plugBoardTrivial);

        assertEquals("HELLO WORLD", enigmaTrivial.convert("IHBDQ QMTQZ"));

    }

    @Test
    public void machineNoPlugTest() {
        createRotorSet();
        String[] rotorSet = {"B", "BETA", "I", "II", "III"};
        String setting = "AAAA";

        Machine enigmaNoPlug = new Machine(alpha, 5, 3, allRotors);
        setMachine(enigmaNoPlug, rotorSet, setting);
        assertEquals("ILBDA AMTAZ", enigmaNoPlug.convert("HELLO WORLD"));
    }

    @Test
    public void machineNavalCipher() {
        createRotorSet();
        Permutation testBoard = new Permutation("(HQ) (EX) (IP) (TR) (BY)",
                alpha);
        String[] rotorSetA = {"B", "BETA", "III", "IV", "I"};
        String setting = "AXLE";

        Machine enigmaHiawatha = new Machine(alpha, 5, 3, allRotors);
        setMachine(enigmaHiawatha, rotorSetA, setting, testBoard);

        assertEquals("QVPQS OKOIL PUBKJ ZPISF XDW",
                enigmaHiawatha.convert("FROM his shoulder Hiawatha"));
        assertEquals("BHCNS CXNUO AATZX SRCFY DGU",
                enigmaHiawatha.convert("Took the camera of rosewood"));
        assertEquals("FLPNX GXIXT YJUJR CAUGE UNCFM KUF",
                enigmaHiawatha.convert("Made of sliding folding rosewood "));
        assertEquals("WJFGK CIIRG XODJG VCGPQ OH",
                enigmaHiawatha.convert("Neatly put it all together"));
        assertEquals("ALWEB UHTZM OXIIV XUEFP RPR",
                enigmaHiawatha.convert("In its case it lay compactly"));
        assertEquals("KCGVP FPYKI KITLB URVGT SFU",
                enigmaHiawatha.convert("Folded into nearly nothing "));
        assertEquals("SMBNK FRIIM PDOFJ VTTUG RZM",
                enigmaHiawatha.convert("But he opened out the hinges"));
        assertEquals("UVCYL FDZPG IBXRE WXUEB ZQJO",
                enigmaHiawatha.convert("Pushed and pulled the joints"));
        assertEquals("YMHIP GRRE",
                enigmaHiawatha.convert("and hinges"));
        assertEquals("GOHET UXDTW LCMMW AVNVJ VH",
                enigmaHiawatha.convert("Till it looked all squares "));
        assertEquals("OUFAN TQACK",
                enigmaHiawatha.convert("and oblongs"));
        assertEquals("KTOZZ RDABQ NNVPO IEFQA FS",
                enigmaHiawatha.convert("Like a complicated figure"));
        assertEquals("VVICV UDUER EYNPF FMNBJ VGQ",
                enigmaHiawatha.convert("in the Second Book of Euclid"));
        createRotorSet();
        testBoard = new Permutation("(HQ) (EX) (IP) (TR) (BY)", alpha);
        String[] rotorSetB = {"B", "BETA", "III", "IV", "I"};
        setting = "AXLE";

        enigmaHiawatha = new Machine(alpha, 5, 3, allRotors);
        setMachine(enigmaHiawatha, rotorSetB, setting, testBoard);

        assertEquals("FROMH ISSHO ULDER HIAWA THA",
                enigmaHiawatha.convert("QVPQS OKOIL PUBKJ ZPISF XDW"));
    }

    @Test
    public void carrollTest() {

        createRotorSet();
        String[] rotorSetA = {"B", "BETA", "III", "IV", "I"};
        String setting = "AXLE";

        Machine enigmaCarrol1 = new Machine(alpha, 5, 3, allRotors);
        setMachine(enigmaCarrol1, rotorSetA, setting);

        assertEquals("FROMH ISSHO ULDER HIAWA THA",
                enigmaCarrol1.convert("HYIHL BKOML IUYDC MPPSF SZW"));
        assertEquals("TOOKT HECAM ERAOF ROSEW OOD",
                enigmaCarrol1.convert("SQCNJ EXNUO JYRZE KTCNB DGU"));
        assertEquals("MADEO FSLID INGFO LDING ROSEW OOD",
                enigmaCarrol1.convert("FLIIE GEPGR SJUJT CALGX SNCTM KUF"));
    }

    @Test
    public void pawlTest() {
        createRotorSet();
        String[] rotorSetA = {"B", "BETA", "III", "II", "IV", "I"};
        String setting = "AXLE";

        Machine enigmaCarrol2 = new Machine(alpha, 5, 3, allRotors);
        setMachine(enigmaCarrol2, rotorSetA, setting);
    }

}
