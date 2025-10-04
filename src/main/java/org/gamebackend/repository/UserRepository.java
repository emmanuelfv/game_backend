package org.gamebackend.repository;

import org.gamebackend.model.TokenModel;
import org.gamebackend.model.UserModel;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    public Optional<UserModel> findByName(String name);
    public Optional<UserModel> findByNameAndPassword(String name, String password);
    public void save(UserModel user);
    public List<UserModel> findAll();
}
