package com.walien.plugins.generator;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiField;
import com.walien.plugins.builders.MethodNameBuilder;

public class GetterGenerator extends AbstractMethodGenerator {

    public GetterGenerator(Project project) {
        super(project);
    }

    @Override
    protected String buildCode(PsiField field) {
        return String.format("public %s %s() { return this.%s; }",
                field.getType().getCanonicalText(),
                MethodNameBuilder.buildGetterName(field),
                field.getName()
        );
    }
}
