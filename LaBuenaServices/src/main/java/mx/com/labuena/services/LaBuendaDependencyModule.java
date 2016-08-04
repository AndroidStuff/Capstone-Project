package mx.com.labuena.services;

import com.google.inject.AbstractModule;

import java.sql.Connection;

import mx.com.labuena.services.dao.BikerDao;
import mx.com.labuena.services.dao.BranchDao;
import mx.com.labuena.services.dao.ClientDao;
import mx.com.labuena.services.dao.ConnectionProvider;
import mx.com.labuena.services.dao.MysqlBikerDao;
import mx.com.labuena.services.dao.MysqlBranchDao;
import mx.com.labuena.services.dao.MysqlClientDao;
import mx.com.labuena.services.messaging.FirebaseCloudMessageNotifier;
import mx.com.labuena.services.messaging.MessageNotifier;

/**
 * LaBuena dependency graph.
 * Created by moracl6 on 8/2/2016.
 */

public class LaBuendaDependencyModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Connection.class).toProvider(new ConnectionProvider());
        bind(MessageNotifier.class).to(FirebaseCloudMessageNotifier.class);
        bind(BikerDao.class).to(MysqlBikerDao.class);
        bind(BranchDao.class).to(MysqlBranchDao.class);
        bind(ClientDao.class).to(MysqlClientDao.class);

    }
}
