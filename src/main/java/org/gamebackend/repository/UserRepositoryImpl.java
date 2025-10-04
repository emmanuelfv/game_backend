package org.gamebackend.repository;

import org.gamebackend.model.UserModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final List<UserModel> users = new ArrayList<>();

    @Override
    public Optional<UserModel> findByName(String name) {
        return users.stream()
                .filter(user -> user.getName().equals(name))
                .findFirst();
    }

    @Override
    public Optional<UserModel> findByNameAndPassword(String name, String password) {
        return users.stream()
                .filter(user -> user.getName().equals(name) &&
                        user.getPassword().equals(password))
                .findFirst();
    }

    // Método auxiliar para agregar usuarios
    public void save(UserModel user) {
        users.add(user);
    }

    public List<UserModel> findAll(){
        return List.copyOf(users);
    }
}
