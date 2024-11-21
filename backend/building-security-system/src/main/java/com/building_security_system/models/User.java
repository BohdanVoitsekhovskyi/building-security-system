package com.building_security_system.models;

import com.building_security_system.db_access.entities.UserEntity;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class User {
    private long id;
    String firstname;
    String lastname;
    String email;
    String password;

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.id)
                .firstname(user.firstname)
                .lastname(user.lastname)
                .email(user.email)
                .password(user.password)
                .build();
    }

    public static User toModel(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .build();
    }
}