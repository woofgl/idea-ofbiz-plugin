package com.cfsoft.ofbiz.dom.entity.api;

import com.cfsoft.ofbiz.OfbizUtils;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.indexing.FileBasedIndex;
import com.intellij.xml.index.XmlTagNamesIndex;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 10/5/11
 * Time: 7:58 PM
 * To change this template use File | Settings | File Templates.
 */
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
