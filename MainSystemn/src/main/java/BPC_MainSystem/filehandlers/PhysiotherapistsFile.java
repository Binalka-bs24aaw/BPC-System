package BPC_MainSystem.filehandlers;

import BPC_MainSystem.models.Physiotherapists;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PhysiotherapistsFile {

    private static final String Phy_FILE_PATH = "src/main/java/BPC_MainSystem/files/physiotherapists.json";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Physiotherapists> readPhysio() {
        File file = new File(Phy_FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file, new TypeReference<List<Physiotherapists>>() {});
        } catch (IOException e) {
            System.out.println("Error reading physiotherapists: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static boolean savePhysio(List<Physiotherapists> physios) {
        try {
            objectMapper.writeValue(new File(Phy_FILE_PATH), physios);
            return true;
        } catch (IOException e) {
            System.out.println("Error saving" + e.getMessage());
            return false;
        }
    }


}
