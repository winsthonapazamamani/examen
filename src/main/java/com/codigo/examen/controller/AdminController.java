package com.codigo.examen.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ms-exam/v1/admin")
public class AdminController {
    @GetMapping("/info")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAdminInfo() {
        return "Informacion para administradores";
    }
}
