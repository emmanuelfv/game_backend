package org.gamebackend.model;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import jakarta.xml.bind.DatatypeConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="auth_token")
@NoArgsConstructor
public class TokenModel {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    long id;
    @Getter
    String token;
    @Getter
    @Setter
    LocalDateTime creationdate;
    @Getter
    @Setter
    LocalDateTime updatedate;

    public void setToken() {
        try {
            LocalDateTime time = LocalDateTime.now();
            SecureRandom rand = new SecureRandom();

            StringBuilder rawToken = new StringBuilder()
                    .append(time.toString());
            for (int i = 0; i < 4; i++) {
                rawToken.append(rand.nextInt(10));
            }

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(rawToken.toString().getBytes());
            this.token = DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    public void setToken(String token) throws Exception{
        if(token == null){
            throw new IllegalArgumentException("token is not defined");
        }
        this.token = token;
    }
}
