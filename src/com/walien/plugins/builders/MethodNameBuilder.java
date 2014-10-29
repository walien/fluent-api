package com.walien.plugins.builders;

import com.google.common.base.CaseFormat;
import com.intellij.psi.PsiField;

public class MethodNameBuilder {

    private static String format(PsiField field) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, field.getName());
    }

    public static String buildGetterName(PsiField field) {
        return String.format("get%s", format(field));
    }

    public static String buildSetterName(PsiField field) {
        return String.format("set%s", format(field));
    }
}
