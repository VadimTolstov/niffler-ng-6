package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(AuthorityEntity... authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"authority\" (user_id, authority) " +
                        "VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
        )) {
            for (AuthorityEntity a : authority) {
                ps.setObject(1, a.getUserId());
                ps.setString(2, a.getAuthority().name());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthorityEntity> findAll() {
        List<AuthorityEntity> authAuthority = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM authority"
        )) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    AuthorityEntity authority = new AuthorityEntity();

                    authority.setId(resultSet.getObject("id", UUID.class));
                    authority.setUserId(resultSet.getObject("user_id", UUID.class));
                    authority.setAuthority(resultSet.getObject("authority", Authority.class));

                    authAuthority.add(authority);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authAuthority;
    }
}
