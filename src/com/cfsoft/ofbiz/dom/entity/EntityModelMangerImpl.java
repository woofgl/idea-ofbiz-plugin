package com.cfsoft.ofbiz.dom.entity;

import com.cfsoft.ofbiz.OfbizUtils;
import com.cfsoft.ofbiz.dom.entity.api.EntityModel;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;

import java.lang.ref.SoftReference;
import java.util.List;


public class EntityModelMangerImpl extends EntityModelManger {
    private final Project project;
    private SoftReference<List<EntityModel>> models;

    public EntityModelMangerImpl(final Project project) {
        this.project = project;
    }

    @Override
    public List<EntityModel> getEntityModels() {
        if (models != null && models.get() != null) {
            return models.get();
        }else {
            List<EntityModel> entityModels = OfbizUtils.
                    getDomFileElements(EntityModel.class, project, GlobalSearchScope.projectScope(project));
            models = new SoftReference(entityModels);
            return entityModels;
        }
    }
}
