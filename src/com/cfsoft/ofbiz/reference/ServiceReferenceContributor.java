package com.cfsoft.ofbiz.reference;

import com.cfsoft.ofbiz.dom.service.api.Service;
import com.cfsoft.ofbiz.reference.service.ServiceInvokeReference;
import com.cfsoft.ofbiz.reference.service.ServiceLocationReference;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.DomPatterns.domElement;
import static com.intellij.patterns.DomPatterns.withDom;
import static com.intellij.patterns.XmlPatterns.xmlAttribute;


public class ServiceReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        registerControllerXmlTags(registrar);
    }

    PsiReferenceProvider serviceInvokeReferenceProvider = new PsiReferenceProvider() {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
            return new PsiReference[]{new ServiceInvokeReference((XmlAttribute) psiElement)};
        }
    };
    PsiReferenceProvider serviceLocationReferenceProvider = new PsiReferenceProvider() {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
            return new PsiReference[]{new ServiceLocationReference((XmlAttribute) psiElement)};
        }
    };

    private void registerControllerXmlTags(final PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(xmlAttribute().withLocalName("invoke").
                withSuperParent(1, withDom(domElement(Service.class))),
                serviceInvokeReferenceProvider);
        registrar.registerReferenceProvider(xmlAttribute().withLocalName("location").
                withSuperParent(1, withDom(domElement(Service.class))),
                serviceLocationReferenceProvider);
    }
}
