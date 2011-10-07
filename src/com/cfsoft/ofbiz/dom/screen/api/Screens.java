package com.cfsoft.ofbiz.dom.screen.api;

import com.cfsoft.ofbiz.OfbizDomIconProvider;
import com.cfsoft.ofbiz.dom.service.api.Service;
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
@Presentation(typeName = "Screens Root", iconProviderClass = OfbizDomIconProvider.class)
public interface Screens extends DomElement {
    @NonNls
    String TAG_NAME = "screens";

    @SubTagList(value = "screen")
    @NotNull
    List<Screen> getScreens();
}
