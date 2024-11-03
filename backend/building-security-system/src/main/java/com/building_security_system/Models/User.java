package com.building_security_system.Models;

import com.building_security_system.DataBaseAccess.Entities.UserEntity;
import lombok.Builder;
import org.bson.types.ObjectId;

@Builder
public class User {
    private ObjectId id;
    String name;
    String email;
    String password;

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
