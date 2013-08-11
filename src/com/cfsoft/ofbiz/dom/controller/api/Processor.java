package com.cfsoft.ofbiz.dom.controller.api;

import com.intellij.util.xml.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public interface Processor extends DomElement {
    @SubTagList(value = "event")
    List<Event> getEvents();
}
