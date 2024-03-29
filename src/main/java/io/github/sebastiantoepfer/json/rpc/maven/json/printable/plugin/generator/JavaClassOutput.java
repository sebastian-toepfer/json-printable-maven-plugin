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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class JavaClassOutput implements TemplateOutput {

    private static final Logger LOG = Logger.getLogger(JavaClassOutput.class.getName());

    private final Path srcDirectory;

    public JavaClassOutput(final Path srcDirectory) {
        this.srcDirectory = Objects.requireNonNull(srcDirectory);
    }

    @Override
    public Writer createWriterFor(final String packageName, final String className) throws IOException {
        final Path packageDirectory = srcDirectory.resolve(packageName.replace('.', '/'));
        if (packageDirectory.toFile().mkdirs()) {
            LOG.log(Level.FINE, "package directories created!");
        }
        return new OutputStreamWriter(
            new FileOutputStream(packageDirectory.resolve(String.format("%s.java", className)).toFile(), false),
            StandardCharsets.UTF_8
        );
    }
}
