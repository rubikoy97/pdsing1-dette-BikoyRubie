package pds.pool;

import java.sql.Connection;

public interface IConnectionPool {

    Connection getConnection();
    void releaseConnection(Connection connection);
    void closeAll();

}
