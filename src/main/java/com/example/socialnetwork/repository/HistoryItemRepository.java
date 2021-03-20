package com.example.socialnetwork.repository;

import com.example.socialnetwork.config.HistoryCacheConfig;
import com.example.socialnetwork.model.HistoryEvent;
import com.example.socialnetwork.model.HistoryEventType;
import com.example.socialnetwork.model.HistoryItem;
import com.example.socialnetwork.model.ObjectType;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author RKomov
 */
@Repository
public class HistoryItemRepository {

    private final JdbcTemplate jdbcTemplate;
    private Cache cache;

    public HistoryItemRepository(JdbcTemplate jdbcTemplate, CacheManager cacheManager) {
        this.jdbcTemplate = jdbcTemplate;
        this.cache = cacheManager.getCache(HistoryCacheConfig.HISTORY_CACHE);
    }

    @Cacheable(value = HistoryCacheConfig.HISTORY_CACHE)
    public Collection<HistoryItem> getHistoryItems(Long ownerId) {

        try (Stream<HistoryItem> friendInfoStream = jdbcTemplate.queryForStream(
                "SELECT id, " +
                        "ownerId, " +
                        "userId, " +
                        "userDescription, " +
                        "objectId, " +
                        "objectDescription, " +
                        "objectType, " +
                        "eventType, " +
                        "eventDate" +
                        " FROM HistoryItem  WHERE ownerId=?" +
                        " order by id desc",
                this::itemMapper,
                ownerId)) {
            return friendInfoStream.collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
        }
    }

    private HistoryItem itemMapper(ResultSet rs, int rowNum) throws SQLException {
        return new HistoryItem(
                rs.getLong("id"),
                rs.getLong("ownerId"),
                new HistoryEvent(
                        rs.getLong("userId"),
                        rs.getString("userDescription"),
                        rs.getLong("objectId"),
                        rs.getString("objectDescription"),
                        ObjectType.valueOf(rs.getString("objectType")),
                        HistoryEventType.valueOf(rs.getString("eventType")),
                        rs.getObject("eventDate", LocalDate.class)
                )
        );
    }

    @Transactional
    public void save(List<HistoryItem> items) {
        try (var con = jdbcTemplate.getDataSource().getConnection()) {

            con.setAutoCommit(false);

            PreparedStatement preparedStatement = insertPreparedStatement(con);

            for (HistoryItem user : items) {
                insertSetParameters(user, preparedStatement);
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();

            con.commit();

            updateCache(items);

        } catch (SQLException e) {

            DataAccessException ex = jdbcTemplate.getExceptionTranslator().translate("Batch Insert", null, e);
            throw ex == null ? new RuntimeException(e) : ex;
        }
    }

    private void updateCache(List<HistoryItem> collect) {
        for (HistoryItem historyItem : collect) {

            ConcurrentLinkedDeque value = cache.get(historyItem.getOwnerId(), ConcurrentLinkedDeque.class);

            if (value != null) {
                value.addFirst(historyItem);
            }
        }
    }

    private PreparedStatement insertPreparedStatement(Connection con) throws SQLException {
        var st = con.prepareStatement("insert into HistoryItem( " +
                "ownerId, " +
                "eventType, " +
                "userId, " +
                "eventDate, " +
                "userDescription," +
                "objectDescription," +
                "objectId," +
                "objectType " +
                ") values (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        return st;
    }

    private void insertSetParameters(HistoryItem item, PreparedStatement st) throws SQLException {
        st.setLong(1, item.getOwnerId());
        st.setString(2, item.getEvent().getEventType().name());
        st.setLong(3, item.getEvent().getUserId());
        st.setObject(4, item.getEvent().getEventDate());
        st.setString(5, item.getEvent().getUserDescription());
        st.setString(6, item.getEvent().getObjectDescription());
        st.setLong(7, item.getEvent().getObjectId());
        st.setString(8, item.getEvent().getObjectType().name());
    }


}
