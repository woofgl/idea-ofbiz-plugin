package com.cfsoft.ofbiz.dom.controller.model;

import com.cfsoft.ofbiz.dom.controller.api.Controller;
import com.cfsoft.ofbiz.dom.controller.api.RequestMap;
import com.cfsoft.ofbiz.dom.controller.api.Response;
import com.cfsoft.ofbiz.dom.controller.api.ViewMap;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiClass;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.Function;
import com.intellij.util.NotNullFunction;
import com.intellij.util.Processor;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.model.impl.DomModelImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;


public class ControllerModelImpl extends DomModelImpl<Controller> implements ControllerModel {

    private static final NotNullFunction<DomFileElement<Controller>, Controller> ROOT_ELEMENT_MAPPER =
            new NotNullFunction<DomFileElement<Controller>, Controller>() {
                @NotNull
                public Controller fun(final DomFileElement<Controller> strutsRootDomFileElement) {
                    return strutsRootDomFileElement.getRootElement();
                }
            };

    private static final Function<Controller, Collection<? extends RequestMap>> REQUSET_MAP_COLLECTOR =
            new Function<Controller, Collection<? extends RequestMap>>() {
                public Collection<? extends RequestMap> fun(final Controller strutsRoot) {
                    return strutsRoot.getAllRequestMaps();
                }
            };

    private static final Function<Controller, Collection<? extends ViewMap>> VIEW_MAP_COLLECTOR =
            new Function<Controller, Collection<? extends ViewMap>>() {
                public Collection<? extends ViewMap> fun(final Controller strutsRoot) {
                    return strutsRoot.getAllViewMaps();
                }
            };
    private static final Function<RequestMap, Collection<? extends Response>> RESPONSE_MAP_COLLECTOR =
            new Function<RequestMap, Collection<? extends Response>>() {
                public Collection<? extends Response> fun(final RequestMap strutsRoot) {
                    return strutsRoot.getResponse();
                }
            };

    public ControllerModelImpl(DomFileElement<Controller> mergedModel, @NotNull Set<XmlFile> configFiles) {
        super(mergedModel, configFiles);
    }

    @NotNull
    public List<Controller> getMergedControllers() {
        return ContainerUtil.map(getRoots(), ROOT_ELEMENT_MAPPER);
    }

    @NotNull
    public List<RequestMap> getAllRequestMaps() {
        return ContainerUtil.concat(getMergedControllers(), REQUSET_MAP_COLLECTOR);
    }

    @NotNull
    public List<ViewMap> getAllViewMaps() {
        return ContainerUtil.concat(getMergedControllers(), VIEW_MAP_COLLECTOR);
    }

    public List<Response> getAllResponses() {
        return ContainerUtil.concat(getAllRequestMaps(), RESPONSE_MAP_COLLECTOR);
    }

    @NotNull
    public List<ViewMap> findViewMapByName(@NotNull @NonNls final String viewName) {
        return ContainerUtil.findAll(getAllViewMaps(), new Condition<ViewMap>() {
            public boolean value(ViewMap viewMap) {
                return viewMap.getName().getStringValue().equals(viewName);
            }
        });
    }

    public boolean isServiceClass(PsiClass psiClass) {
        return false;
    }

    public boolean processViewMap(Processor<ViewMap> processor) {
        for (final ViewMap view : getAllViewMaps()) {
            if (!processor.process(view)) {
                return false;
            }

        }
        return true;
    }


}
