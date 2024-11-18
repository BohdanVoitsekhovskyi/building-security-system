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
    String username;
    String password;
    List<Role> roles;

    public static UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        return UserEntity.builder()
                .id(user.id)
                .name(user.name)
                .username(user.username)
                .password(user.password)
                .build();
    }

    public static User toModel(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .build();
    }

}
