package com.building_security_system.db_access.entities;

import com.building_security_system.models.Role;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Document(collection = "users")
public class UserEntity  {
    @Id
    private long id;
    String name;
    String email;
    String password;
    List<Role> roles;
}
