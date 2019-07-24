package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final RowMapper<Role> ROLE_ROW_MAPPER = (rs, rowNum) -> {
        String strRole = rs.getString("role");
        return strRole == null ? null : Role.valueOf(strRole);
    };

    private static final ResultSetExtractor<List<User>> RESULT_SET_EXTRACTOR =
            resultSet -> {
                final Map<Integer, User> userMap = new LinkedHashMap<>();
                while (resultSet.next()) {
                    int userId = resultSet.getInt("id");
                    int rowNum = resultSet.getRow();
                    User currentUser = ROW_MAPPER.mapRow(resultSet, rowNum);
                    User user = userMap.computeIfAbsent(userId, u -> currentUser);
                    Set<Role> roles = user.getRoles();
                    Role role = ROLE_ROW_MAPPER.mapRow(resultSet, rowNum);
                    if (role != null) {
                        if (roles == null) {
                            user.setRoles(null);
                            roles = user.getRoles();
                        }
                        roles.add(role);
                    }
                }
                resultSet.close();
                return new LinkedList<>(userMap.values());
            };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        String insertRoles = "INSERT INTO user_roles (user_id, role) VALUES (?, ?)";
        String deleteRoles = "DELETE FROM user_roles WHERE user_id=? AND role=?";
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            updateRoles(insertRoles, user.getId(), user.getRoles());
        } else {
            if (namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id",
                    parameterSource) == 0) {
                return null;
            }
            List<Role> oldRoles = jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id=?",
                    ROLE_ROW_MAPPER, user.getId());
            Set<Role> oldRolesSet = EnumSet.copyOf(oldRoles);
            Set<Role> rolesToDelete = EnumSet.copyOf(oldRoles);
            rolesToDelete.removeAll(user.getRoles());
            updateRoles(deleteRoles, user.getId(), rolesToDelete);
            Set<Role> rolesToAdd = EnumSet.copyOf(user.getRoles());
            rolesToAdd.removeAll(oldRolesSet);
            updateRoles(insertRoles, user.getId(), rolesToAdd);
        }
        return user;
    }

    private void updateRoles(String sql, Integer userId, Set<Role> roleSet) {
        if (roleSet.size() == 0) {
            return;
        }

        List<String> roleList = roleSet.stream().map(Enum::toString).collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, userId);
                ps.setString(2, roleList.get(i));
            }

            @Override
            public int getBatchSize() {
                return roleList.size();
            }
        });
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users " +
                "LEFT JOIN user_roles ur ON users.id = ur.user_id " +
                "WHERE id=?", RESULT_SET_EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users " +
                "LEFT JOIN user_roles ur ON users.id = ur.user_id " +
                "WHERE email=?", RESULT_SET_EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ur ON users.id = ur.user_id " +
                "ORDER BY name, email", RESULT_SET_EXTRACTOR);
    }
}