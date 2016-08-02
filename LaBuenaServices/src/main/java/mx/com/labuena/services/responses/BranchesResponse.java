package mx.com.labuena.services.responses;

import java.util.List;

import mx.com.labuena.services.tos.Branch;

/**
 * Created by moracl6 on 8/2/2016.
 */

public class BranchesResponse {
    private List<Branch> branches;

    public BranchesResponse() {
    }

    public BranchesResponse(List<Branch> branches) {
        this.branches = branches;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public void setBranches(List<Branch> branches) {
        this.branches = branches;
    }
}
