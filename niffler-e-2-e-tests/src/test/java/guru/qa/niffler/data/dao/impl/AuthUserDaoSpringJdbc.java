package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;

import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoSpringJdbc implements AuthUserDao {
    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        return null;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return Optional.empty();
    }
}
