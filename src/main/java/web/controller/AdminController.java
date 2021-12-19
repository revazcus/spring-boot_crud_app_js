package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String showAllUsers(Model model) {
        User newUser = new User();
        User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("admin", admin);
        model.addAttribute("newUser", newUser);
        return "users_all";
    }

    @GetMapping("/new")
    public String createUserForm(Model model) {
        User admin = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", new User());
        model.addAttribute("admin", admin);
        return "user_create";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam(required = false) String[] auth) {
        Set<Role> roleSet = new HashSet<>();
        Role adminRole = roleService.getRoleByRoleName("ADMIN");
        Role userRole = roleService.getRoleByRoleName("USER");
        if (adminRole.toString().equals(auth[0])){
            roleSet.add(adminRole);
        } if (userRole.toString().equals(auth[0])){
            roleSet.add(userRole);
        } if (auth.length == 2){
            roleSet.add(adminRole);
            roleSet.add(userRole);
        }
        user.setRoles(roleSet);
        userService.addUser(user);
        return "redirect:/admin/";
    }

        @DeleteMapping("/delete/{id}")
        public String deleteUser ( @PathVariable("id") long id){
            userService.deleteUserById(id);
            return "redirect:/admin/";
        }

        @PutMapping("/edit/{id}")
        public String updateUser (@ModelAttribute("user") User user,
                                  @PathVariable("id") long id,
                                  @RequestParam(required = false) String[] auth){
            Set<Role> roleSet = new HashSet<>();
            Role adminRole = roleService.getRoleByRoleName("ADMIN");
            Role userRole = roleService.getRoleByRoleName("USER");
            if (adminRole.toString().equals(auth[0])){
                roleSet.add(adminRole);
            } if (userRole.toString().equals(auth[0])){
                roleSet.add(userRole);
            } if (auth.length == 2){
                roleSet.add(adminRole);
                roleSet.add(userRole);
            }
            user.setRoles(roleSet);
            userService.updateUser(id, user);
            return "redirect:/admin/";
        }

        @GetMapping("/{id}")
        public String userPage ( @PathVariable("id") long id, Model model){
            model.addAttribute("user", userService.findById(id));
            return "user_page";
        }
    }
