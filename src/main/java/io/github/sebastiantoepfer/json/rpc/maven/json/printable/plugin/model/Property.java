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

import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.utils.FirstCharToUpperCase;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.util.Map;
import java.util.Objects;

public final class Property implements Typeable {

    private final JsonObjectClassDefinition owner;
    private final TypeRegistry typeRegistry;
    private final Map.Entry<String, JsonValue> json;
    private final JsonTypeToJavaTypeMapping jsonTypeToJavaTypeMapping;

    Property(
        final JsonObjectClassDefinition owner,
        final TypeRegistry typeRegistry,
        final JsonTypeToJavaTypeMapping jsonTypeToJavaTypeMapping,
        final Map.Entry<String, JsonValue> property
    ) {
        this.owner = Objects.requireNonNull(owner);
        this.typeRegistry = Objects.requireNonNull(typeRegistry);
        this.json = Objects.requireNonNull(property);
        this.jsonTypeToJavaTypeMapping = jsonTypeToJavaTypeMapping;
    }

    public String getToString() {
        final String result;
        if (Objects.equals(type(), "URL")) {
            result = "toExternalForm";
        } else if (Objects.equals("enum", jsonType())) {
            result = "toString";
        } else {
            result = null;
        }
        return result;
    }

    @Override
    public String adapter() {
        final String result;
        if (getToString() == null) {
            result = new PrintableAdapters().determineAdapterForType(type());
        } else {
            result = new PrintableAdapters().determineAdapterForType("String");
        }
        return result;
    }

    @Override
    public String genericType() {
        final String result;
        if (isArray()) {
            result = jsonTypeToJavaTypeMapping.resolveFor(jsonForGenericType()).resolveType();
        } else {
            result = null;
        }
        return result;
    }

    public EnumProperty enumValue() {
        final EnumProperty result;
        if (Objects.equals("enum", jsonType())) {
            result = new EnumProperty(json.getValue().asJsonObject());
        } else {
            result = null;
        }
        return result;
    }

    public ParameterAlternatives alternatives() {
        final ParameterAlternatives result;
        if (Objects.equals(jsonType(), "oneOf")) {
            result =
                oneOfAlternative(
                    json.getValue().asJsonObject(),
                    new FirstCharToUpperCase(json.getValue().asJsonObject().getString("title")).toCase()
                );
        } else if (isArray() && Objects.equals(new JsonTypeResolver(jsonForGenericType()).resolveType(), "oneOf")) {
            result = oneOfAlternative(jsonForGenericType(), genericType());
        } else {
            result = null;
        }
        return result;
    }

    private boolean isArray() {
        return Objects.equals("array", jsonType());
    }

    private ParameterAlternatives oneOfAlternative(final JsonObject typeInfo, final String name) {
        return new ParameterAlternatives(
            new OneOfAlternativeTypeable(name),
            typeInfo.getJsonArray("oneOf"),
            typeRegistry,
            jsonTypeToJavaTypeMapping
        );
    }

    private JsonObject jsonForGenericType() {
        return json.getValue().asJsonObject().getJsonObject("items");
    }

    @Override
    public JsonObjectClassDefinition owner() {
        return owner;
    }

    @Override
    public String type() {
        return jsonTypeToJavaTypeMapping.resolveFor(json.getValue().asJsonObject()).resolveType();
    }

    @Override
    public boolean isNullable() {
        return jsonTypeToJavaTypeMapping.resolveFor(json.getValue().asJsonObject()).isNullable();
    }

    private String jsonType() {
        return new JsonTypeResolver(json.getValue().asJsonObject()).resolveType();
    }

    @Override
    public String name() {
        return json.getKey();
    }

    class OneOfAlternativeTypeable implements Typeable {

        private final String name;

        public OneOfAlternativeTypeable(final String name) {
            this.name = name;
        }

        @Override
        public JsonObjectClassDefinition owner() {
            return Property.this.owner;
        }

        @Override
        public String name() {
            return name;
        }

        @Override
        public String type() {
            return Property.this.type();
        }

        @Override
        public String genericType() {
            return Property.this.genericType();
        }

        @Override
        public String adapter() {
            return Property.this.adapter();
        }

        @Override
        public boolean isNullable() {
            return true;
        }
    }
}
