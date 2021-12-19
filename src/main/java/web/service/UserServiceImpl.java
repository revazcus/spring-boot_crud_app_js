package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.UserDao;
import web.models.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public User addUser(User user) {
        return userDao.save(user);
    }

    @Override
    @Transactional (readOnly = true)
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userDao.deleteById(id);
    }

    @Override
    @Transactional
    public void updateUser(Long id, User updateUser){
        userDao.saveAndFlush(updateUser);
    }

    @Override
    @Transactional (readOnly = true)
    public User findById(Long id) {
        return userDao.getById(id);
    }

    @Override
    public User getUserByUsername(String email) {
        return userDao.getUserByUsername(email);
    }

}
