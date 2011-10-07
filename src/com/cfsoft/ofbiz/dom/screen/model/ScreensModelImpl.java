package com.cfsoft.ofbiz.dom.screen.model;

import com.cfsoft.ofbiz.dom.screen.api.Screen;
import com.cfsoft.ofbiz.dom.screen.api.Screens;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.model.impl.DomModelImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;


public class ScreensModelImpl extends DomModelImpl<Screens> implements ScreensModel {
    public ScreensModelImpl(DomFileElement<Screens> mergedModel, @NotNull Set<XmlFile> configFiles) {
        super(mergedModel, configFiles);
    }

    @NotNull
    @Override
    public List<Screen> getAllScreens() {
        return ContainerUtil.concat(getMergedScreenss(), new Function<Screens, Collection<? extends Screen>>() {
            @Override
            public Collection<? extends Screen> fun(Screens services) {
                return services.getScreens();
            }
        });
    }

        @NotNull
    protected List<Screens> getMergedScreenss() {
        return ContainerUtil.map(getRoots(), new Function<DomFileElement<Screens>, Screens>() {
            @Override
            public Screens fun(DomFileElement<Screens> serviceDomFileElement) {
                return  serviceDomFileElement.getRootElement();
            }
        });
    }
}
