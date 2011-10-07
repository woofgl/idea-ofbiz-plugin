package com.cfsoft.ofbiz.dom.controller.api;

import com.cfsoft.ofbiz.OfbizDomIconProvider;
import com.intellij.ide.presentation.Presentation;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTag;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 9/21/11
 * Time: 12:00 AM
 * To change this template use File | Settings | File Templates.
 */
//@Namespace(Constants.CONTROLLER_NAMESPACE_KEY)
@Presentation(typeName = "Controller Root", iconProviderClass = OfbizDomIconProvider.class)
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

    public Set<Controller> getAllIncludeBy();

}
