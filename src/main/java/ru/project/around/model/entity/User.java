package ru.project.around.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.project.around.model.status.UserPremiumStatus;
import ru.project.around.model.status.UserStatus;

import javax.persistence.*;

@Data
@Table(name = "\"user\"")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;

    private String hashPassword;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Enumerated(EnumType.STRING)
    private UserPremiumStatus premiumStatus;
}
