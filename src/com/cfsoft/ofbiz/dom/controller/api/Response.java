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
public interface Response extends DomElement {
    @Attribute("value")
    @NameValue
    @Required(nonEmpty = true)
    @Convert(ResponseValueConverter.class)
    @NotNull
    GenericAttributeValue<String> getViewName();
    @Attribute("name")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getName();
    @Attribute("type")
    @NameValue
    @Required(nonEmpty = true)
    @NotNull
    GenericAttributeValue<String> getType();
}
