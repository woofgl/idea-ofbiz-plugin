package com.cfsoft.ofbiz.dom.entity.api;

import com.cfsoft.ofbiz.dom.entity.EntityModelManger;
import com.intellij.openapi.util.Condition;
import com.intellij.util.containers.ContainerUtil;

import java.util.List;


public abstract class MemberEntityImpl implements MemberEntity {
    @Override
    public Entity getEntity() {
        List<EntityModel> list = EntityModelManger.getInstance(getXmlElement().getProject()).getEntityModels();
        final String entityName = getEntityName().getValue().trim();
        for (EntityModel entityModel : list) {
            Entity entity = ContainerUtil.find(entityModel.getEntities(), new Condition<Entity>() {
                @Override
                public boolean value(Entity entity) {
                    return entity.getName().getValue().equals(entityName);
                }
            });
            if (entity != null) {
                return entity;
            }
        }
        return null;
    }
}
