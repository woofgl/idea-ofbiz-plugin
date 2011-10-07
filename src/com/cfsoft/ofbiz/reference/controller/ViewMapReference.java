package com.cfsoft.ofbiz.reference.controller;

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
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.DomUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ViewMapReference extends PsiReferenceBase<XmlAttribute> {

    public ViewMapReference(@NotNull final XmlAttribute xmlTag) {
        super(xmlTag, false);
    }

    @SuppressWarnings({"unchecked"})
    public PsiElement resolve() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return myElement;
        }
        ViewMap viewmap = (ViewMap) DomUtil.getDomElement(myElement.getParent());
        String page = viewmap.getPage().getValue();
        if (page != null && page.trim().length() > 0) {
            final ComponentUrl componentUrl = new ComponentUrl(page.trim());
            PsiFile psiFile = OfbizUtils.findPsiFileByComponentUrl(myElement, componentUrl);
            if (psiFile != null) {
                if (componentUrl.getTag() != null
                        && psiFile instanceof XmlFile) {
                    XmlFile xmlFile = (XmlFile) psiFile;
                    XmlTag rootag = xmlFile.getRootTag();
                    if (rootag.getLocalName().equals("screens") || rootag.getLocalName().equals("forms")) {
                        XmlTag[] xmltags = xmlFile.getRootTag().getSubTags();
                        List<XmlTag> list = ContainerUtil.filter(xmltags, new Condition<XmlTag>() {
                            @Override
                            public boolean value(XmlTag xmlTag) {
                                return xmlTag.getLocalName().equals("screen") || xmlTag.getLocalName().equals("form");
                            }
                        });
                        return ContainerUtil.find(list, new Condition<XmlTag>() {
                            @Override
                            public boolean value(XmlTag xmlTag) {
                                return componentUrl.getTag().trim().equals(xmlTag.getAttributeValue("name"));
                            }
                        });
                    }

                } else {
                    return psiFile;
                }

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

        ViewMap viewmap = (ViewMap) DomUtil.getDomElement(myElement.getParent());
        String page = viewmap.getPage().getValue();
        if (page != null) {
            final ComponentUrl url = new ComponentUrl(page.trim());
            //if (url.getTag() == null||url.getTag().equals("IntellijIdeaRulezzz")) {
            ComponentManager manager = ComponentManager.getInstance(myElement.getProject());
            final Component[] components = manager.getAllComponents();
            if (url.getComponentName() != null) {
                final Component component = manager.getComponent(url.getComponentName(), components);
                if (component != null) {
                    if (!url.isStartTag()) {
                        List<String> list = ContainerUtil.map(component.getAllScreens(), new Function<Screen, String>() {
                            @Override
                            public String fun(Screen screen) {
                                return url.buildComponentUrl(component,
                                        screen.getXmlElement().getContainingFile().getVirtualFile().getPath(), "");
                            }
                        });
                        return list.toArray();
                    } else {
                        List<Screens> screenss = OfbizUtils.getDomFileElements(Screens.class, myElement.getProject(), component.getScope());
                        final Screens screens = ContainerUtil.find(screenss, new Condition<Screens>() {
                            @Override
                            public boolean value(Screens screens) {
                                return url.getRelativePath(component, screens.getXmlElement().
                                        getContainingFile().getVirtualFile().getPath()).equals(url.getRelativePath());
                            }
                        });
                        if (screens != null) {
                            return ContainerUtil.map(screens.getScreens(), new Function<Screen, Object>() {
                                @Override
                                public Object fun(Screen screen) {
                                    String name = screen.getName().getValue();
                                    String path = url.buildComponentUrl(component, screen.getXmlElement().getContainingFile().getVirtualFile().getPath(), name);
                                    return LookupElementBuilder.create(path).setPresentableText(name);
                                }
                            }).toArray();
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