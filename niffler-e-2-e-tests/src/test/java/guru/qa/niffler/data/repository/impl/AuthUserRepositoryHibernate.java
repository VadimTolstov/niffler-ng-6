package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.jpa.EntityManagers;
import guru.qa.niffler.data.repository.AuthUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositoryHibernate implements AuthUserRepository {
    private static final Config CFG = Config.getInstance();


    private final EntityManager entityManager = EntityManagers.em(CFG.authJdbcUrl());

    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        entityManager.joinTransaction();
        entityManager.persist(user);
        return user;
    }

    @Override
    public AuthUserEntity update(AuthUserEntity user) {
        entityManager.joinTransaction();
        entityManager.merge(user);
        return user;
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(AuthUserEntity.class, id));
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        try {
            return Optional.of(
                    entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.username =: username", AuthUserEntity.class)
                            .setParameter("username", username)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void remove(AuthUserEntity user) {
        entityManager.joinTransaction();
        entityManager.remove(user);
    }
}