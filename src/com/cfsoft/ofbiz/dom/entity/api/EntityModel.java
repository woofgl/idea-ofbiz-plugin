package com.cfsoft.ofbiz.dom.entity.api;

import com.cfsoft.ofbiz.OfbizDomIconProvider;
import com.cfsoft.ofbiz.dom.fieldtype.api.FieldType;
import com.intellij.ide.presentation.Presentation;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@Presentation(typeName = "Entity Model Root", iconProviderClass = OfbizDomIconProvider.class)
public interface EntityModel extends DomElement {
    @NonNls
    String TAG_NAME = "entitymodel";

    @SubTagList(value = "entity")
    List<Entity> getEntities();
    @SubTagList(value = "view-entity")
    List<ViewEntity> getViewEntities();
    @SubTagList(value = "extend-entity")
    List<ExtendEntity> getExtendEntities();
}
