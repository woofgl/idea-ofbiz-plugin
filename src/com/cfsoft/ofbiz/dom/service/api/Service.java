package com.cfsoft.ofbiz.dom.service.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;


public interface Service extends DomElement {
    @Attribute("engine")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getEngine();

    @Attribute("name")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getName();

    @Attribute("location")
    @NameValue
    GenericAttributeValue<String> getLocation();

    @Attribute("invoke")
    @NameValue
    @NotNull
    GenericAttributeValue<String> getInvoke();




}
