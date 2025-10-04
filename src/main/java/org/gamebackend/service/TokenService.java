package org.gamebackend.service;

import org.gamebackend.model.TokenModel;
import org.gamebackend.model.UserModel;
import org.gamebackend.repository.TokenRepository;
import org.gamebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TokenService {
    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    UserRepository userRepository;

    @Value("${token.expiration.time}")
    private int expirationMinutes;

    public Optional<TokenModel> findToken(String token){
        return tokenRepository.findByToken(token);
    }

    public String provideToken(UserModel user){
        Optional<UserModel> userFound = userRepository.findByNameAndPassword(user.getName(), user.getPassword());
        if (userFound.isPresent() ){
            UserModel userToSave = userFound.get();
            TokenModel token = new TokenModel();
            try {
                token.setToken(userToSave.getLastToken());
            } catch (Exception e) {
                token.setToken();
            }
            LocalDateTime time = LocalDateTime.now();
            token.setCreationdate(time);
            token.setUpdatedate(time.minusMinutes(expirationMinutes));
            String validateResult = this.validateToken(token);

            if (validateResult.equals("validated")) {
                Optional<TokenModel> tokenWrapper = tokenRepository.findByToken(token.getToken());
                token = tokenWrapper.get();
            } else if (validateResult.equals("expired")) {
                token.setToken();
            }
            token.setUpdatedate(time);
            tokenRepository.save(token);
            userToSave.setLastToken(token.getToken());
            userRepository.save(userToSave);
            return token.getToken();
        }
        else {
            return "invalid";
        }
    }

    public String validateToken(TokenModel token){
        Optional<TokenModel> tokenFound = tokenRepository.findByTokenAndUpdatedateGreaterThan(token.getToken(), token.getUpdatedate());
        if(tokenFound.isPresent()){
            return "validated";
        } else {
            tokenFound = tokenRepository.findByToken(token.getToken());
            if(tokenFound.isPresent()){
                return "expired";
            } else {
                return "not found";
            }
        }
    }

    public List<TokenModel> showAll(){
        return tokenRepository.findAll();
    }
}
