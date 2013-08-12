package com.cfsoft.ofbiz.dom.resouce.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;


public interface Property extends DomElement {
    @Attribute("key")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getKey();
}
