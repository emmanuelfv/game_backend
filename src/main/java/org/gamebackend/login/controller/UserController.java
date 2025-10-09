package org.gamebackend.login.controller;

import lombok.extern.slf4j.Slf4j;
import org.gamebackend.login.model.UserModel;
import org.gamebackend.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/")
    public String welcome(){
        log.info("In Rest /");
        log.info("welcome");
        return "welcome";
    }

    @PostMapping("create_user")
    public ResponseEntity<String> createUser(@RequestHeader Map<String,String> user_creation_data){
        log.info("In Rest create_user");
        log.info("input header:\n{}", user_creation_data);
        UserModel userToCreate = new UserModel();
        try {
            userToCreate.setName(user_creation_data.get("user"));
            userToCreate.setPassword(user_creation_data.get("password"), "create");

        } catch (Exception e) {
            log.warn("Exception: {}", e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        String creationResponse = userService.createUser(userToCreate);
        log.info("creationResponse: {}", creationResponse);
        if(creationResponse.equals("created")){
            log.info("user created succesfully");
            return new ResponseEntity<String>("user created succesfully", HttpStatus.OK);
        }
        log.info("user already in the system, plaease create a new user");
        return new ResponseEntity<String>("user already in the system, plaease create a new user", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("show_all_users")
    public ResponseEntity<List<UserModel>> showAllUsers(){
        log.info("In Rest show_all_users");
        List<UserModel> userList = userService.showAll();
        log.info("users:\n{}", userList);
        return new ResponseEntity<List<UserModel>>(userList, HttpStatus.OK);
    }}
