package com.erenbicakci.chatapplication.controller;

import com.erenbicakci.chatapplication.dto.UserDto;
import com.erenbicakci.chatapplication.log.CustomLogInfo;
import com.erenbicakci.chatapplication.service.AuthenticationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/v1/login")
public class AuthenticationController {
     private final AuthenticationService authenticationService;
    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @PostMapping("/save")
    public ResponseEntity<String> save (@RequestBody UserDto userDto){
        String responseObject = authenticationService.save(userDto);
        return ResponseEntity.ok(responseObject);
    }


    @CustomLogInfo
    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestBody UserDto userDto){
        String responseObject = authenticationService.auth(userDto);
        return ResponseEntity.ok(responseObject);
    }

}
