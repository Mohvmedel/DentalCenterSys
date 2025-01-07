package com.dentalcenter.patient.dto;

import com.dentalcenter.patient.model.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientResponse {
    private int patientId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String phone;
    private String email;
    private String address;

}
