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
package io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.generator;

import static java.util.stream.Collectors.toSet;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.model.JsonObjectClassDefinition;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public final class MustacheJavaClassTemplate implements JavaClassTemplate {

    private final Template template;
    private final TemplateOutput output;

    public MustacheJavaClassTemplate(final Template template, final TemplateOutput output) {
        this.template = template;
        this.output = output;
    }

    @Override
    public void generate(final JsonObjectClassDefinition context) {
        try (final Writer writer = output.createWriterFor(context.packagename(), context.objectname())) {
            template.execute(new ClassDefinitionWithLambdas(context), writer);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static class ClassDefinitionWithLambdas extends AbstractMap<String, Object> {

        private final JsonObjectClassDefinition delegate;
        private Set<Entry<String, Object>> entries;

        ClassDefinitionWithLambdas(final JsonObjectClassDefinition delegate) {
            this.delegate = delegate;
        }

        @Override
        public Set<Entry<String, Object>> entrySet() {
            if (entries == null) {
                entries = Stream.concat(methods(), lamdas()).collect(toSet());
            }
            return entries;
        }

        private Stream<Entry<String, Object>> methods() {
            final Set<Entry<String, Object>> result = new HashSet<>();
            final Collection<Method> methods = methodsOf(delegate.getClass());
            for (Method m : methods) {
                if (m.getDeclaringClass() != Object.class && m.getParameters().length == 0) {
                    try {
                        result.add(Map.entry(m.getName(), m.invoke(delegate)));
                    } catch (ReflectiveOperationException e) {
                        //ignore ...
                    }
                }
            }
            return result.stream();
        }

        private List<Method> methodsOf(final Class<?> cls) {
            final List<Method> result = new ArrayList<>();
            if (Modifier.isPublic(cls.getModifiers())) {
                result.addAll(
                    Arrays.stream(cls.getMethods()).filter(m -> Modifier.isPublic(m.getModifiers())).toList()
                );
            } else {
                if (cls.getSuperclass() != Object.class) {
                    result.addAll(methodsOf(cls.getSuperclass()));
                }
                result.addAll(
                    Arrays.stream(cls.getInterfaces()).map(this::methodsOf).flatMap(Collection::stream).toList()
                );
            }
            return result;
        }

        private Stream<Map.Entry<String, Object>> lamdas() {
            return Stream.of(
                Map.entry(
                    "cap1stChar",
                    (Mustache.Lambda) (frag, out) -> {
                        final String key = frag.execute();
                        final char[] chars = key.toCharArray();
                        if (chars.length > 0) {
                            chars[0] = Character.toUpperCase(chars[0]);
                        }
                        out.write(chars);
                    }
                )
            );
        }
    }
}
