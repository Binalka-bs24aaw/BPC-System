package BPC_MainSystem.filehandlers;

import BPC_MainSystem.models.Timetable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author binalkaamarajeewa
 */
public class TimetableFileTest {

    private static final String TEST_FILE_PATH = "src/main/java/BPC_MainSystem/files/Timetable.json";
    private List<Timetable> originalTimetable;

    @BeforeEach
    public void setUp() throws IOException {
        // Backup original file content
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            originalTimetable = new ObjectMapper().readValue(file, new TypeReference<List<Timetable>>() {});
        } else {
            originalTimetable = new ArrayList<>();
        }
    }

    @AfterEach
    public void tearDown() throws IOException {
        // Restore original file content
        ObjectMapper mapper = new ObjectMapper();
        FileWriter writer = new FileWriter(TEST_FILE_PATH);
        mapper.writeValue(writer, originalTimetable);
        writer.close();
    }

    @Test
    public void testReadTimetable() throws IOException {
        List<Timetable> dummyData = new ArrayList<>();
        dummyData.add(new Timetable());

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(TEST_FILE_PATH), dummyData);
        List<Timetable> readData = TimetableFile.readTimetable();
        assertNotNull(readData, "The returned list should not be null");
        assertEquals(1, readData.size(), "There should be exactly one Timetable entry");
        assertNotNull(readData.get(0), "The first Timetable object should not be null");
    }
}
