package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.entity.userdata.CurrencyValues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UdUserDaoJdbc implements UdUserDao {
    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity create(UserEntity user) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO user (username, currency) VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.executeUpdate();
            final UUID generatedUserId;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedUserId = rs.getObject("id", UUID.class);
                } else {
                    throw new IllegalStateException("Can`t find id in ResultSet");
                }
            }
            user.setId(generatedUserId);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void delete(UserEntity user) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "DELETE FROM spend WHERE id = ?")) {
            ps.setObject(1, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserEntity> findAll() {
        List<UserEntity> users = new ArrayList<>();
        try (PreparedStatement statement = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\""
        )) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    UserEntity user = new UserEntity();

                    user.setId(resultSet.getObject("id", UUID.class));
                    user.setUsername(resultSet.getString("username"));
                    user.setCurrency(resultSet.getObject("currency", CurrencyValues.class));
                    user.setFirstname(resultSet.getString("firstname"));
                    user.setSurname(resultSet.getString("surname"));
                    user.setPhoto(resultSet.getBytes("photo"));
                    user.setPhotoSmall(resultSet.getBytes("photo_small"));
                    user.setFullname(resultSet.getString("full_name"));

                    users.add(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }
}

