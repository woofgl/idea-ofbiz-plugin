package com.cfsoft.ofbiz.dom.simplemethod.api;

import com.cfsoft.ofbiz.Constants;
import com.cfsoft.ofbiz.OfbizDomPresentationProvider;
import com.intellij.ide.presentation.Presentation;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.Namespace;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;


//@Namespace(Constants.CONTROLLER_NAMESPACE_KEY)
@Presentation(typeName = "Simple Methods Root",  provider = OfbizDomPresentationProvider.class)
public interface SimpleMethods extends DomElement {
    @NonNls
    String TAG_NAME = "simple-methods";

    @SubTagList(value = "simple-method")
    @NotNull
    List<SimpleMethod> getSimpleMethods();
}
