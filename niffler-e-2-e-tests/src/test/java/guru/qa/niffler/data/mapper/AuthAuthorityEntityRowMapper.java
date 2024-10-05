package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthAuthorityEntityRowMapper implements RowMapper<AuthorityEntity> {

    public static final AuthAuthorityEntityRowMapper instance = new AuthAuthorityEntityRowMapper();

    private AuthAuthorityEntityRowMapper() {
    }

    @Override
    public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthorityEntity result = new AuthorityEntity();
//        AuthUserEntity user = new AuthUserEntity();
//        user.setId(rs.getObject("user_id", UUID.class));

        result.setId(rs.getObject("id", UUID.class));
//        result.setUser(user);
        result.setUser(rs.getObject("user_id", AuthUserEntity.class));
        result.setAuthority(Authority.valueOf(rs.getString("password")));

        return result;
    }
}