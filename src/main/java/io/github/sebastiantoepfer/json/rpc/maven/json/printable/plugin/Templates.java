/*
 * The MIT License
 *
 * Copyright 2024 sebastian.
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
package io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin;

import com.samskivert.mustache.Mustache;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.generator.JavaClassTemplate;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.generator.MustacheJavaClassTemplate;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.generator.TemplateOutput;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Logger;

final class Templates {

    private static final Logger LOG = Logger.getLogger(Templates.class.getName());
    private final TemplateOutput output;

    public Templates(final TemplateOutput output) {
        this.output = Objects.requireNonNull(output);
    }

    public JavaClassTemplate createTemplate(final String templateResource) {
        LOG.entering(Templates.class.getName(), "createTemplate", templateResource);
        final MustacheJavaClassTemplate result = new MustacheJavaClassTemplate(
            Mustache
                .compiler()
                .compile(
                    new InputStreamReader(
                        GenerateMojo.class.getClassLoader()
                            .getResourceAsStream(String.format("templates/%s", templateResource)),
                        StandardCharsets.UTF_8
                    )
                ),
            output
        );
        LOG.exiting(Templates.class.getName(), "createTemplate", result);
        return result;
    }
}
