package com.building_security_system.models;

import com.building_security_system.db_access.entities.UserEntity;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class User {
    private long id;
    String name;
    String email;
    String password;
    List<Role> roles;

    public static UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.id)
                .name(user.name)
                .email(user.email)
                .password(user.password)
                .build();
    }

    public static User toModel(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .email(entity.getEmail())
                .password(entity.getPassword())
                .build();
    }

}
