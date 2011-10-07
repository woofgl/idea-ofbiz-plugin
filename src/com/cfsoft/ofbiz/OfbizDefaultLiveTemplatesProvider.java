package com.cfsoft.ofbiz;

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 9/20/11
 * Time: 11:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class OfbizDefaultLiveTemplatesProvider implements DefaultLiveTemplatesProvider {
    public String[] getDefaultLiveTemplateFiles() {
        return new String[]{"/liveTemplates/ofbiz"};
    }

    public String[] getHiddenLiveTemplateFiles() {
        return new String[0];
    }
}
