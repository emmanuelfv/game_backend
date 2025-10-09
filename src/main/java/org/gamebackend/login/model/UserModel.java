package org.gamebackend.login.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.xml.bind.DatatypeConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserModel {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Getter
    @Setter
    long id;

    @Getter
    String name;

    @Getter
    String password;

    @Getter
    @Setter
    String lastToken;

    public void setName(String name){
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("name is not defined");
        }
        if(name.length() < 3 || name.length() > 50) {
            throw new IllegalArgumentException("invalid name length");
        }

        this.name = name;
    }

    @SneakyThrows
    public void setPassword(String password, String Mode) throws Exception{
        if(password == null){
            throw new IllegalArgumentException("password is not defined");
        }
        if(Mode.equals("create")){
            if(password.isBlank()){
                throw new IllegalArgumentException("password cannot be null");
            }
            String passwordRegex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,50}$";
            Pattern passwordPattern = Pattern.compile(passwordRegex);
            Matcher matcher = passwordPattern.matcher(password);
            if(! matcher.matches()) {
                throw new IllegalArgumentException("password doesn't meet security criteria");
            }
        }

        String MD5_alg = "MD5";
        MessageDigest md = MessageDigest.getInstance(MD5_alg);
        md.update(password.getBytes());
        byte[] digest = md.digest();
        this.password = DatatypeConverter.printHexBinary(digest).toUpperCase();
    }
}
