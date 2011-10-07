package com.cfsoft.ofbiz.dom.simplemethod.api;

import com.cfsoft.ofbiz.OfbizDomIconProvider;
import com.intellij.ide.presentation.Presentation;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;
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
@Presentation(typeName = "Simple Methods Root", iconProviderClass = OfbizDomIconProvider.class)
public interface SimpleMethods extends DomElement {
    @NonNls
    String TAG_NAME = "simple-methods";

    @SubTagList(value = "simple-method")
    @NotNull
    List<SimpleMethod> getSimpleMethods();
}
