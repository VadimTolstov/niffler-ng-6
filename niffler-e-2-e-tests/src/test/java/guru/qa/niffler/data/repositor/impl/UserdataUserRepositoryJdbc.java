package guru.qa.niffler.data.repositor.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.CurrencyValues;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UdUserEntityRowMapper;
import guru.qa.niffler.data.repositor.UserdataUserRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UserdataUserRepositoryJdbc implements UserdataUserRepository {
    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity create(UserEntity user) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, currency) VALUES (?, ?)",
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
                "SELECT * FROM \"user\" u join \"friendship\" f on u.id = f.requester_id where u.id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                UserEntity user = null;
                List<FriendshipEntity> friendshipEntities = new ArrayList<>();
                while (rs.next()) {
                    if (user == null) {
                        user = UdUserEntityRowMapper.instance.mapRow(rs, 1);
                    }

                    FriendshipEntity fe = new FriendshipEntity();
                    fe.setRequester(user);
                    fe.setAddressee(rs.getObject("f.addressee_id", UserEntity.class));
                    fe.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
                    fe.setCreatedDate(rs.getDate("created_date"));
                    friendshipEntities.add(fe);

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
                if (user == null) {
                    return Optional.empty();
                } else {
                    user.setFriendshipRequests(friendshipEntities);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addInvitation(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) " +
                        "VALUES (?, ?, ?, ?)")) {
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, FriendshipStatus.PENDING.name());
            ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"friendship\" (requester_id, addressee_id, status, created_date) " +
                        "VALUES (?, ?, ?, ?)")) {

            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());
            ps.setString(3, FriendshipStatus.ACCEPTED.name());
            ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            ps.addBatch();

            ps.setObject(1, addressee.getId());
            ps.setObject(2, requester.getId());
            ps.setString(3, FriendshipStatus.ACCEPTED.name());
            ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            ps.addBatch();
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addIncomeInvitation(UserEntity requester, UserEntity addressee) {

    }

    @Override
    public void addOutcomeInvitation(UserEntity requester, UserEntity addressee) {

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

