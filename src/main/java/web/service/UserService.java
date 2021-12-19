package web.service;

import web.models.User;

import java.util.List;

public interface UserService {
    User addUser(User user);
    List<User> getAllUsers();
    void deleteUserById(Long id);
    void updateUser(Long id, User updateUser);;
    User findById (Long id);
    User getUserByUsername(String email);
}
