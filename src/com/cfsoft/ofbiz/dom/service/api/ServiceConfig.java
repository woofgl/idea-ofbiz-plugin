package com.cfsoft.ofbiz.dom.service.api;

import com.cfsoft.ofbiz.OfbizDomPresentationProvider;
import com.intellij.ide.presentation.Presentation;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTag;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;


//@Namespace(Constants.CONTROLLER_NAMESPACE_KEY)
@Presentation(typeName = "Service Config Root", provider = OfbizDomPresentationProvider.class)
public interface ServiceConfig extends DomElement {
    @NonNls
    String TAG_NAME = "service-config";

    @SubTag(value = "service-engine")
    @NotNull
    ServiceEngine getServiceEngine();




}
