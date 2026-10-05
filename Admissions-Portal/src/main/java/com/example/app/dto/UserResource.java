package com.example.app.dto;

import java.util.Map;
import com.example.app.annotation.MethodMetadata;

public class UserResource {

    private Map<String, Object> authMap;

    private Map<String, Object> resourceMap;

    private String resourceName;

    @MethodMetadata(irId = "custom:dto:UserResource:getResourceName()", hash = "89ed1ac9", zone = 1)
    public String getResourceName() {
        return resourceName;
    }

    @MethodMetadata(irId = "custom:dto:UserResource:setResourceName(String)", hash = "7909e065", zone = 1)
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    @MethodMetadata(irId = "custom:dto:UserResource:getAuthMap()", hash = "8ea04941", zone = 1)
    public Map<String, Object> getAuthMap() {
        return authMap;
    }

    @MethodMetadata(irId = "custom:dto:UserResource:setAuthMap(Map<String,Object>)", hash = "93474cc6", zone = 1)
    public void setAuthMap(Map<String, Object> authMap) {
        this.authMap = authMap;
    }

    @MethodMetadata(irId = "custom:dto:UserResource:getResourceMap()", hash = "2bd66a25", zone = 1)
    public Map<String, Object> getResourceMap() {
        return resourceMap;
    }

    @MethodMetadata(irId = "custom:dto:UserResource:setResourceMap(Map<String,Object>)", hash = "ef2e2bdb", zone = 1)
    public void setResourceMap(Map<String, Object> resourceMap) {
        this.resourceMap = resourceMap;
    }
}
