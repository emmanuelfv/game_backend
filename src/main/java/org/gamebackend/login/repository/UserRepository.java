package org.gamebackend.login.repository;

import org.gamebackend.login.model.UserModel;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    public Optional<UserModel> findByName(String name);
    public Optional<UserModel> findByNameAndPassword(String name, String password);
    public void save(UserModel user);
    public List<UserModel> findAll();
}
