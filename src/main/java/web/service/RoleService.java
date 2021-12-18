package web.service;

import web.models.Role;

public interface RoleService {
    Role addRole(Role role);
    Role getRoleByRoleName(String role);
}
