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

class DefaultJsonObjectClassWriterTest {

    @Test
    void should_generate_javaclass_with_usr_to_string() throws Exception {
        assertThat(
            generateOpenRPCSpecClassWithName("ExternalDocumentationObject"),
            is(
                """
                package io.github;

                import io.github.sebastiantoepfer.ddd.common.Media;
                import io.github.sebastiantoepfer.ddd.common.Printable;
                import io.github.sebastiantoepfer.ddd.printables.core.CompositePrintable;
                import io.github.sebastiantoepfer.ddd.printables.core.NamedStringPrintable;
                import java.net.URL;
                import java.util.Objects;
                import javax.annotation.processing.Generated;

                @Generated("jsongen")
                public final class ExternalDocumentationObject implements Printable {

                    private final CompositePrintable values;

                    public ExternalDocumentationObject(final URL url) {
                        this(
                            new CompositePrintable()
                                .withPrintable(new NamedStringPrintable("url", Objects.requireNonNull(url).toExternalForm()))
                        );
                    }

                    private ExternalDocumentationObject(final CompositePrintable values) {
                        this.values = Objects.requireNonNull(values);
                    }

                    public ExternalDocumentationObject withDescription(final String description) {
                        return new ExternalDocumentationObject(values.withPrintable(new NamedStringPrintable("description", description)));
                    }

                    @Override
                    public final <T extends Media<T>> T printOn(final T media) {
                        return values.printOn(media);
                    }

                }
                """
            )
        );
    }

    @Test
    void should_generate_javaclass_with_required_emum() throws Exception {
        assertThat(
            generateOpenRPCSpecClassWithName("OpenrpcDocument"),
            is(
                """
                package io.github;

                import io.github.sebastiantoepfer.ddd.common.Media;
                import io.github.sebastiantoepfer.ddd.common.Printable;
                import io.github.sebastiantoepfer.ddd.printables.core.CompositePrintable;
                import io.github.sebastiantoepfer.ddd.printables.core.NamedListPrintable;
                import io.github.sebastiantoepfer.ddd.printables.core.NamedPrintable;
                import io.github.sebastiantoepfer.ddd.printables.core.NamedStringPrintable;
                import java.util.List;
                import java.util.Objects;
                import javax.annotation.processing.Generated;

                @Generated("jsongen")
                public final class OpenrpcDocument implements Printable {

                    private final CompositePrintable values;

                    public OpenrpcDocument(final Openrpc openrpc, final InfoObject info, final List<MethodOrReference> methods) {
                        this(
                            new CompositePrintable()
                                .withPrintable(new NamedStringPrintable("openrpc", Objects.requireNonNull(openrpc).toString()))
                                .withPrintable(new NamedPrintable("info", Objects.requireNonNull(info)))
                                .withPrintable(new NamedListPrintable("methods", Objects.requireNonNull(methods)))
                        );
                    }

                    private OpenrpcDocument(final CompositePrintable values) {
                        this.values = Objects.requireNonNull(values);
                    }

                    public OpenrpcDocument withExternalDocs(final ExternalDocumentationObject externalDocs) {
                        return new OpenrpcDocument(values.withPrintable(new NamedPrintable("externalDocs", externalDocs)));
                    }

                    public OpenrpcDocument withServers(final List<ServerObject> servers) {
                        return new OpenrpcDocument(values.withPrintable(new NamedListPrintable("servers", servers)));
                    }

                    public OpenrpcDocument withComponents(final Printable components) {
                        return new OpenrpcDocument(values.withPrintable(new NamedPrintable("components", components)));
                    }

                    @Override
                    public final <T extends Media<T>> T printOn(final T media) {
                        return values.printOn(media);
                    }

                    public enum Openrpc {
                        _130("1.3.0"),
                        _126("1.2.6"),
                        _125("1.2.5"),
                        _124("1.2.4"),
                        _123("1.2.3"),
                        _122("1.2.2"),
                        _121("1.2.1"),
                        _120("1.2.0"),
                        _1112("1.1.12"),
                        _1111("1.1.11"),
                        _1110("1.1.10"),
                        _119("1.1.9"),
                        _118("1.1.8"),
                        _117("1.1.7"),
                        _116("1.1.6"),
                        _115("1.1.5"),
                        _114("1.1.4"),
                        _113("1.1.3"),
                        _112("1.1.2"),
                        _111("1.1.1"),
                        _110("1.1.0"),
                        _100("1.0.0"),
                        _100rc1("1.0.0-rc1"),
                        _100rc0("1.0.0-rc0");

                        private final String value;

                        private Openrpc(final String value) {
                            this.value = value;
                        }

                        @Override
                        public String toString() {
                            return value;
                        }

                    }
                }
                """
            )
        );
    }

    @Test
    void should_generate_javaclass_without_required() throws Exception {
        assertThat(
            generateOpenRPCSpecClassWithName("ContactObject"),
            is(
                """
                package io.github;

                import io.github.sebastiantoepfer.ddd.common.Media;
                import io.github.sebastiantoepfer.ddd.common.Printable;
                import io.github.sebastiantoepfer.ddd.printables.core.CompositePrintable;
                import io.github.sebastiantoepfer.ddd.printables.core.NamedStringPrintable;
                import java.util.Objects;
                import javax.annotation.processing.Generated;

                @Generated("jsongen")
                public final class ContactObject implements Printable {

                    private final CompositePrintable values;

                    public ContactObject() {
                        this(
                            new CompositePrintable()
                        );
                    }

                    private ContactObject(final CompositePrintable values) {
                        this.values = Objects.requireNonNull(values);
                    }

                    public ContactObject withName(final String name) {
                        return new ContactObject(values.withPrintable(new NamedStringPrintable("name", name)));
                    }

                    public ContactObject withEmail(final String email) {
                        return new ContactObject(values.withPrintable(new NamedStringPrintable("email", email)));
                    }

                    public ContactObject withUrl(final String url) {
                        return new ContactObject(values.withPrintable(new NamedStringPrintable("url", url)));
                    }

                    @Override
                    public final <T extends Media<T>> T printOn(final T media) {
                        return values.printOn(media);
                    }

                }
                """
            )
        );
    }

    @Test
    void should_generate_javaclass_with_required_primitv() throws Exception {
        assertThat(
            generateOpenRPCSpecClassWithName("ErrorObject"),
            is(
                """
                package io.github;

                import io.github.sebastiantoepfer.ddd.common.Media;
                import io.github.sebastiantoepfer.ddd.common.Printable;
                import io.github.sebastiantoepfer.ddd.printables.core.CompositePrintable;
                import io.github.sebastiantoepfer.ddd.printables.core.NamedNumberPrintable;
                import io.github.sebastiantoepfer.ddd.printables.core.NamedPrintable;
                import io.github.sebastiantoepfer.ddd.printables.core.NamedStringPrintable;
                import java.util.Objects;
                import javax.annotation.processing.Generated;

                @Generated("jsongen")
                public final class ErrorObject implements Printable {

                    private final CompositePrintable values;

                    public ErrorObject(final long code, final String message) {
                        this(
                            new CompositePrintable()
                                .withPrintable(new NamedNumberPrintable("code", code))
                                .withPrintable(new NamedStringPrintable("message", Objects.requireNonNull(message)))
                        );
                    }

                    private ErrorObject(final CompositePrintable values) {
                        this.values = Objects.requireNonNull(values);
                    }

                    public ErrorObject withData(final Printable data) {
                        return new ErrorObject(values.withPrintable(new NamedPrintable("data", data)));
                    }

                    @Override
                    public final <T extends Media<T>> T printOn(final T media) {
                        return values.printOn(media);
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
