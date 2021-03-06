package com.cfsoft.ofbiz.dom.controller.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;


public interface Response extends DomElement {
    @Attribute("value")
    @NameValue
    @Required(nonEmpty = true)
    @Convert(ResponseValueConverter.class)
    @NotNull
    GenericAttributeValue<String> getViewName();
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
