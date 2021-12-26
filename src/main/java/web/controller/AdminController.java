package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.models.Role;
import web.models.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public ModelAndView allUsers() {
        User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin");
        modelAndView.addObject("admin", admin);
        return modelAndView;
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> showAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<User> userPage (@PathVariable("id") long id){
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/api/users")
    public ResponseEntity<User> newUser(@RequestBody User user) {
        Set<Role> roleSet = new HashSet<>();
        Role adminRole = roleService.getRoleByRoleName("ADMIN");
        Role userRole = roleService.getRoleByRoleName("USER");
        if (user.getRoleSetTemp().length == 2) {
            roleSet.add(adminRole);
            roleSet.add(userRole);
        } if (user.getRoleSetTemp()[0].equals("ADMIN")){
            roleSet.add(adminRole);
        } else {
            roleSet.add(userRole);
        }
        user.setRoles(roleSet);
        userService.addUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/api/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        Set<Role> roleSet = new HashSet<>();
        Role adminRole = roleService.getRoleByRoleName("ADMIN");
        Role userRole = roleService.getRoleByRoleName("USER");
        if (user.getRoleSetTemp().length == 2) {
            roleSet.add(adminRole);
            roleSet.add(userRole);
        } if (user.getRoleSetTemp()[0].equals("ADMIN")){
            roleSet.add(adminRole);
        } else {
            roleSet.add(userRole);
        }
        user.setRoles(roleSet);
        userService.updateUser(user.getId(), user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/api/users/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") long id) {
        userService.deleteUserById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ModelAndView addressLineUserPage (@PathVariable("id") long id){
        User user = userService.findById(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user");
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
