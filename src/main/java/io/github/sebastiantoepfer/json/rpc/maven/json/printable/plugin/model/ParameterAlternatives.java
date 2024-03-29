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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.github.sebastiantoepfer.ddd.common.Media;
import io.github.sebastiantoepfer.ddd.common.Printable;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
        return Stream
            .concat(
                alternatives().stream().map(registry::determineFullQualifiedNameOf).flatMap(Collection::stream),
                typesNeededByMethods()
            )
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

    private Stream<String> typesNeededByMethods() {
        return alternatives()
            .stream()
            .map(ParameterAlternative::methods)
            .flatMap(Collection::stream)
            .map(m -> {
                final List<Class<?>> result = new ArrayList<>();
                result.add(m.getReturnType());
                result.addAll(Arrays.asList(m.getParameterTypes()));
                return result;
            })
            .flatMap(Collection::stream)
            .filter(not(cls -> cls.getPackageName().startsWith("java.lang")))
            .map(Class::getCanonicalName);
    }

    @Override
    public String objectname() {
        return alternatives()
            .stream()
            .map(ParameterAlternative::type)
            .map(name -> {
                final String result;
                if (name.endsWith("Object")) {
                    result = name.substring(0, name.length() - "Object".length());
                } else {
                    result = name;
                }
                return result;
            })
            .collect(Collectors.joining("Or"));
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

    @Override
    public boolean hasAdditionalValues() {
        return additionalValues() != null;
    }

    @Override
    public AdditionalValue additionalValues() {
        return null;
    }

    public final class ParameterAlternative implements Typeable {

        private final JsonObject obj;

        private ParameterAlternative(final JsonObject obj) {
            this.obj = obj;
        }

        @Override
        @SuppressFBWarnings("EI_EXPOSE_REP")
        public JsonObjectClassDefinition owner() {
            return ParameterAlternatives.this;
        }

        @Override
        public String name() {
            final String result;
            if (type().startsWith("Reference")) {
                result = "reference";
            } else {
                result = "object";
            }
            return result;
        }

        @Override
        public String type() {
            return typeResolver().resolveType();
        }

        @Override
        public boolean isNullable() {
            return typeResolver().isNullable();
        }

        @Override
        public boolean hasInterfaces() {
            return typeResolver().hasInterfaces();
        }

        public Collection<String> interfaces() {
            return typeResolver().interfaces().map(Class::getSimpleName).toList();
        }

        public Collection<Method> methods() {
            return typeResolver()
                .interfaces()
                .map(Class::getMethods)
                .flatMap(Arrays::stream)
                .filter(not(m -> List.of("toString", "printOn").contains(m.getName())))
                .sorted(Comparator.comparing(Method::getName))
                .toList();
        }

        private JsonTypeToJavaTypeMapping.JavaTypeResolver typeResolver() {
            return jsonTypeToJavaTypeMapping.resolveFor(obj);
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
