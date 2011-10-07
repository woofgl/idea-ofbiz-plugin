package com.cfsoft.ofbiz.dom.component.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;


public interface ComponentLocation extends DomElement {
    @Attribute("component-location")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getLocation();

}
