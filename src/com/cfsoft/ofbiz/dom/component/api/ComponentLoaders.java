package com.cfsoft.ofbiz.dom.component.api;

import com.cfsoft.ofbiz.OfbizDomIconProvider;
import com.intellij.ide.presentation.Presentation;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NonNls;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 9/21/11
 * Time: 12:00 AM
 * To change this template use File | Settings | File Templates.
 */
//@Namespace(Constants.CONTROLLER_NAMESPACE_KEY)
@Presentation(typeName = "Component Load Root", iconProviderClass = OfbizDomIconProvider.class)
public interface ComponentLoaders extends DomElement {
    @NonNls
    String TAG_NAME = "component-loader";

    public static final String ROOT_PATH = "framework/base/config/component-load.xml";
    public static final String NAME = "component-load.xml";
    public static final String HOT_DEPLOY = "hot-deploy";

    @SubTagList(value = "load-components")
    List<ComponentParentDirectory> getComponentParentDirectorys();

    @SubTagList(value = "load-component")
    List<ComponentLocation> getComponentLocations();




}
