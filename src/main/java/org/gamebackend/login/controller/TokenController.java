package org.gamebackend.login.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gamebackend.login.model.TokenModel;
import org.gamebackend.login.model.UserModel;
import org.gamebackend.login.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @Value("${token.expiration.time}")
    private int expirationMinutes;

    @PostMapping("login")
    public ResponseEntity<Map<String,String>> login(@RequestHeader Map<String,String> user_login_data){
        log.info("In Rest login");
        log.info("input header:\n{}", user_login_data);
        UserModel userToCreate = new UserModel();
        try {
            userToCreate.setName(user_login_data.get("user"));
            userToCreate.setPassword(user_login_data.get("password"), "login");
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        String creationResponse = tokenService.provideToken(userToCreate);
        log.info("creationResponse: {}", creationResponse);
        HashMap<String,String> response = new HashMap<String,String>();
        response.put("token",creationResponse);
        if(creationResponse.equals("invalid")){
            response.put("responseString","invalid user password, please retry");
            log.info("response: {}", response);
            log.info("http status: {}", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<Map<String,String>>(response, HttpStatus.BAD_REQUEST);
        }
        response.put("responseString","Success");
        log.info("response: {}", response);
        log.info("http status: {}", HttpStatus.OK);
        return new ResponseEntity<Map<String,String>>(response, HttpStatus.OK);
    }

    @PostMapping("validate_token")
    public ResponseEntity<String> validateToken(@RequestHeader Map<String,String> token_data){
        log.info("In Rest login");
        log.info("input header:\n{}", token_data);
        TokenModel tokenToValidate = new TokenModel();
        LocalDateTime time = LocalDateTime.now();
        log.info("time:\n{}", time);
        log.info("System expiration time [minutes]:\n{}", expirationMinutes);
        try {
            tokenToValidate.setToken(token_data.get("token"));
        } catch (Exception e) {
            log.warn(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        tokenToValidate.setCreationdate(time);
        tokenToValidate.setUpdatedate(time.minusMinutes(expirationMinutes));
        String creationResponse = tokenService.validateToken(tokenToValidate);
        HttpStatusCode status;
        if(creationResponse.equals("validated")){
            status = HttpStatus.OK;
        }
        else {
            status = HttpStatus.BAD_REQUEST;
        }
        log.info("creationResponse: {}", creationResponse);
        log.info("http status: {}", status);
        return new ResponseEntity<String>(creationResponse, status);
    }

    @GetMapping("show_all_tokens")
    public ResponseEntity<List<TokenModel>> showAllTokens(){
        log.info("In Rest show_all_tokens");
        List<TokenModel> tokenList = tokenService.showAll();
        log.info("tokenList: {}", tokenList);
        return new ResponseEntity<List<TokenModel>>(tokenList, HttpStatus.OK);
    }
}