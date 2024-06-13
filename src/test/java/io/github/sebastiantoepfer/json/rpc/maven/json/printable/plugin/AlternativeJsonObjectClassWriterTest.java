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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.model.JsonTypeToJavaTypeMapping;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;

class AlternativeJsonObjectClassWriterTest {

    @Test
    void should_create_simple_alternative_class() throws Exception {
        assertThat(
            generateOpenRPCSpecClassWithName("JsonSchemaOrReference"),
            is(
                """
                package io.github;

                import io.github.sebastiantoepfer.ddd.common.Media;
                import io.github.sebastiantoepfer.ddd.common.Printable;
                import io.github.sebastiantoepfer.jsonschema.JsonSchema;
                import io.github.sebastiantoepfer.jsonschema.Validator;
                import jakarta.json.JsonArray;
                import jakarta.json.JsonObject;
                import jakarta.json.JsonPointer;
                import jakarta.json.JsonValue.ValueType;
                import java.util.Objects;
                import java.util.Optional;
                import java.util.stream.Stream;
                import javax.annotation.processing.Generated;

                @Generated("jsongen")
                public abstract class JsonSchemaOrReference implements Printable {

                    private JsonSchemaOrReference() {}

                    public static final class Object extends JsonSchemaOrReference implements JsonSchema {

                        private final JsonSchema object;

                        public Object(final JsonSchema object) {
                           this.object = Objects.requireNonNull(object);
                        }

                        @Override
                        public <T extends Media<T>> T printOn(final T media) {
                            return object.printOn(media);
                        }

                        @Override
                        public JsonArray asJsonArray() {
                            return value().asJsonArray();
                        }

                        @Override
                        public JsonObject asJsonObject() {
                            return value().asJsonObject();
                        }

                        @Override
                        public ValueType getValueType() {
                            return value().getValueType();
                        }

                        @Override
                        public Optional keywordByName(final String arg0) {
                            return value().keywordByName(arg0);
                        }

                        @Override
                        public JsonSchema rootSchema() {
                            return value().rootSchema();
                        }

                        @Override
                        public Optional subSchema(final JsonPointer arg0) {
                            return value().subSchema(arg0);
                        }

                        @Override
                        public Optional subSchema(final String arg0) {
                            return value().subSchema(arg0);
                        }

                        @Override
                        public Stream subSchemas(final String arg0) {
                            return value().subSchemas(arg0);
                        }

                        @Override
                        public Validator validator() {
                            return value().validator();
                        }

                        private JsonSchema value() {
                            return object;
                        }

                    }

                    public static final class Reference extends JsonSchemaOrReference {

                        private final ReferenceObject reference;

                        public Reference(final ReferenceObject reference) {
                           this.reference = Objects.requireNonNull(reference);
                        }

                        @Override
                        public <T extends Media<T>> T printOn(final T media) {
                            return reference.printOn(media);
                        }

                    }

                }
                """
            )
        );
    }

    private String generateOpenRPCSpecClassWithName(final String clsName) throws IOException {
        final StringWriter writer = new StringWriter();
        new CodeGenerator(
            new ModelCreator(new JsonSchemaProvider(), new JsonTypeToJavaTypeMapping()),
            "io.github",
            new Templates(new FilteredClassOutput(clsName, writer))
        )
            .generateModel();
        return writer.toString();
    }
}
