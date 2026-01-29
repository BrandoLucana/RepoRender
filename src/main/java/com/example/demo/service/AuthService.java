package com.example.demo.service;

import com.example.demo.dto.JwtResponse;
import com.example.demo.dto.LoginRequest;

public interface AuthService {
    JwtResponse login(LoginRequest request);
}