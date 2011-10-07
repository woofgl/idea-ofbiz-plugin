package com.cfsoft.ofbiz.dom.component.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;


public interface ComponentParentDirectory extends DomElement {
    @Attribute("parent-directory")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getParentDirectory();

}
