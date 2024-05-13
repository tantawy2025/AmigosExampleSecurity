package com.example.amigosExampe.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsernamePasswordAuthenticationRequest {

    private String username;
    private String password;
}
