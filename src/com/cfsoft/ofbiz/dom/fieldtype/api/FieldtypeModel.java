package com.cfsoft.ofbiz.dom.fieldtype.api;

import com.cfsoft.ofbiz.OfbizDomPresentationProvider;
import com.intellij.ide.presentation.Presentation;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@Presentation(typeName = "Fieldtype Model Root", provider = OfbizDomPresentationProvider.class)
public interface FieldtypeModel extends DomElement {
    @NonNls
    String TAG_NAME = "fieldtypemodel";

    @SubTagList(value = "field-type-def")
    @NotNull
    List<FieldType> getFieldTypes();
}
