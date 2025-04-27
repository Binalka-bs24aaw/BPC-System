/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package BPC_MainSystem.controllers;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import BPC_MainSystem.models.Patient;
import BPC_MainSystem.filehandlers.PatientFile;

/**
 *
 * @author binalkaamarajeewa
 */
public class PatientCtrlTest {

    private List<Patient> originalPatients;

    public PatientCtrlTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        System.out.println("Starting PatientCtrl Tests...");
    }

    @AfterAll
    public static void tearDownClass() {
        System.out.println("Finished PatientCtrl Tests.");
    }

    @BeforeEach
    public void setUp() {
        originalPatients = new ArrayList<>(PatientFile.readPatientstxt());
    }

    @AfterEach
    public void tearDown() {
        PatientFile.saveAllPatients((ArrayList<Patient>) originalPatients);
    }

    @Test
    public void testReadAllPatients() {
        List<Patient> patients = PatientFile.readPatientstxt();
        assertNotNull(patients, "Patient list should not be null");
    }

    @Test
    public void testAddPatient() {
        int initialSize = PatientFile.readPatientstxt().size();

        Patient newPatient = new Patient(
                PatientCtrl.generatePatientId(),
                "Binki Amagajeewa",
                "140 Mosquito way",
                "0123456789"
        );

        boolean saved = PatientFile.savePatient(newPatient);
        assertTrue(saved, "New patient should be saved successfully.");

        List<Patient> updatedPatients = PatientFile.readPatientstxt();
        assertEquals(initialSize + 1, updatedPatients.size(), "Patient list size should increase by 1 after adding.");
    }

    @Test
    public void testGeneratePatientId() {
        int generatedId = PatientCtrl.generatePatientId();
        List<Patient> patients = PatientFile.readPatientstxt();

        boolean idExists = patients.stream().anyMatch(patient -> patient.getId() == generatedId);
        assertFalse(idExists, "Generated Patient ID should not already exist.");
    }


    

}
