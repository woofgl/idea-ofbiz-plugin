package com.cfsoft.ofbiz.reference.entity;

import com.cfsoft.ofbiz.OfbizUtils;
import com.cfsoft.ofbiz.dom.component.ComponentManager;
import com.cfsoft.ofbiz.dom.entity.api.Field;
import com.cfsoft.ofbiz.dom.fieldtype.api.FieldType;
import com.cfsoft.ofbiz.dom.fieldtype.api.FieldtypeModel;
import com.cfsoft.ofbiz.dom.service.api.Service;
import com.cfsoft.ofbiz.facet.OfbizFacet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.DomUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;


public class FieldTypeReference extends PsiReferenceBase<XmlAttribute> {

    public FieldTypeReference(@NotNull final XmlAttribute xmlTag) {
        super(xmlTag, false);
    }

    @SuppressWarnings({"unchecked"})
    public PsiElement resolve() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return myElement;
        }
        final Field field = (Field) DomUtil.getDomElement(myElement.getParent());
        FieldtypeModel fieldtypeModel = ComponentManager.getInstance(myElement.getProject()).getFieldtypeModel(myElement);
        FieldType fieldtype = ContainerUtil.find(fieldtypeModel.getFieldTypes(), new Condition<FieldType>() {
            @Override
            public boolean value(FieldType fieldType) {
                return fieldType.getType().getValue().equals(field.getType().getValue());
            }
        });
        return fieldtype == null ? null : fieldtype.getXmlElement();
    }


    @NotNull
    @SuppressWarnings({"unchecked"})
    public Object[] getVariants() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return new Object[0];
        }
        final Field field = (Field) DomUtil.getDomElement(myElement.getParent());
        FieldtypeModel fieldtypeModel = ComponentManager.getInstance(myElement.getProject()).getFieldtypeModel(myElement);
        return ContainerUtil.map(fieldtypeModel.getFieldTypes(), new Function<FieldType, LookupElementBuilder>() {
            @Override
            public LookupElementBuilder fun(FieldType fieldType) {
                return LookupElementBuilder.create(fieldType.getType().getValue()).setTypeText(fieldType.getSqlType().getValue());
            }
        }).toArray();

    }
}