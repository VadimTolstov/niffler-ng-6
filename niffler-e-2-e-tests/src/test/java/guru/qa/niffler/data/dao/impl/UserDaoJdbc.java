package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.entity.user.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.*;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

public class UserDaoJdbc implements UserDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity createUser(UserEntity user) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO user(username, currency, firstname, surname, photo, photo_small, full_name) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                ps.setString(1, user.getUsername());
                ps.setObject(2, user.getCurrency());
                ps.setString(3, user.getFirstname());
                ps.setString(4, user.getSurname());
                ps.setByte(5, user.getPhoto()[0]);
                ps.setByte(5, user.getPhotoSmall()[0]);

                ps.executeUpdate();

                final UUID generatedKey;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedKey = rs.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Can`t find id in ResultSet");
                    }
                }
                user.setId(generatedKey);
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM user WHERE id = ?"
            )) {
                ps.setObject(1, id);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        UserEntity us = new UserEntity();
                        us.setId(rs.getObject("id", UUID.class));
                        us.setUsername(rs.getString("username"));
                        us.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        us.setFirstname(rs.getString("firstname"));
                        us.setSurname(rs.getString("surname"));
                        us.setPhoto(rs.getBytes("photo"));
                        us.setPhotoSmall(rs.getBytes("photo_small"));
                        us.setFullname(rs.getString("full_name"));
                        return Optional.of(us);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM user WHERE username = ?"
            )) {
                ps.setObject(1, username);
                ps.execute();
                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        UserEntity us = new UserEntity();
                        us.setId(rs.getObject("id", UUID.class));
                        us.setUsername(rs.getString("username"));
                        us.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        us.setFirstname(rs.getString("firstname"));
                        us.setSurname(rs.getString("surname"));
                        us.setPhoto(rs.getBytes("photo"));
                        us.setPhotoSmall(rs.getBytes("photo_small"));
                        us.setFullname(rs.getString("full_name"));
                        return Optional.of(us);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void delete(UserEntity user) {
        try (Connection connection = Databases.connection(CFG.userdataJdbcUrl());
             PreparedStatement ps = connection.prepareStatement(
                     "DELETE FROM spend WHERE id = ?")) {
            ps.setObject(1, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
