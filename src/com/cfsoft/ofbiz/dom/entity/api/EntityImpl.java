package com.cfsoft.ofbiz.dom.entity.api;

import com.cfsoft.ofbiz.OfbizUtils;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityImpl implements Entity {
    @Override
    public List<Field> getAllFields() {
        Project project = getXmlElement().getProject();
        List<EntityModel> models = OfbizUtils.getDomFileElements(EntityModel.class, project, GlobalSearchScope.projectScope(project));

        final List<Field> fields =new ArrayList<Field>();
        fields.addAll(getFields());
        for (EntityModel model : models) {
            if (model.getExtendEntities() != null) {
                for (ExtendEntity extendEntity : model.getExtendEntities()) {
                     if(extendEntity.getEntityName().getValue().equals(getName().getValue())) {
                         fields.addAll(extendEntity.getFields());
                     }
                }
            }
        }
        return fields;
    }
}
