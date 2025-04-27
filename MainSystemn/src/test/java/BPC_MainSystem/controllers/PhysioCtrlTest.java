package BPC_MainSystem.controllers;

import BPC_MainSystem.models.Physiotherapists;
import BPC_MainSystem.filehandlers.PhysiotherapistsFile;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PhysioCtrlTest {

    private List<Physiotherapists> originalPhysios;

    @BeforeAll
    public void setUpClass() {
        originalPhysios = PhysiotherapistsFile.readPhysio();
    }

    @AfterAll
    public void tearDownClass() {
        PhysiotherapistsFile.savePhysio(originalPhysios);
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testAllPhysiosReadNotNull() {
        List<Physiotherapists> physios = PhysiotherapistsFile.readPhysio();
        assertNotNull(physios, "The list of physios should not be null.");
    }

    @Test
    public void testGeneratePhysioIdIncrements() {
        int id1 = PhysioCtrl.generatePhysiotId();
        int id2 = PhysioCtrl.generatePhysiotId();
        assertTrue(id2 >= id1, "The new physio ID should be greater or equal to the previous ID.");
    }

    @Test
    public void testAddPhysio() {
        List<Physiotherapists> physiosBefore = PhysiotherapistsFile.readPhysio();
        int sizeBefore = physiosBefore.size();
        Physiotherapists newPhysio = new Physiotherapists(
                PhysioCtrl.generatePhysiotId(),
                "Test Physio",
                "123 Test Street",
                "0123456789",
                new java.util.HashMap<>()
        );
        physiosBefore.add(newPhysio);
        PhysiotherapistsFile.savePhysio(physiosBefore);

        List<Physiotherapists> physiosAfter = PhysiotherapistsFile.readPhysio();
        int sizeAfter = physiosAfter.size();

        assertEquals(sizeBefore + 1, sizeAfter, "Physio should be added successfully.");
    }

    @Test
    public void testDeletePhysio() {
        List<Physiotherapists> physios = PhysiotherapistsFile.readPhysio();
        int initialSize = physios.size();

        if (initialSize == 0) {
            Physiotherapists dummy = new Physiotherapists(
                    PhysioCtrl.generatePhysiotId(),
                    "Dummy Physio",
                    "Somewhere",
                    "9876543210",
                    new java.util.HashMap<>()
            );
            physios.add(dummy);
            PhysiotherapistsFile.savePhysio(physios);
        }
        List<Physiotherapists> updatedPhysios = PhysiotherapistsFile.readPhysio();
        Physiotherapists toDelete = updatedPhysios.get(updatedPhysios.size() - 1);
        updatedPhysios.remove(toDelete);
        PhysiotherapistsFile.savePhysio(updatedPhysios);

        List<Physiotherapists> finalList = PhysiotherapistsFile.readPhysio();
        assertEquals(initialSize - 1, finalList.size(), "Physio should be deleted successfully.");
    }
}
