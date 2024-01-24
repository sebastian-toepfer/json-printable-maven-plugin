/*
 * The MIT License
 *
 * Copyright 2023 sebastian.
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
package io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.model;

import static java.util.function.Predicate.not;

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * open-rpc-meta-schema simple schema scanner ... but not a real good one :)
 */
public final class SchemaParser {

    private final JsonObject schema;
    private final TypeRegistry typeRegistry;
    private final JsonTypeToJavaTypeMapping jsonTypeToJavaTypeMapping;

    public SchemaParser(
        final JsonObject schema,
        final TypeRegistry typeRegistry,
        final JsonTypeToJavaTypeMapping jsonTypeToJavaTypeMapping
    ) {
        this.schema = Objects.requireNonNull(schema);
        this.typeRegistry = Objects.requireNonNull(typeRegistry);
        this.jsonTypeToJavaTypeMapping = jsonTypeToJavaTypeMapping;
    }

    public List<JsonObjectClassDefinition> createModel() {
        final List<JsonObjectClassDefinition> result = new ArrayList<>();
        result.add(new DefaultJsonObjectClassDefinition(schema, typeRegistry, jsonTypeToJavaTypeMapping));
        if (schema.containsKey("definitions")) {
            result.addAll(
                schema
                    .getJsonObject("definitions")
                    .entrySet()
                    .stream()
                    .filter(not(e -> Objects.equals(e.getKey(), "specificationExtension")))
                    .filter(not(e -> Objects.equals(e.getKey(), "JSONSchema")))
                    .map(Map.Entry::getValue)
                    .map(JsonValue::asJsonObject)
                    .map(j -> new DefaultJsonObjectClassDefinition(j, typeRegistry, jsonTypeToJavaTypeMapping))
                    .toList()
            );
        }
        return Collections.unmodifiableList(result);
    }
}
