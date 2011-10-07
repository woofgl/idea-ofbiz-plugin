package com.cfsoft.ofbiz.dom.fieldtype.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;


public interface FieldType extends DomElement {
    @Attribute("type")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getType();

    @Attribute("sql-type")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getSqlType();

    @Attribute("java-type")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getJavaType();
}
