package com.cfsoft.ofbiz.dom.component.impl;

import com.cfsoft.ofbiz.OfbizUtils;
import com.cfsoft.ofbiz.dom.component.api.Component;
import com.cfsoft.ofbiz.dom.controller.api.Controller;
import com.cfsoft.ofbiz.dom.entity.api.Entity;
import com.cfsoft.ofbiz.dom.entity.api.EntityModel;
import com.cfsoft.ofbiz.dom.screen.api.Screen;
import com.cfsoft.ofbiz.dom.screen.api.Screens;
import com.cfsoft.ofbiz.dom.service.api.Service;
import com.cfsoft.ofbiz.dom.service.api.Services;
import com.cfsoft.ofbiz.dom.simplemethod.api.SimpleMethod;
import com.cfsoft.ofbiz.dom.simplemethod.api.SimpleMethods;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.DomElement;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

import static com.cfsoft.ofbiz.OfbizUtils.getDomFileElements;

public abstract class ComponentImpl implements Component {
    @Override
    public String getDirectory() {
        return getXmlElement().getContainingFile().getContainingDirectory().getVirtualFile().getPath();
    }

    @Override
    public List<Controller> getAllControllers() {
        return getDomFileElements(Controller.class, getXmlElement().getProject(), scope);
    }


    @Override
    public List<Service> getAllServices() {
        List<Services> list = getDomFileElements(Services.class, getXmlElement().getProject(), scope);
        return ContainerUtil.concat(list, new Function<Services, Collection<? extends Service>>() {
            @Override
            public Collection<Service> fun(Services services) {
                return services.getServices();
            }
        });
    }

    @Override
    public List<Screen> getAllScreens() {
        List<Screens> list = getDomFileElements(Screens.class, getXmlElement().getProject(), scope);
        return ContainerUtil.concat(list, new Function<Screens, Collection<? extends Screen>>() {
            @Override
            public Collection<Screen> fun(Screens screens) {
                return screens.getScreens();
            }
        });
    }

    @Override
    public List<SimpleMethod> getAllSimpleMethods() {
        List<SimpleMethods> list = getDomFileElements(SimpleMethods.class, getXmlElement().getProject(), scope);
        return ContainerUtil.concat(list, new Function<SimpleMethods, Collection<? extends SimpleMethod>>() {
            @Override
            public Collection<? extends SimpleMethod> fun(SimpleMethods simpleMethods) {
                return simpleMethods.getSimpleMethods();
            }
        });
    }

    public <T extends DomElement> List<T> getDomElements(Class<T> clazz) {
        return OfbizUtils.getDomFileElements(clazz, getXmlElement().getProject(), scope);
    }


    @Override
    public GlobalSearchScope getScope() {
        return scope;
    }

    @Override
    public List<Entity> getAllEntities() {
        List<EntityModel> entityModels = getDomFileElements(EntityModel.class, getXmlElement().getProject(), scope);
        return ContainerUtil.concat(entityModels, new Function<EntityModel, Collection<? extends Entity>>() {
            @Override
            public Collection<? extends Entity> fun(EntityModel entityModel) {
                return entityModel.getEntities();
            }
        });
    }

    private GlobalSearchScope scope = new GlobalSearchScope(){
        @Override
        public boolean contains(VirtualFile virtualFile) {
            return StdFileTypes.XML == virtualFile.getFileType();
        }

        @Override
        public int compare(VirtualFile virtualFile, VirtualFile virtualFile1) {
            return virtualFile.getPath().equals(virtualFile1.getPath())?0:1;
        }

        @Override
        public boolean isSearchInModuleContent(@NotNull Module module) {
            return getDirectory().startsWith(module.getModuleFilePath());
        }

        @Override
        public boolean isSearchInLibraries() {
            return false;
        }
    };
}
