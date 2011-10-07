package com.cfsoft.ofbiz.reference.service;

import com.cfsoft.ofbiz.OfbizUtils;
import com.cfsoft.ofbiz.dom.service.api.Service;
import com.cfsoft.ofbiz.facet.OfbizFacet;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.xml.DomJavaUtil;
import com.intellij.util.xml.DomUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;


public class ServiceLocationReference extends PsiReferenceBase<XmlAttribute> {

    public ServiceLocationReference(@NotNull final XmlAttribute xmlTag) {
        super(xmlTag, false);
    }

    @SuppressWarnings({"unchecked"})
    public PsiElement resolve() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return myElement;
        }
        final Service service = (Service) DomUtil.getDomElement(myElement.getParent());
        PsiElement psiEle = OfbizUtils.getServiceLocation(service);
        if (psiEle == null && service.getEngine().getValue().equals("java")) {
            psiEle = DomJavaUtil.findClass(service.getLocation().getStringValue().trim(), service);
        }
        return psiEle == null ? null : psiEle;
    }


    @NotNull
    @SuppressWarnings({"unchecked"})
    public Object[] getVariants() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return new Object[0];
        }
        Service service = (Service) DomUtil.getDomElement(myElement.getParent());
        Collection<Object> set = OfbizUtils.getServiceLocationNames(service);
        return set.toArray();
    }
}