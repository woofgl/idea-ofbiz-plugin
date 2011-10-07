package com.cfsoft.ofbiz.dom.entity.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public interface Relation extends DomElement {
    @Attribute("type")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getType();

    @Attribute("fk-name")
    @NameValue
    @NotNull
    GenericAttributeValue<String> getFkName();

    @Attribute("rel-entity-name")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getRelEntityName();

    @SubTagList("key-map")
    List<KeyMap> getKeyMaps();
}
