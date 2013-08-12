package com.cfsoft.ofbiz.reference.service;

import com.cfsoft.ofbiz.OfbizUtils;
import com.cfsoft.ofbiz.dom.service.api.Engine;
import com.cfsoft.ofbiz.dom.service.api.Service;
import com.cfsoft.ofbiz.dom.service.api.ServiceConfig;
import com.cfsoft.ofbiz.facet.OfbizFacet;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.DomJavaUtil;
import com.intellij.util.xml.DomUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;


public class ServiceReference extends PsiReferenceBase<XmlAttribute> {

    private final String localName;

    public ServiceReference(@NotNull final XmlAttribute xmlTag) {
        super(xmlTag, false);
        this.localName = xmlTag.getLocalName();
    }

    @SuppressWarnings({"unchecked"})
    public PsiElement resolve() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return myElement;
        }
        final Service service = (Service) DomUtil.getDomElement(myElement.getParent());
        PsiElement psiEle = myElement;
        if (localName.equals("location")) {
            psiEle = OfbizUtils.getServiceLocation(service);
            if (psiEle == null && service.getEngine().getValue().equals("java")) {
                psiEle = DomJavaUtil.findClass(service.getLocation().getStringValue().trim(), service);
            }
            return psiEle;
        } else if (localName.equals("invoke")) {
            String engine = service.getEngine().getStringValue();
            if("java".equals(engine)){
                psiEle = OfbizUtils.getServiceMethod(service);
            }
            if("simple".equals(engine)){
                psiEle = OfbizUtils.getServiceSimpleMethodElememnt(service);
            }

            return psiEle;
        } else if (localName.equals("engine")) {
            ServiceConfig serviceConfig = OfbizUtils.findXmlElement(ServiceConfig.class, myElement.getProject(),
                    GlobalSearchScope.projectScope(myElement.getProject()), new Condition<ServiceConfig>() {
                @Override
                public boolean value(ServiceConfig serviceConfig) {
                    return true;
                }
            });
            Engine engine = ContainerUtil.find(serviceConfig.getServiceEngine().getEngines(), new Condition<Engine>() {
                @Override
                public boolean value(Engine engine) {
                    return engine.getName().getStringValue().equals(service.getEngine().getStringValue());
                }
            });
            return engine == null ? null: engine.getXmlElement();
        }
        return myElement;
    }

    @NotNull
    @SuppressWarnings({"unchecked"})
    public Object[] getVariants() {
        if (OfbizFacet.getInstance(myElement) == null) {
            return new Object[0];
        }
        Service service = (Service) DomUtil.getDomElement(myElement.getParent());
        String engine = service.getEngine().getStringValue();
        Collection set = Collections.EMPTY_SET;
        if (localName.equals("location")) {
            set = OfbizUtils.getServiceLocationNames(service);
        } else if (localName.equals("invoke")) {
            if ("java".equals(engine)) {
                set = OfbizUtils.getServiceCompleteMethodNames(service);
            }
            if ("simple".equals(engine)) {
                set =  OfbizUtils.getServiceCompleteSimpleMethodNames(service);
            }

            if("entity-auto".equals(engine)){
                return new String[]{"create", "update", "delete"};
            }

            return set == null ? Collections.emptySet().toArray() : set.toArray();
        } else if (localName.equals("engine")) {
            ServiceConfig serviceConfig = OfbizUtils.findXmlElement(ServiceConfig.class, myElement.getProject(),
                    GlobalSearchScope.projectScope(myElement.getProject()), new Condition<ServiceConfig>() {
                @Override
                public boolean value(ServiceConfig serviceConfig) {
                    return true;
                }
            });
            List<String> names = ContainerUtil.map(serviceConfig.getServiceEngine().getEngines(), new Function<Engine, String>() {
                @Override
                public String fun(Engine engine) {
                    return engine.getName().getStringValue();
                }
            });
            return names.toArray();
        }

        return set.toArray();
    }
}