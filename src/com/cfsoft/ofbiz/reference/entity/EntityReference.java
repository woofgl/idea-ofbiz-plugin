package com.cfsoft.ofbiz.reference.entity;

import com.cfsoft.ofbiz.OfbizUtils;
import com.cfsoft.ofbiz.dom.component.ComponentManager;
import com.cfsoft.ofbiz.dom.component.api.Component;
import com.cfsoft.ofbiz.dom.entity.api.Entity;
import com.cfsoft.ofbiz.dom.entity.api.EntityModel;
import com.cfsoft.ofbiz.facet.OfbizFacet;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.DomManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;


public class EntityReference extends PsiReferenceBase<XmlAttribute> {

    public EntityReference(@NotNull final XmlAttribute xmlTag) {
        super(xmlTag, false);
    }

    @SuppressWarnings({"unchecked"})
    public PsiElement resolve() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return myElement;
        }
        EntityModel model = DomManager.getDomManager(myElement.getProject()).
                getFileElement((XmlFile) myElement.getContainingFile(), EntityModel.class).getRootElement();
        final String entityName = myElement.getValue().trim();
        Entity entity = ContainerUtil.find(model.getEntities(), new Condition<Entity>() {
            @Override
            public boolean value(Entity entity) {
                return entityName.trim().equals(entity.getName().getValue());
            }
        });
        if (entity != null) {
            return entity.getXmlElement();
        }
        List<EntityModel> models = OfbizUtils.getDomFileElements(EntityModel.class,
                myElement.getProject(), GlobalSearchScope.projectScope(myElement.getProject()));
        for (EntityModel entityModel : models) {
            if(entityModel.getXmlElement().getContainingFile().getVirtualFile().getPath().
                    equals(model.getXmlElement().getContainingFile().getVirtualFile().getPath())){
                continue;
            }else{
                entity = ContainerUtil.find(entityModel.getEntities(), new Condition<Entity>() {
                    @Override
                    public boolean value(Entity entity) {
                        return entity.getName().getValue().equals(entityName);
                    }
                });
                if (entity != null) {
                    return entity.getXmlElement();
                }
            }
        }
        return myElement;
    }


    @NotNull
    @SuppressWarnings({"unchecked"})
    public Object[] getVariants() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return new Object[0];
        }
        final ComponentManager manager = ComponentManager.getInstance(myElement.getProject());
        final Component[] components = ComponentManager.getInstance(myElement.getProject()).getAllComponents();
        List<Entity> entities = ContainerUtil.concat(components, new Function<Component, Collection<? extends Entity>>() {
            @Override
            public Collection<? extends Entity> fun(Component component) {
                return component.getAllEntities();
            }
        });

        return ContainerUtil.map2Array(entities, new Function<Entity, Object>() {
            @Override
            public Object fun(Entity entity) {
               //String url = manager.getComponentUrl(entity.getXmlElement(), components);
               // return LookupElementBuilder.create(entity.getName().getValue()).setTypeText(url);
                return entity.getName().getValue();
            }
        });

    }
}