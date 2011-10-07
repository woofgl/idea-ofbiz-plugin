package com.cfsoft.ofbiz.dom.service.api;

import com.cfsoft.ofbiz.OfbizDomIconProvider;
import com.intellij.ide.presentation.Presentation;
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
@Presentation(typeName = "Services Root", iconProviderClass = OfbizDomIconProvider.class)
public interface Services extends DomElement {
    @NonNls
    String TAG_NAME = "services";

    @SubTagList(value = "service")
    @NotNull
    List<Service> getServices();




}
