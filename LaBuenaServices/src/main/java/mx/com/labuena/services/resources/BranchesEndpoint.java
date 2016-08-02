package mx.com.labuena.services.resources;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.inject.Inject;

import java.util.List;

import mx.com.labuena.services.dao.BranchDao;
import mx.com.labuena.services.responses.BranchesResponse;
import mx.com.labuena.services.tos.Branch;

/**
 * Created by moracl6 on 8/2/2016.
 */
@Api(
        name = "branches",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "services.labuena.com.mx",
                ownerName = "services.labuena.com.mx",
                packagePath = ""
        )
)
public class BranchesEndpoint {
    @Inject
    private BranchDao branchDao;

    @ApiMethod(name = "save",
            httpMethod = ApiMethod.HttpMethod.POST)
    public void save(Branch branch) throws InternalServerErrorException {
        branchDao.save(branch);
    }

    @ApiMethod(name = "getAll",
            httpMethod = ApiMethod.HttpMethod.GET)
    public BranchesResponse getAll() throws InternalServerErrorException {
        List<Branch> branches = branchDao.getAll();
        return new BranchesResponse(branches);
    }
}
