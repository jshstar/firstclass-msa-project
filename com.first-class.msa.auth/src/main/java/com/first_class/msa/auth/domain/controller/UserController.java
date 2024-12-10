package com.first_class.msa.auth.domain.controller;

import com.first_class.msa.auth.config.jwt.JwtUtil;
import com.first_class.msa.auth.domain.dto.ReqLoginDTO;
import com.first_class.msa.auth.domain.dto.ReqUserPostDTO;
import com.first_class.msa.auth.domain.dto.ResLoginDTO;
import com.first_class.msa.auth.domain.dto.ResUserPostDTO;
import com.first_class.msa.auth.domain.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signUp")
    public ResponseEntity<ResUserPostDTO> signUp(@RequestBody ReqUserPostDTO reqUserPostDTO) {

        String account = userService.save(reqUserPostDTO);

        ResUserPostDTO responseDTO = ResUserPostDTO.builder()
                .account(account)
                .message("회원가입 성공")
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PostMapping("/signIn")
    public ResponseEntity<ResLoginDTO> signIn(@RequestBody ReqLoginDTO reqLoginDTO) {

        String jwtToken = userService.signIn(reqLoginDTO);

        ResLoginDTO responseDTO = ResLoginDTO.builder()
                .jwtToken(jwtToken)
                .message("로그인 성공")
                .build();

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {

        new SecurityContextLogoutHandler().logout(request, response,
                SecurityContextHolder.getContext().getAuthentication());

        return new ResponseEntity<>("로그아웃 성공", HttpStatus.OK);
    }
}