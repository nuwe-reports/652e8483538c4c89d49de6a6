package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    private Doctor d1;

    private Patient p1;

    private Room r1;

    private Appointment a1;
    private Appointment a2;
    private Appointment a3;

    @BeforeEach
    public void setUp() {
        d1 = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        p1 = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        r1 = new Room("Dermatology");
    }

    @Test
    void should_create_doctor() {
        Assertions.assertEquals("Perla", d1.getFirstName());
        Assertions.assertEquals("Amalia", d1.getLastName());
        Assertions.assertEquals(24, d1.getAge());
        Assertions.assertEquals("p.amalia@hospital.accwe", d1.getEmail());
    }

    @Test
    void should_create_doctor_with_id() {
        long id = 31;
        d1.setId(id);
        Assertions.assertEquals(31, d1.getId());
    }

    @Test
    void should_create_patient() {
        Assertions.assertEquals("Jose Luis", p1.getFirstName());
        Assertions.assertEquals("Olaya", p1.getLastName());
        Assertions.assertEquals(37, p1.getAge());
        Assertions.assertEquals("j.olaya@email.com", p1.getEmail());
    }

    @Test
    void should_create_patient_with_id() {
        long id = 31;
        p1.setId(id);
        Assertions.assertEquals(31, p1.getId());
    }

    @Test
    void should_create_room() {
        Assertions.assertEquals("Dermatology", r1.getRoomName());
    }

    @Test
    void should_create_appointment_with_doctor() {
        a1 = new Appointment();
        a1.setDoctor(d1);

        Assertions.assertEquals("Perla", a1.getDoctor().getFirstName());
        Assertions.assertEquals("Amalia", a1.getDoctor().getLastName());
        Assertions.assertEquals(24, a1.getDoctor().getAge());
        Assertions.assertEquals("p.amalia@hospital.accwe", a1.getDoctor().getEmail());
        assertNotNull(a1.getDoctor());
    }

    @Test
    void should_create_appointment_with_patient() {
        a2 = new Appointment();
        a2.setPatient(p1);

        Assertions.assertEquals("Jose Luis", a2.getPatient().getFirstName());
        Assertions.assertEquals("Olaya", a2.getPatient().getLastName());
        Assertions.assertEquals(37, a2.getPatient().getAge());
        Assertions.assertEquals("j.olaya@email.com", a2.getPatient().getEmail());
        assertNotNull(a2.getPatient());
    }

    @Test
    void should_create_appointment_with_room() {
        a3 = new Appointment();
        a3.setRoom(r1);

        Assertions.assertEquals("Dermatology", a3.getRoom().getRoomName());
        assertNotNull(a3.getRoom());
    }

    @Test
    void should_create_appointment_with_dates() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        LocalDateTime startsAt = LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        Appointment appointment = new Appointment(p1, d1, r1, startsAt, finishesAt);
        Assertions.assertEquals("2023-04-24T19:30", appointment.getStartsAt().toString());
        Assertions.assertEquals("2023-04-24T20:30", appointment.getFinishesAt().toString());

    }

    @Test
    void should_create_appointment_with_id() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        LocalDateTime startsAt = LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);
        long id = 32;

        Appointment appointment = new Appointment(p1, d1, r1, startsAt, finishesAt);
        appointment.setId(id);
        Assertions.assertEquals(id, appointment.getId());


    }

    @Test
    void should_create_appointment_with_overlaps() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        LocalDateTime startsAt = LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        Appointment appointment = new Appointment(p1, d1, r1, startsAt, finishesAt);
        Appointment appointment2 = new Appointment(p1, d1, r1, startsAt, finishesAt);

        Assertions.assertTrue(appointment.overlaps(appointment2));


    }

    @Test
    void should_create_appointment_with_with_overlaps() {

        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Doctor doctor = new Doctor("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Room room = new Room("Dermatology");
        Room room2 = new Room("Dermatologys");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");


        Appointment appointment = new Appointment(
                patient,
                doctor,
                room,
                LocalDateTime.parse("09:00 24/04/2023", formatter),
                LocalDateTime.parse("11:00 24/04/2023", formatter)
        );

        Appointment appointment2 = new Appointment(
                patient,
                doctor,
                room,
                LocalDateTime.parse("09:00 24/04/2023", formatter),
                LocalDateTime.parse("10:30 24/04/2023", formatter)
        );

        Appointment appointment3 = new Appointment(
                patient,
                doctor,
                room,
                LocalDateTime.parse("10:00 24/04/2023", formatter),
                LocalDateTime.parse("11:00 24/04/2023", formatter)
        );

        Appointment appointment4 = new Appointment(
                patient,
                doctor,
                room,
                LocalDateTime.parse("08:30 24/04/2023", formatter),
                LocalDateTime.parse("10:00 24/04/2023", formatter)
        );

        Appointment appointment5 = new Appointment(
                patient,
                doctor,
                room,
                LocalDateTime.parse("10:00 24/04/2023", formatter),
                LocalDateTime.parse("12:00 24/04/2023", formatter)
        );

        Appointment appointment6 = new Appointment(
                patient,
                doctor,
                room2,
                LocalDateTime.parse("09:00 24/04/2023", formatter),
                LocalDateTime.parse("10:00 24/04/2023", formatter)
        );

        //  CASE 1
        Assertions.assertTrue(appointment.overlaps(appointment2));
        //  CASE 2
        Assertions.assertTrue(appointment.overlaps(appointment3));
        //  CASE 3
        Assertions.assertTrue(appointment.overlaps(appointment4));
        //  CASE 5
        Assertions.assertTrue(appointment.overlaps(appointment5));
        //  CASE 6
        Assertions.assertFalse(appointment.overlaps(appointment6));
    }

    @Test
    void should_create_appointment_with_not_overlaps() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        LocalDateTime startsAt = LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        Appointment appointment = new Appointment(p1, d1, r1, startsAt, finishesAt);
        Appointment appointment2 = new Appointment(p1, d1, r1, startsAt, finishesAt);

        Assertions.assertTrue(appointment.overlaps(appointment2));
    }

}
