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

import io.github.sebastiantoepfer.ddd.common.Printable;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.utils.FirstCharToUpperCase;
import jakarta.json.JsonObject;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class JsonTypeToJavaTypeMapping implements TypeRegistry {
    private final Map<String, ClassType> jsonToJava;

    public JsonTypeToJavaTypeMapping(final String schemaClassName) {
        this.jsonToJava =
            Map.ofEntries(
                Map.entry("integer", new ClassType(long.class.getSimpleName())),
                Map.entry("boolean", new ClassType(boolean.class.getSimpleName())),
                Map.entry("uri", new ClassType(URL.class.getSimpleName(), URL.class.getCanonicalName())),
                Map.entry("array", new ClassType(List.class.getSimpleName(), List.class.getCanonicalName())),
                Map.entry("object", new ClassType(Printable.class.getSimpleName(), Printable.class.getCanonicalName())),
                Map.entry("$ref", new ClassType(String.class.getSimpleName())),
                Map.entry(
                    "JSONSchema",
                    new ClassType(schemaClassName.substring(schemaClassName.lastIndexOf(".") + 1), schemaClassName)
                )
            );
    }

    @Override
    public Collection<String> fullQulifiedNameOf(final Typeable property) {
        final Collection<String> result = new HashSet<>();
        result.addAll(fullQulifiedNameOf(property.type()));
        if (property.genericType() != null) {
            result.addAll(fullQulifiedNameOf(property.genericType()));
        }
        return result;
    }

    private Collection<String> fullQulifiedNameOf(final String typeName) {
        return jsonToJava
            .values()
            .stream()
            .filter(type -> type.hasName(typeName))
            .map(ClassType::fullQulifiedName)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    }

    public JavaTypeResolver resolveFor(final JsonObject typeInfo) {
        return new JavaTypeResolver(typeInfo);
    }

    public class JavaTypeResolver {
        private final JsonObject typeInfo;

        private JavaTypeResolver(final JsonObject typeInfo) {
            this.typeInfo = typeInfo;
        }

        public String resolveType() {
            final String result;
            final String jsonType = new JsonTypeResolver(typeInfo).resolveType();
            if (jsonToJava.containsKey(jsonType)) {
                result = jsonToJava.get(jsonType).name();
            } else if (List.of("oneOf", "enum").contains(jsonType)) {
                result = new FirstCharToUpperCase(typeInfo.getString("title")).toCase();
            } else {
                result = new FirstCharToUpperCase(jsonType).toCase();
            }
            return result;
        }
    }

    private static class ClassType {
        private final String name;
        private final String fullQualifiedName;

        public ClassType(final String name) {
            this(name, null);
        }

        public ClassType(final String name, final String fullQualifiedName) {
            this.name = Objects.requireNonNull(name);
            this.fullQualifiedName = fullQualifiedName;
        }

        public String name() {
            return name;
        }

        public Optional<String> fullQulifiedName() {
            return Optional.ofNullable(fullQualifiedName);
        }

        public boolean hasName(final String name) {
            return this.name.equals(name);
        }
    }
}
