package com.cfsoft.ofbiz.reference;

import com.cfsoft.ofbiz.OfbizUtils;
import com.cfsoft.ofbiz.dom.component.ComponentManager;
import com.cfsoft.ofbiz.dom.component.ComponentUrl;
import com.cfsoft.ofbiz.dom.component.api.Component;
import com.cfsoft.ofbiz.dom.controller.api.ViewMap;
import com.cfsoft.ofbiz.dom.screen.api.Screen;
import com.cfsoft.ofbiz.dom.screen.api.Screens;
import com.cfsoft.ofbiz.facet.OfbizFacet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomUtil;
import com.intellij.util.xml.ui.DomEditorManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Class ComponentUrlReference ...
 *
 * @author Administrator
 *         Created on 10/7/11
 */
public class ComponentUrlReference<T extends DomElement> extends PsiReferenceBase<XmlAttribute> {
    private Class<T> clazz = null;

    public ComponentUrlReference(@NotNull final XmlAttribute xmlAttribute, Class<T> clazz) {
        super(xmlAttribute, false);
        this.clazz = clazz;
    }

    public ComponentUrlReference(@NotNull final XmlAttribute xmlAttribute) {
        super(xmlAttribute, false);
    }

    @SuppressWarnings({"unchecked"})
    public PsiElement resolve() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return myElement;
        }
        final ComponentUrl componentUrl = new ComponentUrl(myElement.getValue());
        PsiFile psiFile = OfbizUtils.findPsiFileByComponentUrl(myElement, componentUrl);
        if (psiFile != null) {
            return psiFile;
        }
        return null;
    }

    @NotNull
    @SuppressWarnings({"unchecked"})
    public Object[] getVariants() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return new Object[0];
        }

        String location = OfbizUtils.removeIdeaPostFix(myElement.getValue());
        if (location != null) {
            final ComponentUrl url = new ComponentUrl(location);
            final ComponentManager manager = ComponentManager.getInstance(myElement.getProject());
            final Component[] components = manager.getAllComponents();
            if (url.getComponentName() != null) {
                final Component component = manager.getComponent(url.getComponentName(), components);
                if (component != null) {

                    PsiDirectory psiDir = OfbizUtils.findPsiDirectoryByComponentUrl(myElement, url);
                    if (psiDir != null) {
                        if (clazz == null) {
                            Object[] lookups = ContainerUtil.map2Array(psiDir.getChildren(), new Function<PsiElement, Object>() {
                                @Override
                                public Object fun(PsiElement psiElement) {
                                    PsiFileSystemItem item = (PsiFileSystemItem) psiElement;
                                    String componentPath = manager.getComponentUrl(item.getVirtualFile().getPath(), component);
                                    if (item.isDirectory()) {
                                        componentPath += "/";
                                    }
                                    return LookupElementBuilder.create(componentPath).setTypeText(component.getName().getValue());
                                }
                            });
                            return lookups;
                        } else {
                            List<T> list = OfbizUtils.getDomFileElements(clazz, myElement.getProject(),
                                    GlobalSearchScope.directoryScope(myElement.getProject(), psiDir.getVirtualFile(), true));
                            Object[] lookups = ContainerUtil.map2Array(list, new Function<T, Object>() {
                                @Override
                                public Object fun(T psiElement) {

                                    String componentPath = manager.getComponentUrl(psiElement.getXmlElement().getContainingFile().getVirtualFile().getPath(), component);
                                    return LookupElementBuilder.create(componentPath).setTypeText(component.getName().getValue());
                                }
                            });
                            return lookups;
                        }
                    }

                }
            } else {
                return ContainerUtil.map(components, new Function<Component, Object>() {
                    @Override
                    public Object fun(Component component) {
                        String lookup = String.format("component://" + component.getName().getValue() + "/");
                        return LookupElementBuilder.create(lookup).setPresentableText(component.getName().getValue());
                    }
                }).toArray();
            }
        }


        return new Object[0];
    }

}