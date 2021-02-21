package com.example.socialnetwork.repository;

import com.example.socialnetwork.controller.api.GetUserProfileRequest;
import com.example.socialnetwork.controller.api.UpdateProfileRequest;
import com.example.socialnetwork.model.Gender;
import com.example.socialnetwork.model.UserProfile;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
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

    private JdbcTemplate jdbcTemplateProfiles;


    public UserProfileRepository(JdbcTemplate jdbcTemplate,
                                 @Qualifier("profilesDs") DataSource profilesDs) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplateProfiles = new JdbcTemplate(profilesDs);
    }

    public UserProfile insert(UserProfile userProfile) {

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> this.insertPrepareStatement(con, userProfile), keyHolder);

        return new UserProfile(keyHolder.getKey().longValue(), userProfile);
    }

    public void bulkInsert(Collection<UserProfile> users) {

        try (var con = jdbcTemplate.getDataSource().getConnection()) {

            con.setAutoCommit(false);
            con.createStatement().execute("SET unique_checks=0");
            PreparedStatement preparedStatement = insertPreparedStatement(con);

            for (UserProfile user : users) {
                insertSetParameters(user, preparedStatement);
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

            con.createStatement().execute("SET unique_checks=1");
            con.commit();
        } catch (SQLException e) {

            DataAccessException ex = jdbcTemplate.getExceptionTranslator().translate("Batch Insert", null, e);
            throw ex == null ? new RuntimeException(e) : ex;
        }
    }

    private PreparedStatement insertPrepareStatement(Connection con, UserProfile userProfile) throws SQLException {
        PreparedStatement st = insertPreparedStatement(con);
        insertSetParameters(userProfile, st);


        return st;
    }

    private void insertSetParameters(UserProfile userProfile, PreparedStatement st) throws SQLException {
        st.setString(1, userProfile.getUsername());
        st.setString(2, userProfile.getPassword());
        st.setString(3, userProfile.getFirstName());
        st.setString(4, userProfile.getSurname());
        st.setString(5, userProfile.getCity());
        st.setObject(6, userProfile.getBirthDate());
        st.setString(7, userProfile.getGender() == null ? null : userProfile.getGender().name());
        st.setString(8, userProfile.getInterests());
    }

    private PreparedStatement insertPreparedStatement(Connection con) throws SQLException {
        var st = con.prepareStatement("insert into UserProfile(username, " +
                "password, " +
                "firstName, " +
                "surname, " +
                "city, " +
                "birthDate, " +
                "gender, " +
                "interests) values (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        return st;
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
            stringJoiner.add("firstName like ? ");
            params.add(request.getFirstName() + "%");
        }

        if (request.getSurname() != null) {
            stringJoiner.add("surname like ? ");
            params.add(request.getSurname() + "%");
        }

        String sql = "SELECT id," +
                            "username, " +
                            "password, " +
                            "firstName, " +
                            "surname, " +
                            "city, " +
                            "birthDate, " +
                            "gender, " +
                            "interests FROM UserProfile ";

        String conditions = stringJoiner.toString();
        if (!conditions.isEmpty()) {
            sql += "WHERE " + conditions;
        }

        sql += " order by id desc limit 100";

        return jdbcTemplateProfiles.query(
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
