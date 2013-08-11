package com.cfsoft.ofbiz.dom.controller.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;


public interface Handler extends DomElement {
    @Attribute("class")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getClassName();

    @Attribute("name")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull

    GenericAttributeValue<String> getName();
    @Attribute("type")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getType();
}
