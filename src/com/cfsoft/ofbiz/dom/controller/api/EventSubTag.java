package com.cfsoft.ofbiz.dom.controller.api;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.SubTagList;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 9/21/11
 * Time: 12:49 AM
 * To change this template use File | Settings | File Templates.
 */
public interface EventSubTag extends DomElement {
    @SubTagList(value = "event")
    List<Event> getEvents();
}
