package com.duoc.saludamepo_neon.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
@Tag(name = "1. Salud del Sistema", description = "Endpoint de monitoreo para Render y UptimeRobot")
public class SaludController {

    @GetMapping
    @Operation(summary = "Verificar estado del servidor")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "servicio", "SaludamePo_Neon Backend",
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}