package com.cfsoft.ofbiz.reference.controller;

import com.cfsoft.ofbiz.OfbizUtils;
import com.cfsoft.ofbiz.dom.controller.api.*;
import com.cfsoft.ofbiz.dom.service.api.Service;
import com.cfsoft.ofbiz.dom.service.api.Services;
import com.cfsoft.ofbiz.dom.service.model.ServiceManager;
import com.cfsoft.ofbiz.dom.service.model.ServiceModel;
import com.cfsoft.ofbiz.facet.OfbizFacet;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class EventInvokeReference extends PsiReferenceBase<XmlAttribute> {

    public EventInvokeReference(@NotNull final XmlAttribute xmlTag) {
        super(xmlTag, false);
    }

    @SuppressWarnings({"unchecked"})
    public PsiElement resolve() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return myElement;
        }
        final Event event = (Event) DomUtil.getDomElement(myElement.getParent());
        PsiElement psiEle = OfbizUtils.getEventMethod(event);
        if (psiEle == null) {
            psiEle = OfbizUtils.getEventServiceElememnt(event);
        }
        return psiEle == null ? myElement : psiEle;
    }


    @NotNull
    @SuppressWarnings({"unchecked"})
    public Object[] getVariants() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return new Object[0];
        }
        Event event = (Event) DomUtil.getDomElement(myElement.getParent());
        Set<String> set = OfbizUtils.getEventCompleteMethodNames(event);
        return set.size() == 0 ? OfbizUtils.getEventCompleteServiceNames(event).toArray() : set.toArray();

    }
}