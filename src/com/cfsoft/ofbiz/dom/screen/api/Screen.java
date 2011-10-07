package com.cfsoft.ofbiz.dom.screen.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;


public interface Screen extends DomElement {
    @Attribute("name")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getName();
}
