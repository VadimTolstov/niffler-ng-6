package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendshipEntitiesRowMapper implements RowMapper<FriendshipEntity> {

    public static final FriendshipEntitiesRowMapper instance = new FriendshipEntitiesRowMapper();
    private FriendshipEntitiesRowMapper() {
    }

    @Override
    public FriendshipEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        FriendshipEntity result = new FriendshipEntity();
        result.setRequester(rs.getObject("requester_id", UserEntity.class));
        result.setAddressee(rs.getObject("addressee_id", UserEntity.class));
        result.setStatus(FriendshipStatus.valueOf(rs.getString("status")));
        result.setCreatedDate(rs.getDate("created_date"));
        return result;
    }
}
