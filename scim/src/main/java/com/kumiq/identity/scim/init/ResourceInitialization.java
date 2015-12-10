package com.kumiq.identity.scim.init;

import com.kumiq.identity.scim.resource.core.Resource;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public interface ResourceInitialization<T extends Resource> extends InitializingBean {

    /**
     * Perform bootstrap from any source
     *
     * @return
     */
    List<T> bootstrap();

    /**
     * Install the bootstrapped resources into database
     *
     * @param resources
     */
    void install(List<T> resources);
}
