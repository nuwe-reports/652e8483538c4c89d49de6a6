
package com.example.demo;

import com.example.demo.controllers.DoctorController;
import com.example.demo.controllers.PatientController;
import com.example.demo.controllers.RoomController;
import com.example.demo.entities.Doctor;
import com.example.demo.entities.Patient;
import com.example.demo.entities.Room;
import com.example.demo.repositories.DoctorRepository;
import com.example.demo.repositories.PatientRepository;
import com.example.demo.repositories.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest {


    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllDoctors() throws Exception {
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        List<Doctor> doctorList = new ArrayList<Doctor>();
        doctorList.add(doctor);
        Assertions.assertEquals(1, doctorList.size());
        when(doctorRepository.findAll()).thenReturn(doctorList);
        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotGetAllDoctors() throws Exception {
        List<Doctor> doctorList = new ArrayList<Doctor>();
        when(doctorRepository.findAll()).thenReturn(doctorList);
        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetDoctor() throws Exception {
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        doctor.setId(21);
        Optional<Doctor> opt = Optional.of(doctor);
        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(doctor.getId());
        assertThat(doctor.getId()).isEqualTo(21);

        when(doctorRepository.findById(doctor.getId())).thenReturn(opt);
        mockMvc.perform(get("/api/doctors/" + doctor.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotGetDoctor() throws Exception {
        long id = 21;
        mockMvc.perform(get("/api/doctors/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateDoctor() throws Exception {
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");

        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldNotDeleteDoctor() throws Exception {
        long id = 31;
        mockMvc.perform(delete("/api/doctors/" + id))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldDeleteAllDoctors() throws Exception {
        long id = 31;
        mockMvc.perform(delete("/api/doctors/"))
                .andExpect(status().isOk());

    }

    @Test
    void shouldDeleteDoctor() throws Exception {
        long id = 21;
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        doctor.setId(id);

        Optional<Doctor> opt = Optional.of(doctor);
        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(doctor.getId());
        assertThat(doctor.getId()).isEqualTo(21);

        when(doctorRepository.findById(doctor.getId())).thenReturn(opt);
        mockMvc.perform(delete("/api/doctors/" + doctor.getId()))
                .andExpect(status().isOk());

    }
}


@WebMvcTest(PatientController.class)
class PatientControllerUnitTest {

    @MockBean
    private PatientRepository patientRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    final private String url = "/api/patients";

    @Test
    void shouldGetAllPatients() throws Exception {
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        List<Patient> patients = new ArrayList<>();
        patients.add(patient);
        Assertions.assertEquals(1, patients.size());
        when(patientRepository.findAll()).thenReturn(patients);
        mockMvc.perform(get(this.url))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotGetAllPatients() throws Exception {
        List<Patient> patients = new ArrayList<Patient>();
        when(patientRepository.findAll()).thenReturn(patients);
        mockMvc.perform(get(this.url))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetPatient() throws Exception {
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        patient.setId(21);
        Optional<Patient> opt = Optional.of(patient);
        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(patient.getId());
        assertThat(patient.getId()).isEqualTo(21);

        when(patientRepository.findById(patient.getId())).thenReturn(opt);
        mockMvc.perform(get(this.url + "/" + patient.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotGetPatient() throws Exception {
        long id = 21;
        mockMvc.perform(get(this.url + "/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreatePatient() throws Exception {
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");

        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldNotDeletePatient() throws Exception {
        long id = 31;
        mockMvc.perform(delete(this.url + "/" + id))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldDeleteAllPatients() throws Exception {
        mockMvc.perform(delete(this.url))
                .andExpect(status().isOk());

    }

    @Test
    void shouldDeletePatient() throws Exception {
        long id = 21;
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        patient.setId(id);

        Optional<Patient> opt = Optional.of(patient);
        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(patient.getId());
        assertThat(patient.getId()).isEqualTo(21);

        when(patientRepository.findById(patient.getId())).thenReturn(opt);
        mockMvc.perform(delete(this.url + "/" + patient.getId()))
                .andExpect(status().isOk());

    }

}

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest {

    @MockBean
    private RoomRepository roomRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    final private String url = "/api/rooms";


    @Test
    void shouldGetAllRooms() throws Exception {

        Room room = new Room("Dermatology");
        List<Room> rooms = new ArrayList<>();
        rooms.add(room);
        Assertions.assertEquals(1, rooms.size());
        when(roomRepository.findAll()).thenReturn(rooms);
        mockMvc.perform(get(this.url))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotGetAllRooms() throws Exception {
        List<Room> rooms = new ArrayList<>();
        when(roomRepository.findAll()).thenReturn(rooms);
        mockMvc.perform(get(this.url))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetRoom() throws Exception {
        Room room = new Room("Dermatology");

        Optional<Room> opt = Optional.of(room);
        assertThat(opt).isPresent();
        assertThat(opt.get().getRoomName()).isEqualTo(room.getRoomName());
        assertThat(room.getRoomName()).isEqualTo("Dermatology");

        when(roomRepository.findByRoomName(room.getRoomName())).thenReturn(opt);
        mockMvc.perform(get(this.url + "/" + room.getRoomName()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotGetRoom() throws Exception {
        long id = 21;
        mockMvc.perform(get(this.url + "/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateRoom() throws Exception {
        Room room = new Room("Dermatology");

        mockMvc.perform(post("/api/room").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldNotDeleteRoom() throws Exception {
        final String roomName = "Dermatologys";
        mockMvc.perform(delete(this.url + "/" + roomName))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldDeleteAllRooms() throws Exception {
        mockMvc.perform(delete(this.url))
                .andExpect(status().isOk());

    }

    @Test
    void shouldDeleteRoom() throws Exception {

        Room room = new Room("Dermatology");


        Optional<Room> opt = Optional.of(room);
        assertThat(opt).isPresent();
        assertThat(opt.get().getRoomName()).isEqualTo(room.getRoomName());
        assertThat(room.getRoomName()).isEqualTo("Dermatology");

        when(roomRepository.findByRoomName(room.getRoomName())).thenReturn(opt);
        mockMvc.perform(delete(this.url + "/" + room.getRoomName()))
                .andExpect(status().isOk());

    }

}
