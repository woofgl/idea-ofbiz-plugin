package com.cfsoft.ofbiz.dom.entity.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public interface ViewLink extends DomElement {
    @Attribute("entity-alias")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getEntityAlias();

    @Attribute("rel-entity-alias")
    @NameValue
    @NotNull
    GenericAttributeValue<String> getRelEntityAlias();

    @SubTagList("key-map")
    List<KeyMap> getKeyMaps();
}
