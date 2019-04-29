package ru.project.around.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.project.around.model.status.EntryControlStatusEnum;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EntryControl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;

    private Integer registrationAttemptCount;

    @Enumerated(EnumType.STRING)
    private EntryControlStatusEnum status;
}
