package com.building_security_system.DataBaseAccess.Entities;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Builder
@Getter
@Document(collation = "users")
public class UserEntity {
    @Id
    private ObjectId id;
    String name;
    String email;
    String password;
}
