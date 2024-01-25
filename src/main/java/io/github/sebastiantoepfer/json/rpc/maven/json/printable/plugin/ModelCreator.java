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

import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.model.CompositeTypeRegistry;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.model.JsonObjectClassDefinition;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.model.JsonTypeToJavaTypeMapping;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.model.PrintableAdapters;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.model.SchemaParser;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

final class ModelCreator {

    private final JsonSchemaProvider provider;
    private final JsonTypeToJavaTypeMapping typeRegistry;

    public ModelCreator(final JsonSchemaProvider provider, final JsonTypeToJavaTypeMapping typeRegistry) {
        this.provider = Objects.requireNonNull(provider);
        this.typeRegistry = Objects.requireNonNull(typeRegistry);
    }

    public Stream<JsonObjectClassDefinition> createModel() throws IOException {
        return new SchemaParser(
            provider.loadSchema(),
            new CompositeTypeRegistry(List.of(typeRegistry, new PrintableAdapters())),
            typeRegistry
        )
            .createModel()
            .stream();
    }
}
