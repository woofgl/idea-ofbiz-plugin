package com.cfsoft.ofbiz.dom.entity.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;


public interface AliasAll extends DomElement {
    @Attribute("entity-alias")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getEntityAlias();

    @Attribute("prefix")
    @NameValue
    GenericAttributeValue<String> getPrefix();

}
