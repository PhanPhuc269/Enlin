package com.learn.enlin.api.permission.mapper;

import com.learn.enlin.api.permission.dto.request.PermissionRequest;
import com.learn.enlin.api.permission.dto.response.PermissionResponse;
import com.learn.enlin.api.permission.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
