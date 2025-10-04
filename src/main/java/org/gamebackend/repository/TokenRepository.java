package org.gamebackend.repository;

import org.gamebackend.model.TokenModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TokenRepository {
    public Optional<TokenModel> findByToken(String token);
    public Optional<TokenModel> findByTokenAndUpdatedateGreaterThan(String token, LocalDateTime updatedate);
    public long countByUpdatedateGreaterThan(LocalDateTime updatedate);
    public void save(TokenModel token);
    public List<TokenModel> findAll();
}
