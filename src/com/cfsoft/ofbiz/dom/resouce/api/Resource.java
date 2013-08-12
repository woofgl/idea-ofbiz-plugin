package com.cfsoft.ofbiz.dom.resouce.api;

import com.cfsoft.ofbiz.OfbizDomPresentationProvider;
import com.cfsoft.ofbiz.dom.fieldtype.api.FieldType;
import com.intellij.ide.presentation.Presentation;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@Presentation(typeName = "Resource Root", provider = OfbizDomPresentationProvider.class)
public interface Resource extends DomElement {
    @NonNls
    String TAG_NAME = "resource";

    @SubTagList(value = "property")
    @NotNull
    List<Property> getProperties();
}
