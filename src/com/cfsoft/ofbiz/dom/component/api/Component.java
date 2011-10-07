package com.cfsoft.ofbiz.dom.component.api;

import com.cfsoft.ofbiz.OfbizDomIconProvider;
import com.cfsoft.ofbiz.dom.controller.api.Controller;
import com.cfsoft.ofbiz.dom.entity.api.Entity;
import com.cfsoft.ofbiz.dom.screen.api.Screen;
import com.cfsoft.ofbiz.dom.service.api.Service;
import com.cfsoft.ofbiz.dom.simplemethod.api.SimpleMethod;
import com.intellij.ide.presentation.Presentation;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.xml.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 9/21/11
 * Time: 12:00 AM
 * To change this template use File | Settings | File Templates.
 */
//@Namespace(Constants.CONTROLLER_NAMESPACE_KEY)
@Presentation(typeName = "Component Root", iconProviderClass = OfbizDomIconProvider.class)
public interface Component extends DomElement {
    @NonNls
    String TAG_NAME = "ofbiz-component";

    public static final String NAME = "ofbiz-component.xml";

    @Attribute("name")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getName();

    @SubTagList("webapp")
    List<Webapp> getWebapps();



    String getDirectory();

    List<Service> getAllServices();

    List<Screen>  getAllScreens();

    List<SimpleMethod> getAllSimpleMethods();

    List<Controller> getAllControllers();

    GlobalSearchScope getScope();

    List<Entity> getAllEntities();

}
