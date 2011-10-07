package com.cfsoft.ofbiz.dom.component;

import com.cfsoft.ofbiz.dom.component.api.Component;
import com.cfsoft.ofbiz.dom.fieldtype.api.FieldtypeModel;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NotNullLazyKey;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.xml.XmlElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 9/27/11
 * Time: 2:40 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ComponentManager {
    private static final NotNullLazyKey<ComponentManager, Project> INSTANCE_KEY = ServiceManager.createLazyKey(ComponentManager.class);

    public static ComponentManager getInstance(Project project) {
      return INSTANCE_KEY.getValue(project);
    }

    public abstract Component[]  getAllComponents();

    public abstract Component getComponent(String name, Component ... components);

    public abstract String getComponentUrl(XmlElement xmlElement,Component...components);

    public abstract String getComponentUrl(String path,Component...components);

    public abstract String getRealPath(String componentUrl,Component...components);

    public abstract boolean isComponentUrl(String url,Component...components);

    public abstract Component getComponent(@NotNull XmlElement xmlElement,Component...components);

    public abstract FieldtypeModel getFieldtypeModel(XmlElement xmlElement);

}
