package com.edgar.backend.services;

import com.edgar.backend.models.UserModel;
import com.edgar.backend.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;

@Service
public class UserService {
    @Autowired
    IUserRepository userRepository;

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int ID_LENGTH = 20;
    private static final SecureRandom RANDOM = new SecureRandom();

    // Método para generar un ID único
    private String generateUniqueId() {
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }



    public String registerUser(String name, String email, String password, String cpass, MultipartFile image) throws IOException{
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || cpass.isEmpty()) {
            return "All fields are required";
        }
        if (!password.equals(cpass)) {
            return "Passwords do not match";
        }
        if (userRepository.findByEmail(email).isPresent()) {
            return "Email already exists";
        }
        String imageName = null;
        if (image != null && !image.isEmpty()) {
            String uploadDir = "uploads/files";
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }
            imageName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, imageName);
            Files.write(filePath, image.getBytes());
        }

        UserModel user = new UserModel();
        user.setId(generateUniqueId());
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setImage(imageName);
        userRepository.save(user);
        return "User registered successfully";
    }

    public UserModel loginUser(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getPassword().equals(password))
                .orElse(null);
    }
}
