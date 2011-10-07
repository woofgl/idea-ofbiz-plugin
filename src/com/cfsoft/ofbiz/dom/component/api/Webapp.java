package com.cfsoft.ofbiz.dom.component.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;


public interface Webapp extends DomElement {
    @Attribute("name")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getName();
    @Attribute("location")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getLocation();
    @Attribute("mount-point")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getMountPoint();

}
