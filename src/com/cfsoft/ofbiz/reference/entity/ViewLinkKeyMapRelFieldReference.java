package com.cfsoft.ofbiz.reference.entity;

import com.cfsoft.ofbiz.OfbizUtils;
import com.cfsoft.ofbiz.dom.entity.api.*;
import com.cfsoft.ofbiz.facet.OfbizFacet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.DomManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ViewLinkKeyMapRelFieldReference extends PsiReferenceBase<XmlAttribute> {

    public ViewLinkKeyMapRelFieldReference(@NotNull final XmlAttribute xmlTag) {
        super(xmlTag, false);
    }

    @SuppressWarnings({"unchecked"})
    public PsiElement resolve() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return myElement;
        }
        XmlAttribute attribute;
        if (myElement.getLocalName().equals("field-name")) {
            attribute = myElement.getParent().getParentTag().getAttribute("entity-alias");
        } else {
            attribute = myElement.getParent().getParentTag().getAttribute("rel-entity-alias");
        }
        Entity entity = findEntity(attribute);
        if (entity != null) {
            Field field = ContainerUtil.find(entity.getAllFields(), new Condition<Field>() {
                @Override
                public boolean value(Field field) {
                    return field.getName().getValue().equals(myElement.getValue());
                }
            });
            if (field != null) {
                return field.getXmlElement();
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
        XmlAttribute attribute;
        if (myElement.getLocalName().equals("field-name")) {
            attribute = myElement.getParent().getParentTag().getAttribute("entity-alias");
        } else {
            attribute = myElement.getParent().getParentTag().getAttribute("rel-entity-alias");
        }
        final Entity entity = findEntity(attribute);
        if (entity != null) {
            return ContainerUtil.map2Array(entity.getAllFields(),new Function<Field, Object>() {
                @Override
                public Object fun(Field field) {
                    return LookupElementBuilder.create(field.getName().getValue()).setTypeText(entity.getName().getValue());
                }
            });
        }else{
            return new Object[0];
        }

    }

    private Entity findEntity(XmlAttribute xmlAttribute) {
        XmlFile xmlFile = (XmlFile) xmlAttribute.getContainingFile();
        if (!xmlFile.getRootTag().getLocalName().equals("entitymodel")) {
            return null;
        }
        XmlTag xmlTag = xmlAttribute.getParent();
        if (xmlTag.getLocalName().equals("relation")) {
            XmlTag entitytTag = xmlTag.getParentTag();
            return (Entity) DomManager.getDomManager(xmlAttribute.getProject()).getDomElement(entitytTag);
        } else if (xmlTag.getLocalName().equals("view-link")) {
            ViewEntity viewEntity = (ViewEntity) DomManager.getDomManager
                    (xmlAttribute.getProject()).getDomElement(xmlTag.getParentTag());
            final String alias = xmlAttribute.getValue().trim();
            List<MemberEntity> list = viewEntity.getmMemberEntitys();
            final MemberEntity memberEntity = ContainerUtil.find(list, new Condition<MemberEntity>() {
                @Override
                public boolean value(MemberEntity memberEntity) {
                    return memberEntity.getEntityAlias().getValue().equals(alias);
                }
            });
            if (memberEntity == null) {
                return null;
            }
            //find current first
            EntityModel entityModel = DomManager.getDomManager(xmlAttribute.getProject()).
                    getFileElement(xmlFile, EntityModel.class).getRootElement();
            Entity entity = ContainerUtil.find(entityModel.getEntities(), new Condition<Entity>() {
                @Override
                public boolean value(Entity entity) {
                    return entity.getName().getValue().equals(memberEntity.getEntityName().getValue());
                }
            });
            if (entity != null) {
                return entity;
            }
            //not found
            List<EntityModel> models = OfbizUtils.getDomFileElements(EntityModel.class,
                    xmlAttribute.getProject(), GlobalSearchScope.projectScope(myElement.getProject()));
            for (EntityModel model : models) {
                    entity = ContainerUtil.find(model.getEntities(), new Condition<Entity>() {
                        @Override
                        public boolean value(Entity entity) {
                            return entity.getName().getValue().equals(memberEntity.getEntityName().getValue());
                        }
                    });
                    if (entity != null) {
                        return entity;
                    }

            }
        }
        return null;
    }

}