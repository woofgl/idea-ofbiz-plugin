package com.cfsoft.ofbiz.dom.entity.api;

import com.cfsoft.ofbiz.OfbizDomPresentationProvider;
import com.intellij.ide.presentation.Presentation;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;
import org.jetbrains.annotations.NonNls;

import java.util.List;


@Presentation(typeName = "Entity Model Root", provider = OfbizDomPresentationProvider.class)
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
