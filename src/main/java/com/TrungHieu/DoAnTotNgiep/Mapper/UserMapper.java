package com.TrungHieu.DoAnTotNgiep.Mapper;

import com.TrungHieu.DoAnTotNgiep.DTO.Response.StudentResponse;
import com.TrungHieu.DoAnTotNgiep.DTO.Response.UserResponse;
import com.TrungHieu.DoAnTotNgiep.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", source = "role")
    @Mapping(target = "idClass", source = "studentClass.id")
    public UserResponse toResponse(User user);

    public List<UserResponse> toResponseList(List<User> users);

    @Mapping(target = "idClass", source = "studentClass.id")
    public StudentResponse toStudentResponse(User user);



}
