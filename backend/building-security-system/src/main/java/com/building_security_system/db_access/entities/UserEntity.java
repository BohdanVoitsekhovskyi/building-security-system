package com.building_security_system.db_access.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Getter
@ToString
@Document(collection = "users")
public class UserEntity  {
    @Id
    private long id;
    String name;
    String email;
    String password;
}
