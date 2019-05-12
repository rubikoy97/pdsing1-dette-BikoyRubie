package pds.dbAccess;

import pds.Server;
import pds.model.Shop;
import pds.model.ShopTraffic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class ShopTrafficDbAccess {

    public static boolean create(int id, ShopTraffic shopTraffic) throws SQLException {
        String sql = "INSERT INTO shop_traffic(id, shop_id, traffic, date)VALUES (?,?,?,?);" ;

        Connection c = Server.connectionPool.getConnection() ;
        PreparedStatement req = c.prepareStatement(sql) ;

        req.setInt(1, id);
        req.setInt(2, shopTraffic.getShop().getId());
        req.setInt(3, shopTraffic.getTraffic());
        req.setDate(4, shopTraffic.getDate());

        if (req.executeUpdate() == 1) {
            req.close();
            Server.connectionPool.releaseConnection(c);
            return true;
        }

        req.close();
        Server.connectionPool.releaseConnection(c);
        return false;
    }

    public static List<ShopTraffic> find(int id, int monthId) throws SQLException {

        ArrayList<ShopTraffic> list = new ArrayList<>();

        String sql = "select * from shop_traffic where shop_id = ? and MONTH(date) = ?";

        Connection c = Server.connectionPool.getConnection() ;
        PreparedStatement req = c.prepareStatement(sql) ;

        req.setInt(1, id);
        req.setInt(2, monthId);

        ResultSet rs = req.executeQuery();

        while (rs.next()) {

            int shopTrafficId = rs.getInt(1);
            int shopId = rs.getInt(2);
            int traffic = rs.getInt(3);
            Date date = rs.getDate(4);

            ShopTraffic shopTraffic = new ShopTraffic();
            shopTraffic.setId(shopTrafficId);
            shopTraffic.setShop(ShopDbAccess.find(shopId));
            shopTraffic.setTraffic(traffic);
            shopTraffic.setDate(date);
            list.add(shopTraffic);

        }
        req.close();
        Server.connectionPool.releaseConnection(c);

        return list;
    }

}
