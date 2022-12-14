package com.example.MYSTORE.Controllers;

import com.example.MYSTORE.SECURITY.JWT.JWTAuthorization;
import com.example.MYSTORE.SECURITY.JWT.JWTRefreshRequest;
import com.example.MYSTORE.SECURITY.JWT.JWTRequest;
import com.example.MYSTORE.SECURITY.JWT.JWTResponse;
import com.example.MYSTORE.SECURITY.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class SecurityController {
    private UserService userService;
    private JWTAuthorization authService;
    @Autowired
    public SecurityController(UserService userService, JWTAuthorization authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity Logout(@RequestBody String email, HttpServletRequest request,
                                 HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("refreshToken")) {
                    userService.LogoutUser(c.getValue());
                }
                c.setValue("");
                c.setPath("/");
                c.setMaxAge(0);
                response.addCookie(c);
            }
        }
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(null);
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(null);
    }

    @PostMapping("/api/auth/register")
    public ResponseEntity Register(@RequestBody String reg) {
        return userService.registerUser(reg);
    }

    @GetMapping("/api/auth/registration")
    public ResponseEntity Registration(@RequestParam(name = "token", required = false) String token,
                                       @RequestParam(name = "email", required = false) String email) {
        System.out.println(token);
        System.out.println(email);
        return userService.confirmUser(token, email);
    }

    @PostMapping("/api/auth/reset")
    public ResponseEntity Reset(@RequestBody String res){
        return userService.resetPassword(res);
    }

    @PostMapping("/api/auth/confirmreset")
    public ResponseEntity ConfirmReset(@RequestBody String res){
        return userService.ConfirmResetPassword(res);
    }
    @PostMapping("/api/auth/login")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest authRequest, HttpServletResponse response) {
        final JWTResponse token = authService.login(authRequest, response);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/api/auth/token")
    public ResponseEntity<JWTResponse> getNewAccessToken(@RequestBody JWTRefreshRequest request,
                                                         HttpServletRequest request1) {
        final JWTResponse token = authService.getAccessToken(request1);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/api/auth/refresh")
    public ResponseEntity<JWTResponse> getNewRefreshToken(@RequestBody JWTRefreshRequest request) {
        final JWTResponse token = authService.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }
}
