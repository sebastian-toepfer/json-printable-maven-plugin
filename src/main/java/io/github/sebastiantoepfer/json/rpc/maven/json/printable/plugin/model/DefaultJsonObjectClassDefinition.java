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

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.ddd.common.Printable;
import io.github.sebastiantoepfer.ddd.printables.core.CompositePrintable;
import io.github.sebastiantoepfer.ddd.printables.core.NamedPrintable;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.utils.FirstCharToUpperCase;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.processing.Generated;

class DefaultJsonObjectClassDefinition implements JsonObjectClassDefinition {

    private final JsonObject object;
    private final TypeRegistry typeRegistry;
    private final JsonTypeToJavaTypeMapping jsonTypeToJavaTypeMapping;
    private String packageName;

    DefaultJsonObjectClassDefinition(
        final JsonObject object,
        final TypeRegistry typeRegistry,
        final JsonTypeToJavaTypeMapping jsonTypeToJavaTypeMapping
    ) {
        this.object = Objects.requireNonNull(object);
        this.typeRegistry = Objects.requireNonNull(typeRegistry);
        this.jsonTypeToJavaTypeMapping = jsonTypeToJavaTypeMapping;
    }

    @Override
    public String objectname() {
        return new FirstCharToUpperCase(object.getString("title")).toCase();
    }

    @Override
    public Set<String> imports() {
        return determineImports(createPropertiesDefinitions());
    }

    @Override
    public List<EnumProperty> enums() {
        return createPropertiesDefinitions()
            .stream()
            .filter(e -> e.enumValue() != null)
            .map(p -> p.enumValue())
            .toList();
    }

    @Override
    public List<Property> properties() {
        final List<String> required = determineRequired();
        return createPropertiesDefinitions().stream().filter(not(p -> required.contains(p.name()))).toList();
    }

    @Override
    public List<Property> required() {
        final List<String> required = determineRequired();
        return createPropertiesDefinitions().stream().filter(p -> required.contains(p.name())).toList();
    }

    @Override
    public JsonObjectClassDefinition withPackage(final String packageName) {
        this.packageName = packageName;
        return this;
    }

    @Override
    public String packagename() {
        return packageName;
    }

    @Override
    public boolean hasAdditionalValues() {
        return additionalValues() != null;
    }

    @Override
    public AdditionalValue additionalValues() {
        return object
            .getOrDefault("patternProperties", JsonValue.EMPTY_JSON_OBJECT)
            .asJsonObject()
            .entrySet()
            .stream()
            .filter(e -> e.getValue().asJsonObject().containsKey("$ref"))
            .limit(1)
            .map(e ->
                new AdditionalValue(
                    e.getKey(),
                    jsonTypeToJavaTypeMapping.resolveFor(e.getValue().asJsonObject()).resolveType()
                )
            )
            .findFirst()
            .orElse(null);
    }

    private Set<String> determineImports(final List<Property> properties) {
        return properties
            .stream()
            .map(typeRegistry::determineFullQualifiedNameOf)
            .flatMap(Collection::stream)
            .filter(Predicate.not(String::isBlank))
            .collect(Collectors.toCollection(() -> new TreeSet<>(commonImports())));
    }

    private Collection<String> commonImports() {
        final Stream<Class<?>> result;
        final Stream<Class<?>> basicImports = Stream.of(
            Generated.class,
            Media.class,
            Printable.class,
            CompositePrintable.class,
            Objects.class
        );
        if (hasAdditionalValues()) {
            result =
                Stream.concat(basicImports, Stream.of(Map.class, HashMap.class, Pattern.class, NamedPrintable.class));
        } else {
            result = basicImports;
        }
        return result.map(Class::getName).toList();
    }

    private List<Property> createPropertiesDefinitions() {
        return object
            .getOrDefault("properties", JsonValue.EMPTY_JSON_OBJECT)
            .asJsonObject()
            .entrySet()
            .stream()
            .filter(not(e -> Objects.equals(e.getKey(), "$schema")))
            .map(json -> new Property(this, typeRegistry, jsonTypeToJavaTypeMapping, json))
            .toList();
    }

    private List<String> determineRequired() {
        final List<String> required;
        if (object.containsKey("required")) {
            required =
                object
                    .getJsonArray("required")
                    .stream()
                    .map(JsonString.class::cast)
                    .map(JsonString::getString)
                    .toList();
        } else {
            required = Collections.emptyList();
        }
        return required;
    }
}
