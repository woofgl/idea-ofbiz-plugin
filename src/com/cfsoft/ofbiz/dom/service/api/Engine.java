package com.cfsoft.ofbiz.dom.service.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;


public interface Engine extends DomElement {
    @Attribute("name")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getName();

    @Attribute("class")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getClassName();


}
