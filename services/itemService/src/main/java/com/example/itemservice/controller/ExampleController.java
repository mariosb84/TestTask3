package com.example.itemservice.controller;

import com.example.itemservice.service.UserServiceData;
import com.example.itemservice.service.UserServiceNew;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
@RequiredArgsConstructor
/*@Tag(name = "Аутентификация")*/
public class ExampleController {
    private final UserServiceData service;

    @GetMapping("/2")
    /*@Operation(summary = "Доступен только авторизованным пользователям")*/
    public String example2() {
        return "Hello, world 2!";
    }
    @GetMapping
    /*@Operation(summary = "Доступен только авторизованным пользователям")*/
    public String example() {
        return "Hello, world!";
    }

    @GetMapping("/admin")
   /* @Operation(summary = "Доступен только авторизованным пользователям с ролью ADMIN")*/
    @PreAuthorize("hasRole('ADMIN')")
    public String exampleAdmin() {
        return "Hello, admin!";
    }

    @GetMapping("/get-admin")
   /* @Operation(summary = "Получить роль ADMIN (для демонстрации)")*/
    public void getAdmin() {
        service.getAdmin();
    }

}