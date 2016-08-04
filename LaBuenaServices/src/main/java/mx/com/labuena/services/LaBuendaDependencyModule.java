package mx.com.labuena.services;

import com.google.inject.AbstractModule;

import mx.com.labuena.services.dao.MysqlBikerDao;
import mx.com.labuena.services.dao.MysqlBranchDao;
import mx.com.labuena.services.dao.MysqlClientDao;
import mx.com.labuena.services.dao.MysqlOrderDao;
import mx.com.labuena.services.messaging.FirebaseCloudMessageNotifier;
import mx.com.labuena.services.messaging.MessageNotifier;
import mx.com.labuena.services.models.BikeDriverSelector;
import mx.com.labuena.services.models.BikerDao;
import mx.com.labuena.services.models.BranchDao;
import mx.com.labuena.services.models.ClientDao;
import mx.com.labuena.services.models.OrderDao;

/**
 * LaBuena dependency graph.
 * Created by moracl6 on 8/2/2016.
 */

public class LaBuendaDependencyModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MessageNotifier.class).to(FirebaseCloudMessageNotifier.class);
        bind(BikerDao.class).to(MysqlBikerDao.class);
        bind(BranchDao.class).to(MysqlBranchDao.class);
        bind(ClientDao.class).to(MysqlClientDao.class);
        bind(OrderDao.class).to(MysqlOrderDao.class);
        bind(BikeDriverSelector.class).to(MysqlBikerDao.class);
    }
}
