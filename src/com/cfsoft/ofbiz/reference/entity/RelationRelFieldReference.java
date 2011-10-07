package com.cfsoft.ofbiz.reference.entity;

import com.cfsoft.ofbiz.dom.component.ComponentManager;
import com.cfsoft.ofbiz.dom.component.api.Component;
import com.cfsoft.ofbiz.dom.entity.api.Entity;
import com.cfsoft.ofbiz.dom.entity.api.Field;
import com.cfsoft.ofbiz.facet.OfbizFacet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.ide.structureView.impl.java.FieldsFilter;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.DomUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;


public class RelationRelFieldReference extends PsiReferenceBase<XmlAttribute> {

    public RelationRelFieldReference(@NotNull final XmlAttribute xmlTag) {
        super(xmlTag, false);
    }

    @SuppressWarnings({"unchecked"})
    public PsiElement resolve() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return myElement;
        }
        XmlTag keyMapTag = myElement.getParent();
        XmlTag relationTag = keyMapTag.getParentTag();
        final String entityName = relationTag.getAttributeValue("rel-entity-name");

        if (entityName == null) {
            return myElement;
        }
        List<Entity> entities = ContainerUtil.concat(ComponentManager.getInstance(myElement.getProject()).getAllComponents(),new Function<Component, Collection<? extends Entity>>() {
            @Override
            public Collection<? extends Entity> fun(Component component) {
                return component.getAllEntities();
            }
        });
        Entity entity = ContainerUtil.find(entities, new Condition<Entity>() {
            @Override
            public boolean value(Entity entity) {
                return entity.getName().getValue().equals(entityName);
            }
        });
        if (entity == null) {
            return myElement;
        }
        Field field = ContainerUtil.find(entity.getAllFields(), new Condition<Field>() {
            @Override
            public boolean value(Field field) {
                return field.getName().getValue().equals(myElement.getValue());
            }
        });
        return field==null?null:field.getXmlElement();

    }


    @NotNull
    @SuppressWarnings({"unchecked"})
    public Object[] getVariants() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return new Object[0];
        }
        XmlTag keyMapTag = myElement.getParent();
        XmlTag relationTag = keyMapTag.getParentTag();
        final String entityName = relationTag.getAttributeValue("rel-entity-name");

        if (entityName == null) {
            return new Object[0];
        }
        final Component[] components = ComponentManager.getInstance(myElement.getProject()).getAllComponents();
        List<Entity> entities = ContainerUtil.concat(components,new Function<Component, Collection<? extends Entity>>() {
            @Override
            public Collection<? extends Entity> fun(Component component) {
                return component.getAllEntities();
            }
        });
        final Entity entity = ContainerUtil.find(entities, new Condition<Entity>() {
            @Override
            public boolean value(Entity entity) {
                return entity.getName().getValue().equals(entityName);
            }
        });
        if (entity == null) {
            return new Object[0];
        }

        return ContainerUtil.map2Array(entity.getAllFields(), new Function<Field, Object>() {
            @Override
            public Object fun(Field field) {
                Component component = ComponentManager.getInstance(myElement.getProject())
                        .getComponent(field.getXmlElement(), components);
                return LookupElementBuilder.create(field.getName().getValue()).
                        setTypeText(field.getXmlElement().getContainingFile().getText())
                        .setTypeText(component.getName().getValue()+":"+entity.getName().getValue());
            }
        });

    }
}