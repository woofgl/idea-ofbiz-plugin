package com.cfsoft.ofbiz.dom.component.impl;

import com.cfsoft.ofbiz.OfbizUtils;
import com.cfsoft.ofbiz.dom.component.ComponentManager;
import com.cfsoft.ofbiz.dom.component.ComponentUrl;
import com.cfsoft.ofbiz.dom.component.api.Component;
import com.cfsoft.ofbiz.dom.component.api.ComponentLoaders;
import com.cfsoft.ofbiz.dom.fieldtype.api.FieldtypeModel;
import com.cfsoft.ofbiz.facet.OfbizFacet;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public class ComponentManagerImpl extends ComponentManager {
    private final Project project;

    public ComponentManagerImpl(Project project) {
        this.project = project;
    }

    @Override
    public Component[] getAllComponents() {
        List<Component> components =
                OfbizUtils.getDomFileElements(Component.class, project, GlobalSearchScope.projectScope(project));
//        ComponentLoaders loaderParentdirs = findComponentLoaders(ComponentLoaders.ROOT_PATH);
//
//        List<ComponentParentDirectory> list = loaderParentdirs.getComponentParentDirectorys();
//
//        for (ComponentParentDirectory directory : list) {
//            String dir = directory.getParentDirectory().getValue();
//            if (dir.equals(ComponentLoaders.HOT_DEPLOY)) {
//                //process hot deploy
//            } else {
//                ComponentLoaders loader = findComponentLoaders(String.format("%s/%s", dir, ComponentLoaders.NAME));
//                if (loader != null) {
//                    List<ComponentLocation> locations = loader.getComponentLocations();
//                    for (ComponentLocation location : locations) {
//                        String componentUrl = String.format("file://%s/%s/%s/%s", project.getBaseDir().getPath(),
//                                dir, location.getLocation().getValue(), Component.NAME);
//                        Component component = findComponent(componentUrl);
//                        if (component != null) {
//                            components.add(component);
//                        }
//                    }
//                }
//            }
//        }
        return components.toArray(new Component[components.size()]);
    }

    private Component findComponent(String url) {
        VirtualFile vfile = VirtualFileManager.getInstance().findFileByUrl(url);
        PsiFile psiFile = PsiManager.getInstance(project).findFile(vfile);
        DomFileElement<Component> fileEle = DomManager.getDomManager(project).getFileElement((XmlFile) psiFile, Component.class);
        return fileEle.getRootElement();
    }

    private ComponentLoaders findComponentLoaders(String relPath) {
        //get root
        String path = project.getBaseDir().getPath();
        VirtualFile vfile = VirtualFileManager.getInstance().findFileByUrl(String.format("file://%s/%s", path, relPath));
        if (vfile == null) {
            return null;
        }
        PsiFile psiFile = PsiManager.getInstance(project).findFile(vfile);
        DomFileElement<ComponentLoaders> fileEle = DomManager.getDomManager(project).getFileElement((XmlFile) psiFile, ComponentLoaders.class);
        return fileEle.getRootElement();
    }

    @Override
    public Component getComponent(String name, Component...components) {
        final Component[] list;
        if (components.length == 0) {
            list = getAllComponents();
        }else{
            list = components;
        }


        for (Component component : list) {
            if (component.getName().getValue().equals(name)) {
                return component;
            }
        }
        return null;
    }

    @Override
    public String getComponentUrl(@NotNull XmlElement xmlElement,Component...components) {
        String path = xmlElement.getContainingFile().getVirtualFile().getPath();
        return getComponentUrl(path,components);
    }

    @Override
    public Component getComponent(@NotNull XmlElement xmlElement,Component...components) {
        if (components.length == 0) {
            components = getAllComponents();
        }

        final String path = xmlElement.getContainingFile().getVirtualFile().getPath();
        return ContainerUtil.find(components, new Condition<Component>() {
            @Override
            public boolean value(Component component) {
                return path.startsWith(component.getDirectory());
            }
        });
    }

    @Override
    public FieldtypeModel getFieldtypeModel(XmlElement xmlElement) {
        OfbizFacet facet = OfbizFacet.getInstance(ModuleUtil.findModuleForPsiElement(xmlElement));
        if (facet == null) {
            return null;
        }
        String database = facet.getConfiguration().getDatabase();
        final String fieldDefName = String.format("fieldtype%s.xml",database);
        List<FieldtypeModel> eles = OfbizUtils.getDomFileElements(FieldtypeModel.class, project,
                GlobalSearchScope.projectScope(xmlElement.getProject()));
        return ContainerUtil.find(eles, new Condition<FieldtypeModel>() {
            @Override
            public boolean value(FieldtypeModel fieldtypeModel) {
                return fieldtypeModel.getXmlElement().getContainingFile().getVirtualFile().
                        getPath().endsWith("framework/entity/fieldtype/" + fieldDefName);
            }
        });
    }

    @Override
    public String getComponentUrl(@NotNull String path, Component...components) {
        final Component[] list;
        if (components.length == 0) {
            list = components;
        }else{
            list = getAllComponents();
        }
        for (Component component : list) {
            if (path.startsWith(component.getDirectory())) {
                return String.format("component://%s%s", component.getName().getValue(), path.substring(component.getDirectory().length()));
            }
        }
        return null;
    }

    @Override
    public String getRealPath(@NotNull String componentUrl,Component...components) {
        ComponentUrl url = new ComponentUrl(componentUrl);
        return url.getRealPath(project, components);
    }

    @Override
    public boolean isComponentUrl(String url,Component...components) {
        return url.startsWith("component://");
    }


}
