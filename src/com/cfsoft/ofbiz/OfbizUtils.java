package com.cfsoft.ofbiz;

import com.cfsoft.ofbiz.dom.component.ComponentManager;
import com.cfsoft.ofbiz.dom.component.ComponentUrl;
import com.cfsoft.ofbiz.dom.component.api.Component;
import com.cfsoft.ofbiz.dom.controller.api.Controller;
import com.cfsoft.ofbiz.dom.controller.api.Event;
import com.cfsoft.ofbiz.dom.controller.api.Handler;
import com.cfsoft.ofbiz.dom.controller.model.ControllerManager;
import com.cfsoft.ofbiz.dom.entity.api.Entity;
import com.cfsoft.ofbiz.dom.entity.api.EntityModel;
import com.cfsoft.ofbiz.dom.service.api.Service;
import com.cfsoft.ofbiz.dom.service.api.Services;
import com.cfsoft.ofbiz.dom.service.model.ServiceManager;
import com.cfsoft.ofbiz.dom.service.model.ServiceModel;
import com.cfsoft.ofbiz.dom.simplemethod.api.SimpleMethod;
import com.cfsoft.ofbiz.dom.simplemethod.api.SimpleMethods;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlElement;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomJavaUtil;
import com.intellij.util.xml.DomService;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 9/25/11
 * Time: 2:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class OfbizUtils {
    public static final String IDEA_POSTFIX = "IntellijIdeaRulezzz";
    private OfbizUtils() {
    }

    public static PsiClass findEventClass(Event event) {
        String className = event.getPath().getStringValue();
        Module module = ModuleUtil.findModuleForPsiElement(event.getXmlElement());
        // resolve JAVA-class directly
        return DomJavaUtil.findClass(className,
                event.getXmlElement().getContainingFile(), module, GlobalSearchScope.allScope(event.getXmlElement().getProject()));
    }

    public static PsiClass findServiceClass(Service service) {
        String className = service.getLocation().getStringValue();
        Module module = ModuleUtil.findModuleForPsiElement(service.getXmlElement());
        // resolve JAVA-class directly
        return DomJavaUtil.findClass(className,
                service.getXmlElement().getContainingFile(), module, GlobalSearchScope.allScope(service.getXmlElement().getProject()));
    }

    private static boolean hasValidEventParameters(PsiMethod psiMethod) {
        // psiMethod.getParameterList().getParameters()[1].getTypeElement().getType().getCanonicalText()
        PsiParameter[] parameters = psiMethod.getParameterList().getParameters();
        int count = psiMethod.getParameterList().getParametersCount();
        if (count == 2) {
            boolean first = parameters[0].getTypeElement().getType().getCanonicalText().equals("javax.servlet.http.HttpServletRequest");
            boolean second = parameters[1].getTypeElement().getType().getCanonicalText().equals("javax.servlet.http.HttpServletResponse");
            return first && second;
        }

        return false;
    }

    public static boolean isEventMethod(@NotNull PsiMethod psiMethod) {
        PsiType returnType = psiMethod.getReturnType();
        if (returnType != null && returnType.getCanonicalText().equals("java.lang.String")
                && hasValidEventParameters(psiMethod) && hasValidModifier(psiMethod)) return true;
        else return false;
    }

    public static boolean isServiceMethod(PsiMethod psiMethod) {
        return psiMethod.getReturnType() != null && psiMethod.getReturnType().getCanonicalText().startsWith("java.util.Map")
                && hasValidServiceParameters(psiMethod) && hasValidModifier(psiMethod);
    }

    private static boolean hasValidServiceParameters(PsiMethod psiMethod) {
        PsiParameter[] parameters = psiMethod.getParameterList().getParameters();
        int count = psiMethod.getParameterList().getParametersCount();
        if (count == 2) {
            boolean first = parameters[0].getTypeElement().getType().getCanonicalText().startsWith("org.ofbiz.service.DispatchContext");
            boolean second = parameters[1].getTypeElement().getType().getCanonicalText().startsWith("java.util.Map");
            return first && second;
        }

        return false;
    }

    public static boolean hasValidModifier(PsiMethod psiMethod) {
        PsiModifierList mlist = psiMethod.getModifierList();
        return mlist.hasExplicitModifier("static") && mlist.hasExplicitModifier("public");
    }

    public static PsiMethod getEventMethod(Event event) {
        if (event.getType().getStringValue().equals("java") && (event.getPath().getStringValue()) != null) {
            final PsiClass psiClass = findEventClass(event);
            if (psiClass != null) {
                for (PsiMethod psiMethod : psiClass.getAllMethods()) {
                    if (psiMethod != null) {
                        if (OfbizUtils.isEventMethod(psiMethod) &&
                                psiMethod.getName().equals(event.getInvoke().getStringValue())) {
                            return psiMethod;
                        }
                    }
                }

            }
        }
        return null;
    }

    public static PsiMethod getServiceMethod(Service service) {
        if (service.getEngine().getStringValue().equals("java") && (service.getLocation().getStringValue()) != null) {
            final PsiClass psiClass = findServiceClass(service);
            if (psiClass != null) {
                for (PsiMethod psiMethod : psiClass.getAllMethods()) {
                    if (OfbizUtils.isServiceMethod(psiMethod) &&
                            psiMethod.getName().equals(service.getInvoke().getStringValue())) {
                        return psiMethod;
                    }
                }

            }
        }
        return null;
    }

    public static XmlElement getEventServiceElememnt(Event event) {
        if (event.getType().getStringValue().equals("service") || event.getType().getStringValue().equals("service-multi")) {
            Module module = ModuleUtil.findModuleForPsiElement(event.getXmlElement());
            final String path = event.getPath().getStringValue();
            if (path == null || path.trim().equals("")) {
                ServiceModel serviceModel = ServiceManager.getInstance(event.getXmlElement().getProject()).getCombinedModel(module);
                for (Service service : serviceModel.getAllServices()) {
                    if (service.getName().getStringValue().equals(event.getInvoke().getStringValue())) {
                        return service.getXmlElement();
                    }
                }
            } else {
                List<DomFileElement<Services>> eles = DomService.getInstance().getFileElements(Services.class,
                        event.getXmlElement().getProject(), new GlobalSearchScope() {
                    @Override
                    public boolean contains(VirtualFile virtualFile) {
                        return virtualFile.getPath().endsWith(path);
                    }

                    @Override
                    public int compare(VirtualFile virtualFile, VirtualFile virtualFile1) {
                        return virtualFile.getPath().equals(virtualFile1.getPath()) ? 0 : 1;
                    }

                    @Override
                    public boolean isSearchInModuleContent(@NotNull Module module) {
                        return true;
                    }

                    @Override
                    public boolean isSearchInLibraries() {
                        return false;
                    }
                });
                for (DomFileElement<Services> ele : eles) {
                    Services services = ele.getRootElement();
                    for (Service service : services.getServices()) {
                        if (service.getName().getStringValue().equals(event.getInvoke())) {
                            return service.getXmlElement();
                        }
                    }
                }
            }

        }
        return null;
    }


    public static Set<String> getServiceCompleteMethodNames(Service service) {
        Set<String> psiMethods = new HashSet<String>();
        if (service.getEngine().getStringValue().equals("java")) {
            PsiClass psiClass = OfbizUtils.findServiceClass(service);
            if (psiClass != null && psiClass.getAllMethods() != null) {
                for (PsiMethod psiMethod : psiClass.getAllMethods()) {
                    if (OfbizUtils.isServiceMethod(psiMethod)) {
                        psiMethods.add(psiMethod.getName());
                    }
                }
            }
        }
        return psiMethods;
    }

    public static Set<String> getEventCompleteMethodNames(Event event) {
        Set<String> psiMethods = new HashSet<String>();
        if (event.getType().getStringValue().equals("java")) {
            PsiClass psiClass = OfbizUtils.findEventClass(event);
            if (psiClass != null) {
                for (PsiMethod psiMethod : psiClass.getAllMethods()) {
                    if (OfbizUtils.isEventMethod(psiMethod)) {
                        psiMethods.add(psiMethod.getName());
                    }
                }
            }
        }
        return psiMethods;
    }

    public static Set<Object> getEventCompleteServiceNames(Event event) {
        Set<Object> set = new HashSet<Object>();
        if (event.getType().getStringValue().equals("service") || event.getType().getStringValue().equals("service-multi")) {
            Module module = ModuleUtil.findModuleForPsiElement(event.getXmlElement());
            String path = event.getPath().getStringValue();
            ServiceModel serviceModel = ServiceManager.getInstance(event.getXmlElement().getProject()).getCombinedModel(module);
            for (Service service : serviceModel.getAllServices()) {
                if (path != null && !path.trim().equals("") && service.getXmlElement().getContainingFile().getVirtualFile().getPath().endsWith(path)) {
                    set.add(service.getName().getStringValue());
                } else {
//                    set.add(service.getName().getValue());
                    set.add(LookupElementBuilder.create(service.getName().getValue()).setTypeText(service.getXmlElement().getContainingFile().getVirtualFile().getPresentableName()));
                }

            }
        }
        return set;

    }

    public static PsiElement getServiceSimpleMethodElememnt(Service service) {
        if (service.getEngine().getStringValue().equals("simple")) {
            // Module module = ModuleUtil.findModuleForPsiElement(service.getXmlElement());
            String path = service.getLocation().getStringValue();
            if (path.startsWith("component://")) {
                path = path.substring("component://".length());
            }
            if (path != null && !path.trim().equals("")) {
                List<DomFileElement<SimpleMethods>> eles = DomService.getInstance().getFileElements(SimpleMethods.class,
                        service.getXmlElement().getProject(),
                        GlobalSearchScope.allScope(service.getXmlElement().getProject()));
                for (DomFileElement<SimpleMethods> ele : eles) {
                    if (ele.getFile().getVirtualFile().getPath().endsWith(path)) {
                        for (SimpleMethod simpleMethod : ele.getRootElement().getSimpleMethods()) {
                            if (simpleMethod.getMethodName().getStringValue().equals(service.getInvoke().getStringValue())) {
                                return simpleMethod.getXmlElement();
                            }
                        }
                    }
                }
            }

        }
        return null;
    }


    public static Set<String> getServiceCompleteSimpleMethodNames(Service service) {
        Set<String> set = new HashSet<String>();
        if (service.getEngine().getValue().equals("simple") && service.getInvoke() != null) {
            Module module = ModuleUtil.findModuleForPsiElement(service.getXmlElement());
            String path = service.getLocation().getStringValue();
            if (path.startsWith("component://")) {
                path = path.substring("component://".length());
            }
            List<DomFileElement<SimpleMethods>> eles = DomService.getInstance().getFileElements(SimpleMethods.class,
                    service.getXmlElement().getProject(), new ModulePsiFileTypeScope(module, XmlFileType.INSTANCE));
            for (DomFileElement<SimpleMethods> ele : eles) {
                if (ele.getXmlElement().getContainingFile().getVirtualFile().getPath().endsWith(path)) {
                    for (SimpleMethod simpleMethod : ele.getRootElement().getSimpleMethods()) {
                        set.add(simpleMethod.getMethodName().getStringValue());
                    }
                }
            }
        }
        return set;

    }

    public static PsiElement getServiceLocation(Service service) {
        ComponentManager manager = ComponentManager.getInstance(service.getXmlElement().getProject());
        if (service.getLocation() == null || service.getLocation().getValue() == null || !service.getEngine().getValue().equals("simple")) {
            return null;
        }
        String path = service.getLocation().getStringValue();
        if (!path.startsWith("component://")) {
            Component component = manager.getComponent(service.getXmlElement());
            path = String.format("file://%s/script/%s", component.getDirectory(), path);
        } else {
            path = "file://" + manager.getRealPath(path);
        }
        VirtualFile vf = VirtualFileManager.getInstance().findFileByUrl(path);
        if (vf != null) {
            return PsiManager.getInstance(service.getXmlElement().getProject()).findFile(vf);
        }


        return null;
    }

    public static Collection getServiceLocationNames(Service service) {
        ComponentManager componentManager = ComponentManager.getInstance(service.getXmlElement().getProject());
        String location = service.getLocation().getStringValue();

        if (service.getEngine().getValue().equals("simple")) {
            if (location.startsWith("comp")) {
                Component[] components = componentManager.getAllComponents();
                ComponentUrl url = new ComponentUrl(location);
                if (url.getComponentName() == null) {
                    return ContainerUtil.map(components, new Function<Component, Object>() {
                        @Override
                        public Object fun(Component component) {
                            return String.format("component://%s/", component.getName().getValue());
                        }
                    });
                } else {
                    final Component component = componentManager.getComponent(url.getComponentName(), components);
                    List<SimpleMethods> list = getDomFileElements(SimpleMethods.class, service.getXmlElement().getProject(), component.getScope());
                    List<String> serviceLocations = ContainerUtil.map(list, new Function<SimpleMethods, String>() {
                        @Override
                        public String fun(SimpleMethods simpleMethods) {
                            return ComponentUrl.buildComponentUrl(component, simpleMethods.getXmlElement().getContainingFile().getVirtualFile().getPath(), null);
                        }
                    });
                    return serviceLocations;
                }
            } else {
                Set<Object> set = new HashSet<Object>();
                Module module = ModuleUtil.findModuleForPsiElement(service.getXmlElement());
                List<DomFileElement<SimpleMethods>> eles = DomService.getInstance().getFileElements(SimpleMethods.class, service.getXmlElement().getProject(),
                        new ModulePsiFileTypeScope(module, XmlFileType.INSTANCE));
                String path;
                int pos;
                for (DomFileElement<SimpleMethods> ele : eles) {
                    path = ele.getFile().getVirtualFile().getPath();
                    pos = path.lastIndexOf("/script/");
                    if (pos >= 0) {
                        set.add(path.substring(pos + 8));
                    }
                }
                return set;
            }

        } else {
            return Collections.emptyList();
        }
    }

    public static Entity findEntity(Project project, String entityName) {
        List<EntityModel> models = getDomFileElements(EntityModel.class, project, GlobalSearchScope.projectScope(project));
        for (EntityModel model : models) {
            for (Entity entity : model.getEntities()) {
                if (entityName.equals(entity.getName().getValue())) {
                    return entity;
                }
            }
        }
        return null;
    }

    public static PsiElement getEventTypeElement(Event event) {
        ControllerManager controllerManager = ControllerManager.getInstance(event.getXmlElement().getProject());
        Controller controller = controllerManager.getController((XmlFile) event.getXmlElement().getContainingFile().getContainingFile());

        for (Handler handler : controller.getAllHandlers()) {
            if (handler.getName().equals(event.getType())) {
                return handler.getXmlElement();
            }
        }
        return null;
    }

    public static Set<String> getEventCompleteTypes(Event event, String type) {
        ControllerManager controllerManager = ControllerManager.getInstance(event.getXmlElement().getProject());
        Controller controller = controllerManager.getController((XmlFile) event.getXmlElement().getContainingFile().getContainingFile());
        Set<String> names = new HashSet<String>();
        for (Handler handler : controller.getAllHandlers()) {
            if(handler.getType().equals(type)){
                names.add(handler.getName().getStringValue());
            }

        }
        return names;
    }

    public static class ModulePsiFileTypeScope extends GlobalSearchScope {
        private final Module module;

        public ModulePsiFileTypeScope(Module module, XmlFileType instance) {
            this.module = module;
        }

        @Override
        public boolean contains(VirtualFile virtualFile) {
            return XmlFileType.INSTANCE == virtualFile.getFileType();
        }

        @Override
        public int compare(VirtualFile virtualFile, VirtualFile virtualFile1) {
            return virtualFile.getPath().equals(virtualFile1) ? 0 : 1;
        }

        @Override
        public boolean isSearchInModuleContent(@NotNull Module module) {
            return module == this.module;
        }

        @Override
        public boolean isSearchInLibraries() {
            return false;
        }
    }

    public static <T extends DomElement> List<T> getDomFileElements(Class<T> clazz, Project project, GlobalSearchScope scope) {
        List<T> list = new ArrayList<T>();
        List<DomFileElement<T>> eles =
                DomService.getInstance().getFileElements(clazz, project, scope);
        for (DomFileElement<T> ele : eles) {
            list.add(ele.getRootElement());
        }
        return list;
    }

    public static PsiFile findPsiFileByComponentUrl(@NotNull XmlElement xmlElement, @NotNull ComponentUrl url) {
        Project project = xmlElement.getProject();
        ComponentManager manager = ComponentManager.getInstance(project);
        Component[] components = manager.getAllComponents();
        Component currentComponent;

        if (url.getComponentName() == null) {
            currentComponent = manager.getComponent(xmlElement, components);
            if (currentComponent == null) {
                return null;
            }
            int pos = url.getUrl().indexOf("#");
            String relativePath = url.getUrl().substring(0, pos >= 0 ? pos : url.getUrl().length());
            String fileName = relativePath;
            pos = relativePath.lastIndexOf("/");
            if (pos >= 0) {
                fileName = relativePath.substring(pos);
            }
            PsiFile[] files = FilenameIndex.getFilesByName(project, fileName, currentComponent.getScope());
            for (PsiFile file : files) {
                if (file.getVirtualFile().getPath().endsWith(relativePath)) {
                    return file;
                }
            }
        } else {
            currentComponent = manager.getComponent(url.getComponentName(), components);
            String path = url.getRealPath(project, components);
            VirtualFile vf = VirtualFileManager.getInstance().findFileByUrl("file://" + path);
            if (vf != null) {
                PsiFile psiFile = PsiManager.getInstance(project).findFile(vf);
                if (psiFile != null) {
                    return psiFile;
                }
            }
        }
        return null;
    }

    public static PsiDirectory findPsiDirectoryByComponentUrl(@NotNull XmlElement xmlElement, @NotNull ComponentUrl url) {
        Project project = xmlElement.getProject();
        ComponentManager manager = ComponentManager.getInstance(project);
        Component[] components = manager.getAllComponents();

        if (url.getRelativeDirectory() != null) {
            String path = url.getRealDirectory(project, components);
            VirtualFile vf = VirtualFileManager.getInstance().findFileByUrl("file://" + path);
            if (vf != null && vf.isDirectory()) {
                return PsiManager.getInstance(project).findDirectory(vf);
            }
        }else if (url.getComponentName() != null) {
             VirtualFile vf = VirtualFileManager.getInstance().findFileByUrl("file://" + url.getComponentDirectory(project, components));
            if (vf != null && vf.isDirectory()) {
                return PsiManager.getInstance(project).findDirectory(vf);
            }
        }
        return null;
    }

    public static String removeIdeaPostFix(String s) {
        if (s == null) {
            return s;
        }
        s = s.trim();
        if (s.endsWith(IDEA_POSTFIX)) {
            return s.substring(0, s.length() - IDEA_POSTFIX.length());
        }
        return s;
    }

}
