package com.cfsoft.ofbiz.dom.simplemethod.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;


public interface SimpleMethod extends DomElement {
    @Attribute("method-name")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getMethodName();
}
