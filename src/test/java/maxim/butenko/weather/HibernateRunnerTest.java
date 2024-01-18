package maxim.butenko.weather;

import maxim.butenko.weather.entity.User;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Table;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

class HibernateRunnerTest {
    @Test
    void checkReflectionAPI() throws SQLException, IllegalAccessException {
        User user = User.builder()
                .id(1L)
                .login("admin")
                .password("admin")
                .build();
        String sql = """
                insert
                into
                %s
                (%s)
                values
                (%s)
                """;


        Field[] declaredFields = user.getClass().getDeclaredFields();

        String tableName = Optional.ofNullable(user.getClass().getAnnotation(Table.class))
                .map(tableAnnotation -> tableAnnotation.schema() + "." + tableAnnotation.name())
                .orElse(user.getClass().getName());

        String columnNames = Arrays.stream(declaredFields)
                .map(field -> Optional.ofNullable(field.getAnnotation(Column.class))
                        .map(Column::name)
                        .orElse(field.getName()))
                .collect(joining(", "));

        String columnValues = Arrays.stream(declaredFields)
                .map(field -> "?")
                .collect(joining(", "));

        System.out.println(sql.formatted(tableName, columnNames, columnValues));

        Connection connection = null;
        PreparedStatement prepareStatement = connection.prepareStatement(sql.formatted(tableName, columnNames, columnValues));
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            prepareStatement.setObject(1, declaredField.get(user));
        }
    }
}