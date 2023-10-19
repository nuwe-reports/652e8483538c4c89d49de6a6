package com.example.demo.controllers;

import com.example.demo.repositories.*;
import com.example.demo.entities.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    AppointmentRepository appointmentRepository;


    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();

        appointmentRepository.findAll().forEach(appointments::add);

        if (appointments.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable("id") long id) {
        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (appointment.isPresent()) {
            return new ResponseEntity<>(appointment.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = {"/appointments", "/appointment"})
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {

        if ((appointment.getPatient() == null) || (appointment.getRoom() == null) || (appointment.getDoctor() == null) || (appointment.getStartsAt() == null) || (appointment.getFinishesAt() == null)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check your data,possible invalid payload");
        }


        Patient patient = new Patient(
                appointment.getPatient().getFirstName(),
                appointment.getPatient().getLastName(),
                appointment.getPatient().getAge(),
                appointment.getPatient().getEmail()
        );

        Doctor doctor = new Doctor(
                appointment.getDoctor().getFirstName(),
                appointment.getDoctor().getLastName(),
                appointment.getDoctor().getAge(),
                appointment.getDoctor().getEmail()
        );

        Room room = new Room(appointment.getRoom().getRoomName());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        final String startsAta = appointment.getStartsAt().format(formatter);
        final String finishesAta = appointment.getFinishesAt().format(formatter);

        LocalDateTime startsAt = LocalDateTime.parse(startsAta, formatter);
        LocalDateTime finishesAt = LocalDateTime.parse(finishesAta, formatter);


        if (startsAt.isEqual(finishesAt)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startsAt and finishesAt ,cannot be the same");
        }


        Appointment appointments = new Appointment(patient, doctor, room, startsAt, finishesAt);
        final List<Appointment> appointmentList = appointmentRepository.findAll();

        for (Appointment appointmentLoop : appointmentList) {
            if (appointmentLoop.overlaps(appointments)) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Overlap appointments");
            }
        }

        Appointment appointmentSaved = appointmentRepository.save(appointments);
        return new ResponseEntity<>(appointmentSaved, HttpStatus.OK);


    }


    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<HttpStatus> deleteAppointment(@PathVariable("id") long id) {

        Optional<Appointment> appointment = appointmentRepository.findById(id);

        if (!appointment.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        appointmentRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping("/appointments")
    public ResponseEntity<HttpStatus> deleteAllAppointments() {
        appointmentRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
