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

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

final class JsonSchemaProvider {

    private static final Logger LOG = Logger.getLogger(JsonSchemaProvider.class.getName());
    private final URL schemaUrl;

    public JsonSchemaProvider() {
        this(null);
    }

    public JsonSchemaProvider(final URL schemaUrl) {
        this.schemaUrl = schemaUrl;
    }

    public JsonObject loadSchema() throws IOException {
        LOG.entering(JsonSchemaProvider.class.getName(), "loadSchema");
        final JsonObject schema;
        try (JsonReader reader = Json.createReader(schemaAsStream())) {
            schema = reader.readObject();
        }
        LOG.exiting(JsonSchemaProvider.class.getName(), "loadSchema", schema);
        return schema;
    }

    private InputStream schemaAsStream() throws IOException {
        LOG.entering(JsonSchemaProvider.class.getName(), "schemaAsStream");
        final InputStream result;
        if (schemaUrl == null) {
            LOG.log(Level.FINE, "use bundled schema.");
            result = GenerateMojo.class.getClassLoader().getResource("open-rpc-meta-schema.json").openStream();
        } else {
            LOG.log(Level.FINE, () -> String.format("use url %s to retrieve schema.", schemaUrl));
            result = schemaUrl.openStream();
        }
        LOG.exiting(JsonSchemaProvider.class.getName(), "schemaAsStream");
        return result;
    }
}
