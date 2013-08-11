package com.cfsoft.ofbiz.dom.screen.api;

import com.cfsoft.ofbiz.OfbizDomPresentationProvider;
import com.intellij.ide.presentation.Presentation;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;


//@Namespace(Constants.CONTROLLER_NAMESPACE_KEY)
@Presentation(typeName = "Screens Root", provider = OfbizDomPresentationProvider.class)
public interface Screens extends DomElement {
    @NonNls
    String TAG_NAME = "screens";

    @SubTagList(value = "screen")
    @NotNull
    List<Screen> getScreens();
}
