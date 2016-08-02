package mx.com.labuena.services;

import com.google.inject.AbstractModule;

import mx.com.labuena.services.dao.BikerDao;
import mx.com.labuena.services.dao.BranchDao;
import mx.com.labuena.services.dao.MysqlBikerDao;
import mx.com.labuena.services.dao.MysqlBranchDao;

/**
 * LaBuena dependency graph.
 * Created by moracl6 on 8/2/2016.
 */

public class LaBuendaDependencyModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BikerDao.class).to(MysqlBikerDao.class);
        bind(BranchDao.class).to(MysqlBranchDao.class);
    }
}
