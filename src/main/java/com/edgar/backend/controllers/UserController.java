package com.edgar.backend.controllers;

import com.edgar.backend.models.UserModel;
import com.edgar.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam String name, @RequestParam String email,
                                                  @RequestParam String password, @RequestParam String cpass,
                                                  @RequestParam ("image") MultipartFile image) {
        try {
            String result = userService.registerUser(name, email, password, cpass, image);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: "+e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserModel> loginUser(@RequestParam String email, @RequestParam String password) {
        try {
            UserModel user = userService.loginUser(email, password);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}
