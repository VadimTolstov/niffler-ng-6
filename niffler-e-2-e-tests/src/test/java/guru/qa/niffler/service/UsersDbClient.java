package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.CurrencyValues;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repositor.AuthUserRepository;
import guru.qa.niffler.data.repositor.UserdataUserRepository;
import guru.qa.niffler.data.repositor.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repositor.impl.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.utils.RandomDataUtils;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

import static guru.qa.niffler.utils.RandomDataUtils.*;


public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();
    private final UserdataUserRepository udUserRepository = new UserdataUserRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UserJson createUser(String username, String password) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, password);
                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            udUserRepository.create(userEntity(username)),
                            null
                    );
                }
        );
    }

    public void addIncomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = udUserRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            String username = randomUsername();

                            AuthUserEntity authUser = authUserEntity(username, "12345");
                            authUserRepository.create(authUser);
                            UserEntity adressee = udUserRepository.create(userEntity(username));
                            udUserRepository.addIncomeInvitation(targetEntity, adressee);
                            return null;
                        }
                );
            }
        }
    }

    public void addOutcomeInvitation(UserJson targetUser, int count) {
        if (count > 0) {
            UserEntity targetEntity = udUserRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            String username = randomUsername();

                            AuthUserEntity authUser = authUserEntity(username, "12345");
                            authUserRepository.create(authUser);
                            UserEntity adressee = udUserRepository.create(userEntity(username));
                            udUserRepository.addOutcomeInvitation(targetEntity, adressee);
                            return null;
                        }
                );
            }
        }
    }

    void addFriend(UserJson targetUser, int count) {

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
            udUserRepository.addInvitation(UserEntity.fromJson(requester), UserEntity.fromJson(addressee));
            return null;
        });
    }

    public void addFriend(UserJson requester, UserJson addressee) {
        xaTransactionTemplate.execute(() -> {
            udUserRepository.addFriend(UserEntity.fromJson(requester), UserEntity.fromJson(addressee));
            return null;
        });
    }
}