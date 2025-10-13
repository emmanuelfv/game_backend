package org.gamebackend.login.repository;

import org.gamebackend.login.model.TokenModel;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TokenRepositoryImpl implements TokenRepository {

    private static final List<TokenModel> tokens = new ArrayList<>();

    @Override
    public Optional<TokenModel> findByToken(String token) {
        return tokens.stream()
                .filter(t -> t.getToken().equals(token))
                .findFirst();
    }

    @Override
    public Optional<TokenModel> findByTokenAndUpdatedateGreaterThan(String token, LocalDateTime updatedate) {
        Optional<TokenModel> tokenFound = tokens.stream()
                .filter(t -> t.getToken().equals(token) &&
                        t.getUpdatedate().isAfter(updatedate))
                .findFirst();

        if (tokenFound.isPresent()) {
            tokenFound.get().setUpdatedate(LocalDateTime.now());
        }

        return tokenFound;
    }

    @Override
    public long countByUpdatedateGreaterThan(LocalDateTime updatedate) {
        return tokens.stream()
                .filter(t -> t.getUpdatedate().isAfter(updatedate))
                .count();
    }

    public void save(TokenModel token) {
        tokens.add(token);
    }

    @Override
    public List<TokenModel> findAll() {
        return List.copyOf(tokens);
    }
}