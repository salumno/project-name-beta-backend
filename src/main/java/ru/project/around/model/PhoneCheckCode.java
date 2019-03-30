package ru.project.around.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@Entity
public class PhoneCheckCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationSessionId;

    private String phone;

    private String code;

    private Long expirationTime;

    @Enumerated(EnumType.STRING)
    private PhoneCheckCodeStatus status;

    private int codeCheckAttemptCount;
}
