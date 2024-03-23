/*
 * The MIT License
 *
 * Copyright 2024 sebastian.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin;

import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.generator.JavaClassTemplate;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.model.JsonObjectClassDefinition;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.model.Property;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

final class CodeGenerator {

    private final ModelCreator modelGenerator;
    private final String packageName;
    private final JavaClassTemplate objectClassTemplate;
    private final JavaClassTemplate parameterClassTemplate;

    public CodeGenerator(final ModelCreator modelGenerator, final String packageName, final Templates templates) {
        this.objectClassTemplate = templates.createTemplate("default_json_object_class.mustache");
        this.parameterClassTemplate = templates.createTemplate("parameter_class.mustache");
        this.modelGenerator = Objects.requireNonNull(modelGenerator);
        this.packageName = Objects.requireNonNull(packageName);
    }

    void generateModel() throws IOException {
        modelGenerator.createModel().map(def -> def.withPackage(packageName)).forEach(this::generate);
    }

    private void generate(final JsonObjectClassDefinition clsDef) {
        objectClassTemplate.generate(clsDef);
        generateAlternatives(clsDef);
    }

    private void generateAlternatives(final JsonObjectClassDefinition clsDef) {
        Stream
            .concat(clsDef.properties().stream(), clsDef.required().stream())
            .map(Property::alternatives)
            .filter(Objects::nonNull)
            .map(def -> def.withPackage(packageName))
            .forEach(parameterClassTemplate::generate);
    }
}
