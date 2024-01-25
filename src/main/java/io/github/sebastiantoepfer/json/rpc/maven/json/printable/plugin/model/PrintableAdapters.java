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
import io.github.sebastiantoepfer.ddd.printables.core.NamedBooleanPrintable;
import io.github.sebastiantoepfer.ddd.printables.core.NamedListPrintable;
import io.github.sebastiantoepfer.ddd.printables.core.NamedNumberPrintable;
import io.github.sebastiantoepfer.ddd.printables.core.NamedPrintable;
import io.github.sebastiantoepfer.ddd.printables.core.NamedStringPrintable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

public final class PrintableAdapters implements TypeRegistry {

    private final Class<? extends Printable> defaultAdapter;
    private final Map<String, Class<? extends Printable>> typeToAdapter;

    public PrintableAdapters() {
        this.defaultAdapter = NamedPrintable.class;
        this.typeToAdapter =
            Map.of(
                "String",
                NamedStringPrintable.class,
                "URL",
                NamedStringPrintable.class,
                "List",
                NamedListPrintable.class,
                "boolean",
                NamedBooleanPrintable.class,
                "long",
                NamedNumberPrintable.class
            );
    }

    @Override
    public Collection<String> fullQulifiedNameOf(final Typeable property) {
        final Collection<String> result;
        if (property.adapter() == null) {
            result = Collections.emptyList();
        } else {
            result = Collections.singleton(fullQulifiedNameForAdapter(property.adapter()));
        }
        return result;
    }

    private String fullQulifiedNameForAdapter(final String adapter) {
        return Stream
            .concat(Stream.of(defaultAdapter), typeToAdapter.values().stream())
            .filter(cls -> cls.getSimpleName().equals(adapter))
            .map(Class::getCanonicalName)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(String.format("invalid adapter: %s!", adapter)));
    }

    public String determineAdapterForType(final String type) {
        return typeToAdapter.getOrDefault(type, defaultAdapter).getSimpleName();
    }
}
