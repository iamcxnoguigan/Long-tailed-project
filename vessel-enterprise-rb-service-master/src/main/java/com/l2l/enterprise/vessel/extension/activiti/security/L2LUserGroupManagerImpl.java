package com.l2l.enterprise.vessel.extension.activiti.security;

import com.l2l.enterprise.vessel.security.SecurityUtils;
import org.activiti.runtime.api.identity.UserGroupManager;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class L2LUserGroupManagerImpl implements UserGroupManager {
    @Override
    public List<String> getUserGroups(String s) {
        return SecurityUtils.getUserGroups(s);
    }

    @Override
    public List<String> getUserRoles(String s) {
        return SecurityUtils.getUserRoles(s);
    }
}
