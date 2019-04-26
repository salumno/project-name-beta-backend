package ru.project.around.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.project.around.model.status.PhoneCheckCodeStatus;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
