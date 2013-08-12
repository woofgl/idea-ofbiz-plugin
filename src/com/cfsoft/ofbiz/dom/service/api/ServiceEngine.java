package com.cfsoft.ofbiz.dom.service.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public interface ServiceEngine extends DomElement {
    @SubTagList(value = "engine")
    @NotNull
    List<Engine> getEngines();

    @Attribute("name")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getName();
}
