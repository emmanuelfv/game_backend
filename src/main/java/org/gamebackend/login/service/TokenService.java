package org.gamebackend.login.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.gamebackend.login.model.TokenModel;
import org.gamebackend.login.model.UserModel;
import org.gamebackend.login.repository.TokenRepository;
import org.gamebackend.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
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

    public String provideToken(UserModel user) throws Exception{
        Optional<UserModel> userFound = userRepository.findByNameAndPassword(user.getName(), user.getPassword());
        if (userFound.isEmpty() ){
            return "invalid";
        }

        UserModel userToSave = userFound.get();

        String validateResult = this.validateToken(userToSave.getLastToken());

        TokenModel token = new TokenModel();
        if (validateResult.equals("validated")) {
            Optional<TokenModel> tokenWrapper = tokenRepository.findByToken(userToSave.getLastToken());
            token = tokenWrapper.get();
        } else {
            LocalDateTime time = LocalDateTime.now();
            token.setToken();
            token.setCreationdate(time);
            token.setUpdatedate(time);
            tokenRepository.save(token);
        }
        userToSave.setLastToken(token.getToken());
        userRepository.save(userToSave);
        return token.getToken();
    }

    public String validateToken(String token_str){
        if (token_str == null || token_str.isEmpty()) {
            return "not found";
        }

        TokenModel token = new TokenModel();
        LocalDateTime time = LocalDateTime.now();
        log.info("time:\n{}", time);
        log.info("System expiration time [minutes]:\n{}", expirationMinutes);
        token.setToken(token_str);
        token.setCreationdate(time);
        token.setUpdatedate(time);
        Optional<TokenModel> tokenFound = tokenRepository
                .findByTokenAndUpdatedateGreaterThan(
                        token.getToken(),
                        token.getUpdatedate().minusMinutes(expirationMinutes)
                );
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
