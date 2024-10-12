package guru.qa.niffler.data.repositor.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.jpa.EntityManagers;
import guru.qa.niffler.data.repositor.UserdataUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserdataUserRepositoryHibernate implements UserdataUserRepository {
    private static final Config CFG = Config.getInstance();


    private final EntityManager entityManager = EntityManagers.em(CFG.userdataJdbcUrl());

    @Override
    public UserEntity create(UserEntity user) {
        entityManager.joinTransaction();
        entityManager.persist(user);
        return user;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(UserEntity.class, id));
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try {
            return Optional.of(
                    entityManager.createQuery("SELECT u FROM UserEntity u WHERE u.username =: username", UserEntity.class)
                            .setParameter("username", username)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    @Override
    public List<UserEntity> findAll() {
        return null;
    }

    @Override
    public void addInvitation(UserEntity requester, UserEntity addressee) {

    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {
        entityManager.joinTransaction();
    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {
        entityManager.joinTransaction();

    }
    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        entityManager.joinTransaction();

    }

    @Override
    public void delete(UserEntity user) {

    }

}
