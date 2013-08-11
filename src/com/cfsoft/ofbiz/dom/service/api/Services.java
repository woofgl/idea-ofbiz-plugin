package com.cfsoft.ofbiz.dom.service.api;

import com.cfsoft.ofbiz.OfbizDomPresentationProvider;
import com.intellij.ide.presentation.Presentation;
import com.intellij.util.xml.*;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;


//@Namespace(Constants.CONTROLLER_NAMESPACE_KEY)
@Presentation(typeName = "Services Root", provider = OfbizDomPresentationProvider.class)
public interface Services extends DomElement {
    @NonNls
    String TAG_NAME = "services";

    @SubTagList(value = "service")
    @NotNull
    List<Service> getServices();




}
