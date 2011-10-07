package com.cfsoft.ofbiz.dom.entity.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public interface ViewEntity extends DomElement {
    @Attribute("entity-name")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getName();

    @Attribute("package-name")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getPackage();

    @SubTagList("relation")
    List<Relation> getRelations();
    @SubTagList("member-entity")
    List<MemberEntity> getmMemberEntitys();

    @SubTagList("alias")
    List<Alias>  getAliass();
    @SubTagList("alias-all")
    List<AliasAll>  getAliasAlls();
    @SubTagList("view-link")
    List<ViewLink> getviViewLinks();
}
