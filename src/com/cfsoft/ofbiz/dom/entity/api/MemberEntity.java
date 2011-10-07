package com.cfsoft.ofbiz.dom.entity.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public interface MemberEntity extends DomElement {
    @Attribute("entity-name")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getEntityName();

    @Attribute("entity-alias")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getEntityAlias();

    public Entity getEntity();


}
