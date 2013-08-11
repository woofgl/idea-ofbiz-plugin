package com.cfsoft.ofbiz.reference.controller;

import com.cfsoft.ofbiz.dom.controller.api.*;
import com.cfsoft.ofbiz.dom.controller.model.ControllerManager;
import com.cfsoft.ofbiz.facet.OfbizFacet;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;


public class ResponseValueReference extends PsiReferenceBase<XmlAttribute> {

    public ResponseValueReference(@NotNull final XmlAttribute xmlTag) {
        super(xmlTag, false);
    }

    @SuppressWarnings({"unchecked"})
    public PsiElement resolve() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return myElement;
        }
        Response response = (Response) DomUtil.getDomElement(myElement.getParent());
        Controller controller = ControllerManager.getInstance(myElement.getProject()).getController((XmlFile) myElement.getContainingFile());
        //String localName = myElement.getLocalName();
        //if(localName.equals("value")){
            if (response.getType().getStringValue().equals("request")) {
                for (RequestMap requestMap : controller.getAllRequestMaps()) {
                    if (requestMap.getUri().getStringValue().equals(response.getViewName().getStringValue())) {
                        return requestMap.getXmlElement();
                    }
                }
            } else {
                for (ViewMap viewMap : controller.getAllViewMaps()) {
                    if (viewMap.getName().getStringValue().equals(response.getViewName().getStringValue())) {
                        return viewMap.getXmlElement();
                    }
                }
                return myElement;
            }
        //}
        return myElement;

    }

    @NotNull
    @SuppressWarnings({"unchecked"})
    public Object[] getVariants() {
        Response response = (Response) DomUtil.getDomElement(myElement.getParent());
        Controller controller = ControllerManager.getInstance(myElement.getProject()).getController((XmlFile) myElement.getContainingFile());
        Set<String> set = new HashSet<String>();
        if (response.getType().getStringValue().equals("request")) {
            for (RequestMap requestMap : controller.getAllRequestMaps()) {
                set.add(requestMap.getUri().getValue());
            }

        } else {
            for (ViewMap viewMap : controller.getAllViewMaps()) {
                set.add(viewMap.getName().getStringValue());
            }
        }
        return set.toArray();
    }
}