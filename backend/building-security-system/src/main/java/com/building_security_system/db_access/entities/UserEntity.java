package com.building_security_system.db_access.entities;


import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



@Builder
@Getter
@ToString
@Document(collation = "users")
public class UserEntity  {
    @Id
    private ObjectId id;
    String name;
    String email;
    String password;
}
