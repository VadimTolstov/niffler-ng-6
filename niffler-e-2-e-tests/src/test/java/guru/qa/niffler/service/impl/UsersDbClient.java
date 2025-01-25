package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.CurrencyValues;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;


public class UsersDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
    private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    @NotNull
    @Override
    @Step("Создаем юзера через UsersDbClient Hibernate")
    public UserJson createUser(@NotNull String username, @NotNull String password) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() ->
                UserJson.fromEntity((createNewUser(username, password)),
                        null
                )
        ));
    }

    private UserEntity createNewUser(String username, String password) {
        AuthUserEntity authUser = authUserEntity(username, password);
        authUserRepository.create(authUser);
        return userdataUserRepository.create(userEntity(username));
    }

    @Override
    public List<UserJson> addIncomeInvitations(UserJson targetUser, int count) {
        List<UserJson> users = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            final String username = randomUsername();
                            final UserEntity user = createNewUser(username, "12345");
                            userdataUserRepository.sendInvitation(user, targetEntity);
                            users.add(UserJson.fromEntity(user, null));
                            return null;
                        }
                );
            }
        }
        return users;
    }

    @Override
    public List<UserJson> addOutcomeInvitations(UserJson targetUser, int count) {
        List<UserJson> users = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            final String username = randomUsername();
                            final UserEntity user = createNewUser(username, "12345");
                            userdataUserRepository.sendInvitation(targetEntity, user);
                            users.add(UserJson.fromEntity(user, null));
                            return null;
                        }
                );
            }
        }
        return users;
    }

    @Override
    public List<UserJson> addFriends(UserJson targetUser, int count) {
        List<UserJson> users = new ArrayList<>();
        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            final String username = randomUsername();
                            final UserEntity user = createNewUser(username, "12345");
                            userdataUserRepository.addFriend(targetEntity,user );
                            users.add(UserJson.fromEntity(user, null));
                            return user;
                        }
                );
            }
        }
        return users;
    }

    private UserEntity userEntity(String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setCurrency(CurrencyValues.RUB);
        return ue;
    }

    private AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUser);
                            ae.setAuthority(e);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }

    public void addInvitation(UserJson requester, UserJson addressee) {
        xaTransactionTemplate.execute(() -> {
            userdataUserRepository.sendInvitation(UserEntity.fromJson(requester), UserEntity.fromJson(addressee));
            return null;
        });
    }

    public void addFriend(UserJson requester, UserJson addressee) {
        xaTransactionTemplate.execute(() -> {
            userdataUserRepository.addFriend(UserEntity.fromJson(requester), UserEntity.fromJson(addressee));
            return null;
        });
    }
}