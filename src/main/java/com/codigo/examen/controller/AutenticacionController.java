package com.codigo.examen.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ms-exam/v1/auth")
public class AutenticacionController {
    @GetMapping("/login")
    public String login() {
        return "Pagina inicio de sesión ";
    }
    @GetMapping("/logout")
    public String logout() {
        return "Cerrar sesión";
    }
}
