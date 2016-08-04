package mx.com.labuena.services.resources;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.inject.Inject;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import mx.com.labuena.services.models.BranchDao;
import mx.com.labuena.services.responses.BranchesResponse;
import mx.com.labuena.services.models.Branch;

/**
 * Created by moracl6 on 8/2/2016.
 */
@Api(
        name = "branches",
        version = "v2",
        namespace = @ApiNamespace(
                ownerDomain = "services.labuena.com.mx",
                ownerName = "services.labuena.com.mx",
                packagePath = ""
        )
)
public class BranchesEndpoint {
    private static final Logger log = Logger.getLogger(BranchesEndpoint.class.getName());

    @Inject
    private BranchDao branchDao;

    @ApiMethod(name = "save",
            httpMethod = ApiMethod.HttpMethod.POST)
    public void save(Branch branch) throws InternalServerErrorException {
        branchDao.save(branch);
    }

    @ApiMethod(name = "getAll",
            httpMethod = ApiMethod.HttpMethod.GET)
    public BranchesResponse getAll() throws InternalServerErrorException, SQLException {
        List<Branch> branches = branchDao.getAll();
        return new BranchesResponse(branches);
    }
}
