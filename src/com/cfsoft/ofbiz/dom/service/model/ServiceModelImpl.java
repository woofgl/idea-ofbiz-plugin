package com.cfsoft.ofbiz.dom.service.model;

import com.cfsoft.ofbiz.dom.service.api.Service;
import com.cfsoft.ofbiz.dom.service.api.Services;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.model.impl.DomModelImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;


public class ServiceModelImpl extends DomModelImpl<Services> implements ServiceModel {
    public ServiceModelImpl(DomFileElement<Services> mergedModel, @NotNull Set<XmlFile> configFiles) {
        super(mergedModel, configFiles);
    }

    @NotNull
    @Override
    public List<Service> getAllServices() {
        return ContainerUtil.concat(getMergedServices(), new Function<Services, Collection<? extends Service>>() {
            @Override
            public Collection<? extends Service> fun(Services services) {
                return services.getServices();
            }
        });
    }

        @NotNull
    protected List<Services> getMergedServices() {
        return ContainerUtil.map(getRoots(), new Function<DomFileElement<Services>, Services>() {
            @Override
            public Services fun(DomFileElement<Services> serviceDomFileElement) {
                return  serviceDomFileElement.getRootElement();
            }
        });
    }
}
