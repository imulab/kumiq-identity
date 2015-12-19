package com.kumiq.identity.scim.endpoint.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kumiq.identity.scim.path.ModificationUnit;

import java.util.List;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PatchOpBody {

    @JsonProperty("Operations")
    private List<ModificationUnit> operations;

    public List<ModificationUnit> getOperations() {
        return operations;
    }

    public void setOperations(List<ModificationUnit> operations) {
        this.operations = operations;
    }
}
