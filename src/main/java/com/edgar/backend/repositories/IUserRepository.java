package com.edgar.backend.repositories;

import com.edgar.backend.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserModel, String> {
    Optional<UserModel> findByEmail(String email);
}
