package org.activiti.cloud.starter.rb.extension.cache;

import org.activiti.engine.impl.persistence.deploy.DefaultDeploymentCache;

import java.util.Map;

public class L2LDeploymentCache<T> extends DefaultDeploymentCache<T> {
    public L2LDeploymentCache() {
        super();
    }

    public L2LDeploymentCache(final int limit) {
        super(limit);
    }

    public Map<String, T>  getCache(){
        return this.cache;
    }
}
