package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.models.Role;
import web.models.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
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
    public String showAllUsers (Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users_all";
    }

    @GetMapping("/new")
    public String createUserForm(Model model){
        model.addAttribute("user", new User());
        return "user_create";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam(required = false) String roleAdmin,
                             @RequestParam(required = false) String roleUser){
        Set<Role> roleSet = new HashSet<>();
        Role admins = roleService.getRoleByRoleName("ADMIN");
        Role users = roleService.getRoleByRoleName("USER");
        if (admins.toString().equals(roleAdmin)){
            roleSet.add(admins);
        } if (users.toString().equals(roleUser)){
            roleSet.add(users);
        } if (roleAdmin != null && roleUser != null) {
            roleSet.add(admins);
            roleSet.add(users);
        } if (roleAdmin == null && roleUser == null) {
            roleSet.add(users);
        }
        user.setRoles(roleSet);
        userService.addUser(user);
        return "redirect:/admin/";
    }

    @DeleteMapping("/user_delete/{id}")
    public String deleteUser(@PathVariable("id") long id){
        userService.deleteUserById(id);
        return "redirect:/admin/";
    }

    @GetMapping("/edit/{id}")
    public String updateUserForm(@PathVariable("id") long id, Model model){
        User user = userService.findById(id);
        Set<Role> roleSet = user.getRoles();
        for (Role role : roleSet) {
            if (role.toString().equals("ADMIN")){
                model.addAttribute("roleAdmin", true);
            }
            if (role.toString().equals("USER")){
                model.addAttribute("roleUser", true);
            }
        }
        model.addAttribute("user", user);
        return "user_update";
    }

    @PutMapping("/edit/{id}")
    public String updateUser(@ModelAttribute ("user") User user,
                             @PathVariable("id") long id,
                             @RequestParam(required = false) String roleAdmin,
                             @RequestParam(required = false) String roleUser) {
        Set<Role> roleSet = new HashSet<>();
        Role admins = roleService.getRoleByRoleName("ADMIN");
        Role users = roleService.getRoleByRoleName("USER");
        if (admins.toString().equals(roleAdmin)){
            roleSet.add(admins);
        } if (users.toString().equals(roleUser)){
            roleSet.add(users);
        } if (roleAdmin != null && roleUser != null) {
            roleSet.add(admins);
            roleSet.add(users);
        } if (roleAdmin == null && roleUser == null) {
            roleSet.add(users);
        }
        user.setRoles(roleSet);
        userService.updateUser(id, user);
        return "redirect:/admin/";
    }

    @GetMapping("/{id}")
    public String userPage(@PathVariable("id") long id, Model model){
        model.addAttribute("user", userService.findById(id));
        return "user_page";
    }
}
