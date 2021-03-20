package com.example.socialnetwork.repository;

import com.example.socialnetwork.controller.api.FriendInfo;
import com.example.socialnetwork.model.Friend;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author RKomov
 */
@Repository
public class FriendRepository {

    private JdbcTemplate jdbcTemplate;
    private JdbcTemplate jdbcTemplate1;

    public FriendRepository(JdbcTemplate jdbcTemplate,
                            @Qualifier("friendsDs") DataSource friendsDs) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplate1 = new JdbcTemplate(friendsDs);
    }


    public List<FriendInfo> getFriendsByUserId(Long userId) {
        try (Stream<FriendInfo> friendInfoStream = jdbcTemplate1.queryForStream(
                "SELECT f.userId, f.friendId, p.firstName, p.surname FROM Friend f inner join UserProfile p on f.friendId = p.id WHERE f.userId = ?",
                this::friendInfoMapper
                , userId)) {
            return friendInfoStream
                    .collect(Collectors.toList());
        }
    }

    public List<FriendInfo> getFriendsByUserId(Long userId, Long fromId, int limit) {
        try (Stream<FriendInfo> friendInfoStream = jdbcTemplate.queryForStream(
                "SELECT f.userId, f.friendId, p.firstName, p.surname FROM Friend f inner join UserProfile p on f.friendId = p.id WHERE f.userId = ? and f.friendId >= ? order by f.friendId limit ?",
                this::friendInfoMapper
                , userId, fromId, limit)) {
            return friendInfoStream
                    .collect(Collectors.toList());
        }
    }

    public Long getMinFriendId(Long userId) {
        return jdbcTemplate.queryForObject(
                "SELECT min(f.friendId) FROM Friend f  WHERE f.userId = ?",
                Long.class,
                userId);
    }

    public Friend save(Friend friend) {

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            var preparedStatement = con.prepareStatement("insert into Friend(userId,friendId) values (?, ?) on duplicate key update id=id", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, friend.getUserId());
            preparedStatement.setLong(2, friend.getFriendId());

            return preparedStatement;
        }, keyHolder);

        return new Friend(keyHolder.getKey().longValue(), friend.getUserId(), friend.getFriendId());
    }

    public Optional<FriendInfo> getFriendInfo(Long id) {
        try (Stream<FriendInfo> friendInfoStream = jdbcTemplate.queryForStream(
                "SELECT f.userId, f.friendId, p.firstName, p.surname FROM Friend f inner join UserProfile p on f.friendId = p.id WHERE f.id = ?",
                this::friendInfoMapper
                , id)) {
            return friendInfoStream
                    .findAny();
        }
    }

    public Optional<FriendInfo> getFriendInfoByUserIdAndFriendId(Long userId, Long friendId) {
        try (Stream<FriendInfo> friendInfoStream = jdbcTemplate.queryForStream(
                "SELECT f.userId, f.friendId, p.firstName, p.surname FROM Friend f inner join UserProfile p on f.friendId = p.id WHERE f.userId=? and f.friendId = ?",
                this::friendInfoMapper,
                userId,
                friendId)) {
            return friendInfoStream.findAny();
        }
    }

    public Optional<Friend> getFriendByUserIdAndFriendId(Long userId, Long friendId) {
        try (Stream<Friend> friendInfoStream = jdbcTemplate.queryForStream(
                "SELECT f.id, f.userId, f.friendId FROM Friend f WHERE f.userId=? and f.friendId = ?",
                this::friendMapper,
                userId,
                friendId)) {
            return friendInfoStream.findAny();
        }
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from  Friend where id=? ", id);
    }


    private Friend friendMapper(ResultSet rs, int rowNum) throws SQLException {
        return new Friend(
                rs.getLong("id"),
                rs.getLong("userId"),
                rs.getLong("friendId")
        );
    }

    private FriendInfo friendInfoMapper(ResultSet rs, int rowNum) throws SQLException {
        return new FriendInfo(
                rs.getLong("userId"),
                rs.getLong("friendId"),
                rs.getString("firstName"),
                rs.getString("surname")
        );
    }

}
