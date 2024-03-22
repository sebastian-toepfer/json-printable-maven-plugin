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

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

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

    public Stream<JsonObjectClassDefinition> createModel() {
        return Stream.concat(
            Stream.of(new DefaultJsonObjectClassDefinition(schema, typeRegistry, jsonTypeToJavaTypeMapping)),
            Stream.of("properties", "definitions").flatMap(this::extractDefinitionsFromProperty)
        );
    }

    private Stream<JsonObjectClassDefinition> extractDefinitionsFromProperty(final String propertyName) {
        final Stream<JsonObjectClassDefinition> result;
        if (schema.containsKey(propertyName)) {
            result =
                schema
                    .getJsonObject(propertyName)
                    .entrySet()
                    .stream()
                    .filter(not(e -> Objects.equals(e.getKey(), "specificationExtension")))
                    .filter(not(e -> Objects.equals(e.getKey(), "JSONSchema")))
                    .map(Map.Entry::getValue)
                    .filter(e -> e.getValueType() == JsonValue.ValueType.OBJECT)
                    .map(JsonValue::asJsonObject)
                    .filter(e -> e.containsKey("type") && e.get("type").equals(Json.createValue("object")))
                    .flatMap(this::createDefinitions);
        } else {
            result = Stream.empty();
        }
        return result;
    }

    private Stream<JsonObjectClassDefinition> createDefinitions(final JsonObject subSchema) {
        return new SchemaParser(subSchema, typeRegistry, jsonTypeToJavaTypeMapping).createModel();
    }
}
