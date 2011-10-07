package com.cfsoft.ofbiz.dom.controller.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 9/21/11
 * Time: 12:08 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Processor extends DomElement {
    @SubTagList(value = "event")
    List<Event> getEvents();
}
