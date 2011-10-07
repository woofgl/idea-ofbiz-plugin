package com.cfsoft.ofbiz.dom.entity.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;


public interface Alias extends DomElement {
    @Attribute("entity-alias")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getEntityAlias();

    @Attribute("name")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getName();

    @Attribute("field")
    @NameValue
    GenericAttributeValue<String> getField();

    @Attribute("function")
    @NameValue
    GenericAttributeValue<String> getFunction();

    @Attribute("group-by")
    @NameValue
    GenericAttributeValue<String> getGroupBy();

}
