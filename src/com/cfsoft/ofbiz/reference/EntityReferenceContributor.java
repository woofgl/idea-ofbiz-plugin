package com.cfsoft.ofbiz.reference;

import com.cfsoft.ofbiz.dom.entity.api.*;
import com.cfsoft.ofbiz.reference.entity.*;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.DomPatterns.domElement;
import static com.intellij.patterns.DomPatterns.withDom;
import static com.intellij.patterns.XmlPatterns.xmlAttribute;
import static com.intellij.patterns.XmlPatterns.xmlTag;


public class EntityReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        registerControllerXmlTags(registrar);
    }

    PsiReferenceProvider fieldTypeReferenceProvider = new PsiReferenceProvider() {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
            return new PsiReference[]{new FieldTypeReference((XmlAttribute) psiElement)};
        }
    };

    PsiReferenceProvider fieldReferenceProvider = new PsiReferenceProvider() {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
            return new PsiReference[]{new FieldReference((XmlAttribute) psiElement)};
        }
    };

    PsiReferenceProvider entityReferenceProvider = new PsiReferenceProvider() {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
            return new PsiReference[]{new EntityReference((XmlAttribute) psiElement)};
        }
    };
    PsiReferenceProvider relationRelFieldReferenceProvider = new PsiReferenceProvider() {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
            return new PsiReference[]{new RelationRelFieldReference((XmlAttribute) psiElement)};
        }
    };
    PsiReferenceProvider viewLinkKeyMapFieldReferenceProvider = new PsiReferenceProvider() {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
            return new PsiReference[]{new ViewLinkKeyMapRelFieldReference((XmlAttribute) psiElement)};
        }
    };
    PsiReferenceProvider aliasReferenceProvider = new PsiReferenceProvider() {
        @NotNull
        @Override
        public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext) {
            return new PsiReference[]{new AliasReference((XmlAttribute) psiElement)};
        }
    };


    private void registerControllerXmlTags(final PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(xmlAttribute().withLocalName("type").
                withSuperParent(1, withDom(domElement(Field.class))),
                fieldTypeReferenceProvider);
        registrar.registerReferenceProvider(xmlAttribute().withLocalName("field").
                withSuperParent(1, withDom(domElement(PrimKey.class))),
                fieldReferenceProvider);
        registrar.registerReferenceProvider(xmlAttribute().withLocalName("name").
                withSuperParent(1, xmlTag().withLocalName("index-field")),
                fieldReferenceProvider);
//              <relation type="one" fk-name="PROD_CC_CATALOG" rel-entity-name="ProdCatalog">
//        <key-map field-name="prodCatalogId" />
//      </relation>

        registrar.registerReferenceProvider(xmlAttribute().withLocalName("field-name").
                withSuperParent(2,  xmlTag().withLocalName("relation")),
                fieldReferenceProvider);

        registrar.registerReferenceProvider(xmlAttribute().withLocalName("rel-entity-name").
                withSuperParent(1, withDom(domElement(Relation.class))),
                entityReferenceProvider);
        registrar.registerReferenceProvider(xmlAttribute().withLocalName("entity-name").
                withSuperParent(1, xmlTag().withLocalName("member-entity")),
                entityReferenceProvider);
        registrar.registerReferenceProvider(xmlAttribute().withLocalName("rel-field-name").
                withSuperParent(2, xmlTag().withLocalName("view-link")),
                viewLinkKeyMapFieldReferenceProvider);
        registrar.registerReferenceProvider(xmlAttribute().withLocalName("rel-field-name").
                withSuperParent(2, xmlTag().withLocalName("relation")),
                relationRelFieldReferenceProvider);

        registrar.registerReferenceProvider(xmlAttribute().withLocalName("field-name").
                withSuperParent(2, xmlTag().withLocalName("view-link")),
                viewLinkKeyMapFieldReferenceProvider);

        registrar.registerReferenceProvider(xmlAttribute().
                withSuperParent(1, xmlTag().withLocalName("alias")),
                aliasReferenceProvider);
        registrar.registerReferenceProvider(xmlAttribute().
                withSuperParent(1, xmlTag().withLocalName("alias-all")),
                aliasReferenceProvider);
        registrar.registerReferenceProvider(xmlAttribute("entity-name").
                withSuperParent(1, xmlTag().withLocalName("extend-entity")),
                entityReferenceProvider);


    }
}
