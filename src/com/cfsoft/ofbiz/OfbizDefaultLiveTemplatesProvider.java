package com.cfsoft.ofbiz;

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;


public class OfbizDefaultLiveTemplatesProvider implements DefaultLiveTemplatesProvider {
    public String[] getDefaultLiveTemplateFiles() {
        return new String[]{"/liveTemplates/ofbiz"};
    }

    public String[] getHiddenLiveTemplateFiles() {
        return new String[0];
    }
}
