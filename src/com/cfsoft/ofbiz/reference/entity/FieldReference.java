package com.cfsoft.ofbiz.reference.entity;

import com.cfsoft.ofbiz.OfbizUtils;
import com.cfsoft.ofbiz.dom.entity.EntityModelManger;
import com.cfsoft.ofbiz.dom.entity.api.*;
import com.cfsoft.ofbiz.facet.OfbizFacet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FieldReference extends PsiReferenceBase<XmlAttribute> {

    public FieldReference(@NotNull final XmlAttribute xmlTag) {
        super(xmlTag, false);
    }

    @SuppressWarnings({"unchecked"})
    public PsiElement resolve() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return myElement;
        }
        XmlTag entityTag = myElement.getParent();
        String tagName;
        while (entityTag != null) {
            tagName = entityTag.getLocalName();
            if (tagName.equals("entity") || tagName.equals("extend-entity")) {
                break;
            } else {
                entityTag = entityTag.getParentTag();
            }
        }

        if (entityTag != null) {
//            Entity entity = (Entity) DomUtil.getDomElement(entityTag);
            Entity entity = OfbizUtils.findEntity(myElement.getProject(), entityTag.getAttributeValue("entity-name"));
            if (entity != null) {
                Field find = ContainerUtil.find(entity.getAllFields(), new Condition<Field>() {
                    @Override
                    public boolean value(Field field) {
                        return myElement.getValue().equals(field.getName().getValue());
                    }
                });
            return find == null ? null : find.getXmlElement();
            }
        } else {
            entityTag = myElement.getParent();
            while (entityTag != null && !entityTag.getLocalName().equals("view-entity")) {
                entityTag = entityTag.getParentTag();
            }
            if (entityTag != null) {
                ViewEntity viewEntity = (ViewEntity) DomUtil.getDomElement(entityTag);
                List<MemberEntity> memberEntities = viewEntity.getmMemberEntitys();
                if (memberEntities.size() > 0) {
                    Map<String, Entity> aliasMap = new HashMap<String, Entity>();
                    for (MemberEntity memberEntity : memberEntities) {
                        aliasMap.put(memberEntity.getEntityAlias().getValue().trim(),
                                memberEntity.getEntity());
                    }
                    final List<AliasAll> aliasAlls = viewEntity.getAliasAlls();
                    final List<DomElement> fields = new ArrayList<DomElement>();
                    if (aliasAlls != null) {
                        for (AliasAll aliasAll : aliasAlls) {
                            Entity entity = aliasMap.get(aliasAll.getEntityAlias().getValue().trim());
                            if (entity != null) {
                                fields.addAll(entity.getAllFields());
                            }
                        }
                    }
                    final List<Alias> aliases = viewEntity.getAliass();
                    if (aliases != null) {
                        for (Alias alias : aliases) {
                            Entity entity = aliasMap.get(alias.getEntityAlias().getValue().trim());
                            for (Field field : entity.getAllFields()) {
                                String fieldName = alias.getField().getValue() == null ? alias.getName().getValue() : alias.getField().getValue().trim();
                                if (field.getName().getValue().equals(fieldName)) {
                                    fields.add(alias);
                                }
                            }
                        }
                    }
                    for (DomElement field : fields) {
                        if (((XmlTag)field.getXmlElement()).getAttributeValue("name").equals(myElement.getValue().trim())) {
                            return field.getXmlElement();
                        }
                    }

                }

            }
        }

        return null;
    }


    @NotNull
    @SuppressWarnings({"unchecked"})
    public Object[] getVariants() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return new Object[0];
        }
        XmlTag entityTag = myElement.getParent();
        String tagName;
        while (entityTag != null) {
            tagName = entityTag.getLocalName();
            if (tagName.equals("entity") || tagName.equals("extend-entity")) {
                break;
            } else {
                entityTag = entityTag.getParentTag();
            }
        }

        if (entityTag != null) {
            final Entity entity = OfbizUtils.findEntity(myElement.getProject(), entityTag.getAttributeValue("entity-name"));
            if(entity!=null){
                return ContainerUtil.map2Array(entity.getAllFields(), new Function<Field, Object>() {
                    @Override
                    public Object fun(Field field) {
                        return LookupElementBuilder.create(field.getName().getValue()).
                                setTailText("  " + field.getType().getValue()).setTypeText(entity.getName().getValue());
                    }
                });
            }
        } else {
            entityTag = myElement.getParent();
            while (entityTag != null && !entityTag.getLocalName().equals("view-entity")) {
                entityTag = entityTag.getParentTag();
            }
            if (entityTag != null) {
                ViewEntity viewEntity = (ViewEntity) DomUtil.getDomElement(entityTag);
                List<MemberEntity> memberEntities = viewEntity.getmMemberEntitys();
                if (memberEntities.size() > 0) {
                    Map<String, Entity> aliasMap = new HashMap<String, Entity>();
                    for (MemberEntity memberEntity : memberEntities) {
                        aliasMap.put(memberEntity.getEntityAlias().getValue().trim(),
                                memberEntity.getEntity());
                    }
                    final List<AliasAll> aliasAlls = viewEntity.getAliasAlls();
                    final List<DomElement> fields = new ArrayList<DomElement>();
                    if (aliasAlls != null) {
                        for (AliasAll aliasAll : aliasAlls) {
                            Entity entity = aliasMap.get(aliasAll.getEntityAlias().getValue().trim());
                            if (entity != null) {
                                fields.addAll(entity.getAllFields());
                            }
                        }
                    }
                    final List<Alias> aliases = viewEntity.getAliass();
                    if (aliases != null) {
                        for (Alias alias : aliases) {
                            Entity entity = aliasMap.get(alias.getEntityAlias().getValue().trim());
                            String fieldName = alias.getField().getValue() == null ? alias.getName().getValue().trim() : alias.getField().getValue().trim();
                            for (Field field : entity.getAllFields()) {
                                if (field.getName().getValue().equals(fieldName)) {
                                    fields.add(alias);
                                }
                            }
                        }
                    }
                    return ContainerUtil.map2Array(fields, new Function<DomElement, Object>() {
                        @Override
                        public Object fun(DomElement field) {
                            return LookupElementBuilder.create((field.getXmlTag()).getAttributeValue("name")).
                                    setTypeText(field.getParent().getXmlTag().getAttributeValue("entity-name"));
                        }
                    });
                }

            }
        }
        return new Object[0];

    }

    private Entity findEntity(Project project, @NotNull final String entityName) {
        for (EntityModel model : EntityModelManger.getInstance(project).getEntityModels()) {
            Entity entity = ContainerUtil.find(model.getEntities(), new Condition<Entity>() {
                @Override
                public boolean value(Entity entity) {
                    return entityName.equals(entity.getName().getValue());
                }
            });
            if (entity != null) {
                return entity;
            }
        }
        return null;

    }
}