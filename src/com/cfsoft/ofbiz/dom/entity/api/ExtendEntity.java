package com.cfsoft.ofbiz.dom.entity.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public interface ExtendEntity extends DomElement {
    @Attribute("entity-name")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getEntityName();

    @SubTagList("field")
    List<Field>  getFields();
    @SubTagList("prim-key")
    List<PrimKey> getpPrimKeys();
    @SubTagList("relation")
    List<Relation> getRelations();

}
