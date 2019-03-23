package ru.project.around.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("\"user\"")
public class User {
    @Id
    private Long id;
    private String email;
    private String phone;
    private String hashPassword;
}
