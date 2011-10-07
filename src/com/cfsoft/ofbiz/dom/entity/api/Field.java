package com.cfsoft.ofbiz.dom.entity.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;


public interface Field extends DomElement {
    @Attribute("type")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getType();

    @Attribute("name")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getName();

}
