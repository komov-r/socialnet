package com.example.socialnetwork.repository;

import com.example.socialnetwork.controller.api.GetUserProfileRequest;
import com.example.socialnetwork.controller.api.UpdateProfileRequest;
import com.example.socialnetwork.model.Gender;
import com.example.socialnetwork.model.UserProfile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Stream;

/**
 * @author RKomov
 */
@Repository
public class UserProfileRepository {

    private JdbcTemplate jdbcTemplate;

    public UserProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserProfile insert(UserProfile userProfile) {

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            var st = con.prepareStatement("insert into UserProfile(username, " +
                    "password, " +
                    "firstName, " +
                    "surname, " +
                    "city, " +
                    "birthDate, " +
                    "gender, " +
                    "interests) values (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, userProfile.getUsername());
            st.setString(2, userProfile.getPassword());
            st.setString(3, userProfile.getFirstName());
            st.setString(4, userProfile.getSurname());
            st.setString(5, userProfile.getCity());
            st.setObject(6, userProfile.getBirthDate());
            st.setString(7, userProfile.getGender() == null ? null : userProfile.getGender().name());
            st.setString(8, userProfile.getInterests());
            return st;
        }, keyHolder);

        return new UserProfile(keyHolder.getKey().longValue(), userProfile);
    }

    public Optional<UserProfile> getByUsername(String username) {
        try (Stream<UserProfile> userProfileStream = jdbcTemplate.queryForStream(
                "SELECT * FROM UserProfile WHERE username = ?",
                this::userProfileMapper,
                username)) {

            return userProfileStream
                    .findAny();
        }
    }

    public Optional<UserProfile> getById(Long id) {
        try (Stream<UserProfile> userProfileStream = jdbcTemplate.queryForStream(
                "SELECT * FROM UserProfile WHERE ID = ?",
                this::userProfileMapper,
                id)) {
            return userProfileStream
                    .findAny();
        }
    }

    public List<UserProfile> findUserProfiles(GetUserProfileRequest request) {


        StringJoiner stringJoiner = new StringJoiner(" and ");
        List<Object> params = new ArrayList<>();
        if (request.getFirstName() != null) {
            stringJoiner.add("firstName = ? ");
            params.add(request.getFirstName());
        }

        if (request.getSurname() != null) {
            stringJoiner.add("surname = ? ");
            params.add(request.getSurname());
        }

        String sql = "SELECT * FROM UserProfile ";

        String conditions = stringJoiner.toString();
        if (!conditions.isEmpty()) {
            sql += "WHERE " + conditions;
        }

        sql += " order by id desc limit 100";

        return jdbcTemplate.query(
                sql,
                params.toArray(),
                this::userProfileMapper
        );
    }

    public void updateProfile(UpdateProfileRequest req) {
        jdbcTemplate.update("update UserProfile set " +
                        " firstName=?, " +
                        " surname=?, " +
                        " city=?, " +
                        " birthDate=?, " +
                        " gender=?, " +
                        " interests=? " +
                        "where id=?",
                req.getFirstName(),
                req.getSurname(),
                req.getCity(),
                req.getBirthDate(),
                req.getGender() == null ? null : req.getGender().name(),
                req.getInterests(),
                req.getId()
        );
    }


    private UserProfile userProfileMapper(ResultSet rs, int rowNum) throws SQLException {
        return new UserProfile(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("firstName"),
                rs.getString("surname"),
                rs.getString("city"),
                rs.getObject("birthDate", LocalDate.class),
                rs.getString("gender") == null ? null : Gender.valueOf(rs.getString("gender")),
                rs.getString("interests")
        );
    }
}
