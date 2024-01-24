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
import jakarta.json.JsonString;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public final class EnumProperty {

    private final JsonObject value;

    EnumProperty(final JsonObject value) {
        if (!value.containsKey("enum") || !value.containsKey("title")) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    public String name() {
        return new FirstCharToUpperCase(value.getString("title")).toCase();
    }

    public List<EnumPropertyValues> values() {
        return value.getJsonArray("enum").stream().map(JsonString.class::cast).map(EnumPropertyValues::new).toList();
    }

    public static class EnumPropertyValues {

        private static final Pattern INVALID_ENUM_NAME = Pattern.compile("^[^\\p{L}]");
        private final JsonString value;

        private EnumPropertyValues(final JsonString value) {
            this.value = Objects.requireNonNull(value);
        }

        public String enumName() {
            final String result;
            if (INVALID_ENUM_NAME.matcher(enumValue()).find()) {
                result = String.format("_%s", enumValue());
            } else {
                result = enumValue();
            }
            return result.replace(".", "").replace("-", "");
        }

        public String enumValue() {
            return value.getString();
        }
    }
}
