package com.cfsoft.ofbiz.dom.controller.api;

import com.cfsoft.ofbiz.OfbizDomPresentationProvider;
import com.intellij.ide.presentation.Presentation;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTag;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;


//@Namespace(Constants.CONTROLLER_NAMESPACE_KEY)
@Presentation(typeName = "Controller Root", provider = OfbizDomPresentationProvider.class)
public interface Controller extends DomElement {
    @NonNls
    String TAG_NAME = "site-conf";

    @SubTagList(value = "request-map")
    @NotNull
    List<RequestMap> getRequestMaps();

    List<RequestMap> getAllRequestMaps();

    @SubTagList(value = "include")
    List<Include> getIncludes();

    @SubTagList(value = "view-map")
    @NotNull
    List<ViewMap> getViewMaps();

    List<ViewMap> getAllViewMaps();

    @SubTag(value = "preprocessor")
    Processor getPreprocessor();

    @SubTag(value = "postprocessor")
    Processor getPostprocessor();

    @SubTagList(value = "handler")
    List<Handler> getHandlers();

    public Set<Controller> getAllIncludeBy();

    List<Handler> getAllHandlers();
}
