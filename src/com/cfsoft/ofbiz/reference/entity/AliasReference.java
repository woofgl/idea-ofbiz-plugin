package com.cfsoft.ofbiz.reference.entity;

import com.cfsoft.ofbiz.OfbizUtils;
import com.cfsoft.ofbiz.dom.component.ComponentManager;
import com.cfsoft.ofbiz.dom.component.api.Component;
import com.cfsoft.ofbiz.dom.entity.api.*;
import com.cfsoft.ofbiz.facet.OfbizFacet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
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

import javax.swing.text.View;
import java.lang.reflect.Member;
import java.util.Collection;
import java.util.List;


public class AliasReference extends PsiReferenceBase<XmlAttribute> {

    public AliasReference(@NotNull final XmlAttribute xmlTag) {
        super(xmlTag, false);
    }

    @SuppressWarnings({"unchecked"})
    public PsiElement resolve() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return myElement;
        }
        ViewEntity viewEntity = (ViewEntity) DomManager.getDomManager(myElement.getProject())
                .getDomElement(myElement.getParent().getParentTag());
        if (myElement.getLocalName().equals("entity-alias")) {

            MemberEntity memberEntity = ContainerUtil.find(viewEntity.getmMemberEntitys(), new Condition<MemberEntity>() {
                @Override
                public boolean value(MemberEntity memberEntity) {
                    return memberEntity.getEntityAlias().getValue().equals(myElement.getValue().trim());
                }
            });
            if (memberEntity != null) {
                return memberEntity.getXmlElement();
            }
        } else if (myElement.getLocalName().equals("field")) {
            final String aliasName = myElement.getParent().getAttributeValue("entity-alias").trim();
            MemberEntity memberEntity = ContainerUtil.find(viewEntity.getmMemberEntitys(), new Condition<MemberEntity>() {
                @Override
                public boolean value(MemberEntity memberEntity) {
                    return memberEntity.getEntityAlias().getValue().equals(aliasName);
                }
            });
            if (memberEntity != null&&memberEntity.getEntity()!=null) {
                Field field = ContainerUtil.find(memberEntity.getEntity().getAllFields(), new Condition<Field>() {
                    @Override
                    public boolean value(Field field) {
                        return field.getName().getValue().equals(myElement.getValue().trim());
                    }
                });
                if (field != null) {
                    return field.getXmlElement();
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
        ViewEntity viewEntity = (ViewEntity) DomManager.getDomManager(myElement.getProject())
                .getDomElement(myElement.getParent().getParentTag());
        if (myElement.getLocalName().equals("entity-alias")) {
            return ContainerUtil.map2Array(viewEntity.getmMemberEntitys(), new Function<MemberEntity, Object>() {
                @Override
                public Object fun(MemberEntity memberEntity) {
                    return LookupElementBuilder.create(memberEntity.getEntityAlias().getValue())
                            .setTypeText(memberEntity.getEntityName().getValue());
                }
            });
        } else if (myElement.getLocalName().equals("field")||myElement.getLocalName().equals("name")) {
            final String aliasName = myElement.getParent().getAttributeValue("entity-alias").trim();
            final MemberEntity memberEntity = ContainerUtil.find(viewEntity.getmMemberEntitys(), new Condition<MemberEntity>() {
                @Override
                public boolean value(MemberEntity memberEntity) {
                    return memberEntity.getEntityAlias().getValue().equals(aliasName);
                }
            });
            if (memberEntity != null) {
                return ContainerUtil.map2Array(memberEntity.getEntity().getAllFields(), new Function<Field, Object>() {
                    @Override
                    public Object fun(Field field) {
                        return LookupElementBuilder.create(field.getName().getValue())
                                .setTailText(" " + field.getType().getValue())
                                .setTypeText(memberEntity.getEntity().getName().getValue());
                    }
                });
            }
        }
        return new Object[0];

    }
}