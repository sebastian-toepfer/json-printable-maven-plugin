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

import static java.util.stream.Collectors.toSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CompositeTypeRegistry implements TypeRegistry {

    private final List<TypeRegistry> registries;

    public CompositeTypeRegistry() {
        this(List.of());
    }

    public CompositeTypeRegistry(final Collection<TypeRegistry> registries) {
        this.registries = List.copyOf(registries);
    }

    public CompositeTypeRegistry withRegistry(final TypeRegistry registry) {
        final List<TypeRegistry> newRegistries = new ArrayList<>(registries);
        newRegistries.add(registry);
        return new CompositeTypeRegistry(newRegistries);
    }

    @Override
    public Collection<String> determineFullQualifiedNameOf(final Typeable property) {
        return registries
            .stream()
            .map(registry -> registry.determineFullQualifiedNameOf(property))
            .flatMap(Collection::stream)
            .collect(toSet());
    }
}
