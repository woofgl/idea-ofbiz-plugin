package com.cfsoft.ofbiz.facet.ui;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.util.containers.MultiMap;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 9/22/11
 * Time: 12:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OfbizConfigsSercher {
    void search();

    MultiMap<Module, PsiFile> getFilesByModules();

    MultiMap<VirtualFile, PsiFile> getJars();
}
