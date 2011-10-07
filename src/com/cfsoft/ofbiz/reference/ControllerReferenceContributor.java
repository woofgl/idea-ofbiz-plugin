package com.cfsoft.ofbiz.reference;

import com.cfsoft.ofbiz.OfbizUtils;
import com.cfsoft.ofbiz.dom.controller.api.*;
import com.cfsoft.ofbiz.dom.service.api.Service;
import com.cfsoft.ofbiz.reference.controller.EventInvokeReference;
import com.cfsoft.ofbiz.reference.controller.ResponseValueReference;
import com.cfsoft.ofbiz.reference.controller.ViewMapReference;
import com.cfsoft.ofbiz.reference.service.ServiceInvokeReference;
import com.cfsoft.ofbiz.reference.service.ServiceLocationReference;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.DomPatterns.domElement;
import static com.intellij.patterns.DomPatterns.withDom;
import static com.intellij.patterns.XmlPatterns.xmlAttribute;
import static com.intellij.patterns.XmlPatterns.xmlTag;


public class ControllerReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        registerControllerXmlTags(registrar);
    }

    PsiReferenceProvider responseReferenceProvider = new PsiReferenceProvider() {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
            return new PsiReference[]{new ResponseValueReference((XmlAttribute) psiElement)};
        }
    };
    PsiReferenceProvider eventReferenceProvider = new PsiReferenceProvider() {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
            return new PsiReference[]{new EventInvokeReference((XmlAttribute) psiElement)};
        }
    };
    PsiReferenceProvider includeLocationReferenceProvider = new PsiReferenceProvider() {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
            return new PsiReference[]{new ComponentUrlReference((XmlAttribute) psiElement)};
        }
    };
    PsiReferenceProvider viewMapPageReferenceProvider = new PsiReferenceProvider() {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
            return new PsiReference[]{new ViewMapReference((XmlAttribute) psiElement)};
        }
    };
    private void registerControllerXmlTags(final PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(xmlAttribute().withLocalName("value").
                withSuperParent(1, withDom(domElement(Response.class))),
                responseReferenceProvider);
        registrar.registerReferenceProvider(xmlAttribute().withLocalName("invoke").
                withSuperParent(1, withDom(domElement(Event.class))),
                eventReferenceProvider);
        registrar.registerReferenceProvider(xmlAttribute().withLocalName("page").
                withSuperParent(1, withDom(domElement(ViewMap.class))),
                viewMapPageReferenceProvider);

        registrar.registerReferenceProvider(xmlAttribute().withLocalName("location").
                withSuperParent(1, withDom(domElement(Include.class))),new PsiReferenceProvider() {
            @NotNull
            @Override
            public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
                return new PsiReference[]{new ComponentUrlReference((XmlAttribute) psiElement, Controller.class)};
            }
        });
    }
}
