package best.azura.irc.sql;

import best.azura.irc.main.Main;
import best.azura.irc.sql.base.annotations.Table;
import best.azura.irc.sql.base.entities.SQLEntity;
import best.azura.irc.sql.base.entities.SQLParameter;
import best.azura.irc.sql.base.entities.SQLResponse;
import best.azura.irc.sql.base.utils.SQLUtil;
import org.reflections.Reflections;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A Class to actually handle the SQL data.
 * Used to provide Data from the Database and to save Data into the Database.
 * <p>
 * Constructor to create a new Instance of the SQLWorker with a ref to the SQL-Connector.
 *
 * @param sqlConnector an Instance of the SQL-Connector to retrieve the data from.
 */
@SuppressWarnings({"SqlNoDataSourceInspection", "SqlResolve", "unused", "SingleStatementInBlock"})
public record SQLWorker(SQLConnector sqlConnector) {

    //region Entity-System

    /**
     * Create a Table for the Entity.
     *
     * @param entity the Entity.
     * @return {@link Boolean} as result. If true, the Table was created | If false, the Table was not created.
     */
    public boolean createTable(Class<? extends SQLEntity> entity) {
        if (!entity.isAnnotationPresent(Table.class)) {
            return false;
        }

        String tableName = SQLUtil.getTable(entity);
        List<SQLParameter> sqlParameters = SQLUtil.getAllSQLParameter(entity, false);

        if (sqlParameters.isEmpty()) {
            return false;
        }

        if (tableName == null) {
            return false;
        }

        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS ");
        query.append(tableName);
        query.append(" (");
        sqlParameters.forEach(parameter -> {
            query.append(parameter.getName());
            query.append(" ");
            query.append(SQLUtil.mapJavaToSQL(parameter.getValue()));
            query.append(", ");
        });

        sqlParameters.stream().filter(SQLParameter::isPrimaryKey).findFirst().ifPresent(primaryKey -> {
            query.append("PRIMARY KEY (");
            query.append(primaryKey.getName());
            query.append(")");
        });

        if (query.charAt(query.length() - 2) == ',') {
            query.delete(query.length() - 2, query.length());
        }

        query.append(")");

        try {
            sqlConnector.querySQL(query.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Save an Entity to the Database.
     *
     * @param entity the Entity to save.
     */
    public void saveEntity(Object entity) {
        Class<?> entityClass = entity.getClass();

        if (!entityClass.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("Entity must be annotated with @Table! (" + entityClass.getSimpleName() + ")");
        }

        String tableName = SQLUtil.getTable(entityClass);
        List<SQLParameter> sqlParameters = SQLUtil.getAllSQLParameter(entity, false, false);

        if (sqlParameters.isEmpty()) {
            return;
        }

        if (tableName == null) {
            return;
        }

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ");
        query.append(tableName);
        query.append(" (");
        sqlParameters.forEach(parameter -> {
            query.append(parameter.getName());
            query.append(", ");
        });

        if (query.charAt(query.length() - 2) == ',') {
            query.delete(query.length() - 2, query.length());
        }

        query.append(") VALUES (");

        query.append("?, ".repeat(sqlParameters.size()));

        if (query.charAt(query.length() - 2) == ',') {
            query.delete(query.length() - 2, query.length());
        }

        query.append(")");
        try {
            sqlConnector.querySQL(query.toString(), SQLUtil.getValuesFromSQLEntity(entityClass, entity, false, false).toArray());
        } catch (Exception exception) {
            Main.getInstance().getLogger().error("Error while saving Entity: " + entity, exception);
        }
    }

    /**
     * Update an Entity in the Database.
     *
     * @param oldEntity       the old Entity.
     * @param newEntity       the new Entity.
     * @param onlyUpdateField the only update the given Field.
     */
    public void updateEntity(Object oldEntity, Object newEntity, boolean onlyUpdateField) {
        Class<?> entityClass = oldEntity.getClass();

        if (!entityClass.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("Entities must be annotated with @Table! (" + ((Class) oldEntity).getSimpleName() + ")");
        }

        if (!oldEntity.getClass().equals(newEntity.getClass())) {
            throw new IllegalArgumentException("Entities must be of the same type");
        }

        String tableName = SQLUtil.getTable(entityClass);
        List<SQLParameter> sqlParameters = SQLUtil.getAllSQLParameter(newEntity, onlyUpdateField, true);

        if (sqlParameters.isEmpty()) {
            return;
        }

        if (tableName == null) {
            return;
        }

        StringBuilder query = new StringBuilder();
        query.append("UPDATE ");
        query.append(tableName);
        query.append(" SET ");
        sqlParameters.forEach(parameter -> {
            query.append(parameter.getName());
            query.append(" = ?, ");
        });

        if (query.indexOf(",", query.length() - 2) != -1) {
            query.delete(query.length() - 2, query.length());
        }


        query.append(" WHERE ");
        SQLUtil.getAllSQLParameter(oldEntity, false, true).forEach(parameter -> {
            query.append(parameter.getName());
            query.append(" = ? AND ");
        });

        if (query.indexOf("AND", query.length() - 5) != -1) {
            query.delete(query.length() - 5, query.length());
        }

        try {
            ArrayList<Object> parameter = new ArrayList<>();

            parameter.addAll(SQLUtil.getValuesFromSQLEntity(entityClass, newEntity, onlyUpdateField, true));
            parameter.addAll(SQLUtil.getValuesFromSQLEntity(entityClass, oldEntity, false, true));

            sqlConnector.querySQL(query.toString(), parameter.toArray());
        } catch (Exception exception) {
            Main.getInstance().getLogger().error("Error while updating Entity: " + ((Class) oldEntity).getSimpleName(), exception);
        }
    }

    /**
     * Delete an entity from the database
     *
     * @param entity the Entity-class instance that is to be deleted.
     */
    public void deleteEntity(Object entity) {
        Class<?> entityClass = entity.getClass();

        if (!entityClass.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("Entity must be annotated with @Table! (" + ((Class) entity).getSimpleName() + ")");
        }

        String tableName = SQLUtil.getTable(entityClass);
        List<SQLParameter> sqlParameters = SQLUtil.getAllSQLParameter(entity, false, true);

        if (sqlParameters.isEmpty()) {
            return;
        }

        if (tableName == null) {
            return;
        }

        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM ");
        query.append(tableName);
        query.append(" WHERE ");
        sqlParameters.forEach(parameter -> {
            query.append(parameter.getName());
            query.append("= ? AND ");
        });

        if (query.indexOf("AND", query.length() - 4) != -1) {
            query.delete(query.length() - 5, query.length());
        }

        try {
            sqlConnector.querySQL(query.toString(), SQLUtil.getValuesFromSQLEntity(entityClass, entity, false, true).toArray());
        } catch (Exception exception) {
            Main.getInstance().getLogger().error("Error while deleting Entity: " + ((Class) entity).getSimpleName(), exception);
        }
    }

    /**
     * Constructs a new mapped Version of the Entity-class.
     *
     * @param entity The entity to get.
     * @return The mapped entity.
     */
    public SQLResponse getEntity(Class<?> entity) {
        return getEntity(entity, "");
    }

    /**
     * Constructs a query for the given Class-Entity, and returns a mapped Version of the given Class-Entity.
     *
     * @param entity The Class-Entity to get.
     * @param query  The query to use.
     * @param args   The arguments to use.
     * @return The mapped Version of the given Class-Entity.
     */
    public SQLResponse getEntity(Class<?> entity, String query, Object... args) {
        if (query.isEmpty()) {
            if (entity.isAnnotationPresent(Table.class)) {
                String queryBuilder = "SELECT * FROM " + SQLUtil.getTable(entity);
                return sqlConnector.getEntityMapper().mapEntity(sqlConnector.querySQL(queryBuilder, args), entity);
            } else {
                return new SQLResponse(null);
            }
        } else {
            return sqlConnector.getEntityMapper().mapEntity(sqlConnector.querySQL(query, args), entity);
        }
    }

    //endregion
}