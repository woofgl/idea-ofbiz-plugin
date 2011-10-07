package com.cfsoft.ofbiz.dom.entity;

import com.cfsoft.ofbiz.dom.entity.api.EntityModel;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NotNullLazyKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 10/2/11
 * Time: 1:47 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class EntityModelManger {
      private static final NotNullLazyKey<EntityModelManger, Project> INSTANCE_KEY = ServiceManager.createLazyKey(EntityModelManger.class);

  public static EntityModelManger getInstance(@NotNull final Project project) {
    return INSTANCE_KEY.getValue(project);
  }
    public abstract List<EntityModel> getEntityModels();
}
