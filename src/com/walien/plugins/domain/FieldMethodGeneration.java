package com.walien.plugins.domain;

import com.intellij.psi.PsiField;

public class FieldMethodGeneration {

    private PsiField field;

    private boolean generateGetter;

    private boolean generateSetter;

    public PsiField getField() {
        return field;
    }

    public boolean isGenerateGetter() {
        return generateGetter;
    }

    public boolean isGenerateSetter() {
        return generateSetter;
    }

    public FieldMethodGeneration setField(final PsiField field) {
        this.field = field;
        return this;
    }

    public FieldMethodGeneration setGenerateGetter(final boolean generateGetter) {
        this.generateGetter = generateGetter;
        return this;
    }

    public FieldMethodGeneration setGenerateSetter(final boolean generateSetter) {
        this.generateSetter = generateSetter;
        return this;
    }
}
