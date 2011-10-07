package com.cfsoft.ofbiz.dom.controller.impl;

import com.cfsoft.ofbiz.OfbizUtils;
import com.cfsoft.ofbiz.dom.component.ComponentManager;
import com.cfsoft.ofbiz.dom.component.ComponentUrl;
import com.cfsoft.ofbiz.dom.component.api.Component;
import com.cfsoft.ofbiz.dom.controller.api.Controller;
import com.cfsoft.ofbiz.dom.controller.api.Include;
import com.cfsoft.ofbiz.dom.controller.api.RequestMap;
import com.cfsoft.ofbiz.dom.controller.api.ViewMap;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class ControllerImpl implements Controller {
    private SoftReference<Set<Controller>> parentRef = null;

    @Override
    public List<RequestMap> getAllRequestMaps() {
        List<Include> includes = getIncludes();
        List<RequestMap> requestMaps = new ArrayList<RequestMap>();
        final Project project = getXmlElement().getProject();
        for (Include include : includes) {
            Controller controller = getFromInclude(project, include);
            if (controller != null) {
                requestMaps.addAll(controller.getAllRequestMaps());
            }
        }
        requestMaps.addAll(getRequestMaps());
        return requestMaps;
    }

    @Override
    public List<ViewMap> getAllViewMaps() {
        List<Include> includes = getIncludes();
        List<ViewMap> viewMaps = new ArrayList<ViewMap>();
        final Project project = getXmlElement().getProject();
        for (Include include : includes) {
            Controller controller = getFromInclude(project, include);
            if (controller != null) {
                viewMaps.addAll(controller.getAllViewMaps());
            }
        }
        viewMaps.addAll(getViewMaps());
        return viewMaps;
    }

    private Controller getFromInclude(Project project, Include include) {
        if (include.getLocation().getValue() != null) {
            ComponentUrl url = new ComponentUrl(include.getLocation().getValue());
            PsiFile psiFile = OfbizUtils.findPsiFileByComponentUrl(getXmlElement(), url);
            if (psiFile != null) {
                DomFileElement<Controller> cfile = DomManager.getDomManager(project).getFileElement((XmlFile) psiFile, Controller.class);
                if (cfile != null) {
                    return cfile.getRootElement();
                }
            }
        }
        return null;
    }

    public Set<Controller> getAllIncludeBy() {
        if (parentRef == null || parentRef.get() == null) {
            final Set<Controller> results = new HashSet<Controller>();
            Component[] components = ComponentManager.getInstance(getXmlElement().getProject()).getAllComponents();
            List<Controller> controllers = OfbizUtils.getDomFileElements(Controller.class,
                    getXmlElement().getProject(), GlobalSearchScope.projectScope(getXmlElement().getProject()));
            Set<Controller> controllerSet = new HashSet<Controller>();
            Set<Controller> checks = new HashSet<Controller>();
            checks.add(this);
            controllerSet.addAll(controllers);
            controllers.remove(this);

            Set<Controller> parents = getParents(controllerSet, results, checks, components);
            while (parents.size() > 0) {
                parents = getParents(controllerSet, results, parents, components);
            }

            parentRef = new SoftReference<Set<Controller>>(results);
        }
        return parentRef.get();
    }

    public Set<Controller> getParents(Set<Controller> controllers, Set<Controller> results, Set<Controller> checks, Component... components) {
        Set<Controller> parents = new HashSet<Controller>();

        for (Controller check : checks) {
            controllers.remove(check);
            for (Controller controller : controllers) {
                List<Include> includes = controller.getIncludes();
                if (includes != null) {
                    for (Include include : includes) {
                        ComponentUrl url = new ComponentUrl(include.getLocation().getValue());
                        if (url.getRealPath(getXmlElement().getProject(), components).
                                equals(check.getXmlElement().getContainingFile().getVirtualFile().getPath())) {
                            results.add(controller);
                            parents.add(controller);
                            break;
                        }
                    }
                }
            }
        }
        return parents;
    }
}
