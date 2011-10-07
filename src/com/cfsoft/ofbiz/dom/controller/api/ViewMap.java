package com.cfsoft.ofbiz.dom.controller.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 9/21/11
 * Time: 12:08 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ViewMap extends DomElement {
    @Attribute("name")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getName();
    @Attribute("type")
    @NameValue
    GenericAttributeValue<String> getType();

    @Attribute("page")
    @NameValue
    GenericAttributeValue<String> getPage();
}
