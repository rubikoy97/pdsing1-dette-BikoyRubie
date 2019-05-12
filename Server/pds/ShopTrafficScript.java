package pds.mock;

import pds.dbAccess.ShopDbAccess;
import pds.dbAccess.ShopTrafficDbAccess;
import pds.model.Shop;
import pds.model.ShopTraffic;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ShopTrafficScript {

    private static int id = 1;

    public static void insertAll() throws SQLException {
        for (int i = 1; i <= 10; i++) {
            Shop shop = ShopDbAccess.find(i);

            for (int j = 1; j <= 12; j++) {

                for (int k = 1; k <= 30; k++) {
                    if (j == 2 && k == 29) break;

                    int traffic = new Random().nextInt(5000 - 500 + 1) + 500;

                    Date date = Date.valueOf(LocalDate.of(2018, j, k));

                    ShopTraffic shopTraffic = new ShopTraffic();
                    shopTraffic.setId(id);
                    shopTraffic.setShop(shop);
                    shopTraffic.setTraffic(traffic);
                    shopTraffic.setDate(date);

                    ShopTrafficDbAccess.create(id, shopTraffic);

                    id++;
                }

            }
        }
    }
}
