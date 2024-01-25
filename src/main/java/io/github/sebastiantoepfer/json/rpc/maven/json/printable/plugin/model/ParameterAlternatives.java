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
import static java.util.stream.Collectors.toCollection;

import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.ddd.common.Printable;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.utils.FirstCharToUpperCase;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.processing.Generated;

public final class ParameterAlternatives implements JsonObjectClassDefinition {

    private final Typeable parent;
    private final JsonArray oneOf;
    private final TypeRegistry registry;
    private final JsonTypeToJavaTypeMapping jsonTypeToJavaTypeMapping;

    public ParameterAlternatives(
        final Typeable parent,
        final JsonArray oneOf,
        final TypeRegistry registry,
        final JsonTypeToJavaTypeMapping jsonToJavaMapping
    ) {
        this.parent = Objects.requireNonNull(parent);
        this.oneOf = Objects.requireNonNull(oneOf);
        this.registry = Objects.requireNonNull(registry);
        this.jsonTypeToJavaTypeMapping = Objects.requireNonNull(jsonToJavaMapping);
    }

    @Override
    public List<EnumProperty> enums() {
        return List.of();
    }

    @Override
    public Set<String> imports() {
        return alternatives()
            .stream()
            .map(registry::fullQulifiedNameOf)
            .flatMap(Collection::stream)
            .filter(not(String::isBlank))
            .collect(
                toCollection(() ->
                    new TreeSet<>(
                        Set.of(
                            Generated.class.getName(),
                            Media.class.getName(),
                            Printable.class.getName(),
                            Objects.class.getName()
                        )
                    )
                )
            );
    }

    @Override
    public String objectname() {
        return new FirstCharToUpperCase(parent.name()).toCase();
    }

    @Override
    public List<Property> properties() {
        return List.of();
    }

    @Override
    public List<Property> required() {
        return List.of();
    }

    @Override
    public String packagename() {
        return parent.owner().packagename();
    }

    @Override
    public JsonObjectClassDefinition withPackage(final String packagename) {
        return this;
    }

    public List<ParameterAlternative> alternatives() {
        return oneOf.stream().map(JsonValue::asJsonObject).map(ParameterAlternative::new).toList();
    }

    public final class ParameterAlternative implements Typeable {

        private final JsonObject obj;

        private ParameterAlternative(final JsonObject obj) {
            this.obj = obj;
        }

        @Override
        public JsonObjectClassDefinition owner() {
            return ParameterAlternatives.this;
        }

        @Override
        public String name() {
            final String result;
            if (type().startsWith("Reference")) {
                result = "Reference";
            } else {
                result = "Object";
            }
            return result;
        }

        @Override
        public String type() {
            return jsonTypeToJavaTypeMapping.resolveFor(obj).resolveType();
        }

        @Override
        public boolean isNullable() {
            return jsonTypeToJavaTypeMapping.resolveFor(obj).isNullable();
        }

        @Override
        public String genericType() {
            return null;
        }

        @Override
        public String adapter() {
            return null;
        }
    }
}
