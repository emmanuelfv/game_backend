package org.gamebackend.login.service;

import org.gamebackend.login.model.UserModel;
import org.gamebackend.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public String createUser(UserModel user){
        Optional<UserModel> userFound = userRepository.findByName(user.getName());
        if (userFound.isPresent()){
            return "found";
        }
        else {
            userRepository.save(user);
            return "created";
        }
    }

    public List<UserModel> showAll(){
        return userRepository.findAll();
    }
}
