package com.example.itemservice.repository;


import com.example.itemservice.domain.model.JwtAuthenticationResponse;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenBlackListRepository extends JpaRepository<JwtAuthenticationResponse, Long> {

    @NonNull
    List<JwtAuthenticationResponse> findAll();

    Optional<JwtAuthenticationResponse> findByToken(String token);

}
