package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UdUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repositor.AuthUserRepository;
import guru.qa.niffler.data.repositor.impl.AuthUserRepositoryJdbc;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;


public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryJdbc();
    //    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();
    private final UdUserDao udUserDao = new UdUserDaoSpringJdbc();

    private final AuthUserDao authUserDaoJdbc = new AuthUserDaoJdbc();
    private final AuthAuthorityDao authAuthorityDaoJdbc = new AuthAuthorityDaoJdbc();
    private final UdUserDao udUserDaoJdbc = new UdUserDaoJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new ChainedTransactionManager(
                    new JdbcTransactionManager(DataSources.dataSource(CFG.authJdbcUrl())),
                    new JdbcTransactionManager(DataSources.dataSource(CFG.userdataJdbcUrl()))
            )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UserJson createUserSpringJdbcTransaction(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
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

                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            udUserDao.create(UserEntity.fromJson(user)),
                            null
                    );
                }
        );
    }

//    public UserJson createUserSpringJdbc(UserJson user) {
//        AuthUserEntity authUser = new AuthUserEntity();
//        authUser.setUsername(user.username());
//        authUser.setPassword(pe.encode("12345"));
//        authUser.setEnabled(true);
//        authUser.setAccountNonExpired(true);
//        authUser.setAccountNonLocked(true);
//        authUser.setCredentialsNonExpired(true);
//
//        AuthUserEntity createdAuthUser = authUserDao.create(authUser);
//
//        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
//                e -> {
//                    AuthorityEntity ae = new AuthorityEntity();
//                    ae.setUser(createdAuthUser);
//                    ae.setAuthority(e);
//                    return ae;
//                }
//        ).toArray(AuthorityEntity[]::new);
//
//        authAuthorityDao.create(authorityEntities);
//        return UserJson.fromEntity(
//                udUserDao.create(UserEntity.fromJson(user)),
//                null
//        );
//    }

    public UserJson createUserJdbcTransaction(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserDaoJdbc.create(authUser);

                    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(createdAuthUser);
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthorityEntity[]::new);

                    authAuthorityDaoJdbc.create(authorityEntities);
                    return UserJson.fromEntity(
                            udUserDaoJdbc.create(UserEntity.fromJson(user)),
                            null
                    );
                }
        );
    }

    public UserJson createUserJdbc(UserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);

        AuthUserEntity createdAuthUser = authUserDaoJdbc.create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUser(createdAuthUser);
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);

        authAuthorityDaoJdbc.create(authorityEntities);
        return UserJson.fromEntity(
                udUserDaoJdbc.create(UserEntity.fromJson(user)),
                null
        );
    }
}