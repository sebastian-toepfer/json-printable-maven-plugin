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
                import io.github.sebastiantoepfer.ddd.printables.core.NamedPrintable;
                import io.github.sebastiantoepfer.ddd.printables.core.NamedStringPrintable;
                import java.net.URL;
                import java.util.HashMap;
                import java.util.Map;
                import java.util.Objects;
                import java.util.regex.Pattern;
                import javax.annotation.processing.Generated;

                @Generated("jsongen")
                public final class ExternalDocumentationObject implements Printable {

                    private static final Pattern PROPERTY_NAME_PATTERN = Pattern.compile("^x-");
                    private final Map<String, Printable> additionalValues;
                    private final CompositePrintable values;

                    public ExternalDocumentationObject(final URL url) {
                        this(
                            new CompositePrintable()
                                .withPrintable(new NamedStringPrintable("url", Objects.requireNonNull(url).toExternalForm()))
                            , Map.of()
                        );
                    }

                    private ExternalDocumentationObject(
                        final CompositePrintable values,
                        final Map<String, Printable> additionalValues
                    ) {
                        this.values = Objects.requireNonNull(values);
                        this.additionalValues = Map.copyOf(additionalValues);
                    }

                    public ExternalDocumentationObject withValue(final String name, final Printable value) {
                        if (PROPERTY_NAME_PATTERN.asPredicate().negate().test(name)) {
                            throw new IllegalArgumentException("provided name is not valid!");
                        }
                        final var newAdditionalValues = new HashMap(additionalValues);
                        newAdditionalValues.put(name, value);
                        return new ExternalDocumentationObject(values, newAdditionalValues);
                    }

                    public ExternalDocumentationObject withDescription(final String description) {
                        return new ExternalDocumentationObject(values.withPrintable(new NamedStringPrintable("description", description)), this.additionalValues);
                    }

                    @Override
                    public final <T extends Media<T>> T printOn(final T media) {
                        return additionalValues.entrySet()
                                .stream()
                                .map(e -> new NamedPrintable(e.getKey(), e.getValue()))
                                .reduce(values, CompositePrintable::withPrintable, (l,r) -> null)
                                .printOn(media);
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
                import java.util.HashMap;
                import java.util.List;
                import java.util.Map;
                import java.util.Objects;
                import java.util.regex.Pattern;
                import javax.annotation.processing.Generated;

                @Generated("jsongen")
                public final class OpenrpcDocument implements Printable {

                    private static final Pattern PROPERTY_NAME_PATTERN = Pattern.compile("^x-");
                    private final Map<String, Printable> additionalValues;
                    private final CompositePrintable values;

                    public OpenrpcDocument(final Openrpc openrpc, final InfoObject info, final List<MethodOrReference> methods) {
                        this(
                            new CompositePrintable()
                                .withPrintable(new NamedStringPrintable("openrpc", Objects.requireNonNull(openrpc).toString()))
                                .withPrintable(new NamedPrintable("info", Objects.requireNonNull(info)))
                                .withPrintable(new NamedListPrintable("methods", Objects.requireNonNull(methods)))
                            , Map.of()
                        );
                    }

                    private OpenrpcDocument(
                        final CompositePrintable values,
                        final Map<String, Printable> additionalValues
                    ) {
                        this.values = Objects.requireNonNull(values);
                        this.additionalValues = Map.copyOf(additionalValues);
                    }

                    public OpenrpcDocument withValue(final String name, final Printable value) {
                        if (PROPERTY_NAME_PATTERN.asPredicate().negate().test(name)) {
                            throw new IllegalArgumentException("provided name is not valid!");
                        }
                        final var newAdditionalValues = new HashMap(additionalValues);
                        newAdditionalValues.put(name, value);
                        return new OpenrpcDocument(values, newAdditionalValues);
                    }

                    public OpenrpcDocument withExternalDocs(final ExternalDocumentationObject externalDocs) {
                        return new OpenrpcDocument(values.withPrintable(new NamedPrintable("externalDocs", externalDocs)), this.additionalValues);
                    }

                    public OpenrpcDocument withServers(final List<ServerObject> servers) {
                        return new OpenrpcDocument(values.withPrintable(new NamedListPrintable("servers", servers)), this.additionalValues);
                    }

                    public OpenrpcDocument withComponents(final Components components) {
                        return new OpenrpcDocument(values.withPrintable(new NamedPrintable("components", components)), this.additionalValues);
                    }

                    @Override
                    public final <T extends Media<T>> T printOn(final T media) {
                        return additionalValues.entrySet()
                                .stream()
                                .map(e -> new NamedPrintable(e.getKey(), e.getValue()))
                                .reduce(values, CompositePrintable::withPrintable, (l,r) -> null)
                                .printOn(media);
                    }

                    public enum Openrpc {
                        Openrpc_132("1.3.2"),
                        Openrpc_131("1.3.1"),
                        Openrpc_130("1.3.0"),
                        Openrpc_126("1.2.6"),
                        Openrpc_125("1.2.5"),
                        Openrpc_124("1.2.4"),
                        Openrpc_123("1.2.3"),
                        Openrpc_122("1.2.2"),
                        Openrpc_121("1.2.1"),
                        Openrpc_120("1.2.0"),
                        Openrpc_1112("1.1.12"),
                        Openrpc_1111("1.1.11"),
                        Openrpc_1110("1.1.10"),
                        Openrpc_119("1.1.9"),
                        Openrpc_118("1.1.8"),
                        Openrpc_117("1.1.7"),
                        Openrpc_116("1.1.6"),
                        Openrpc_115("1.1.5"),
                        Openrpc_114("1.1.4"),
                        Openrpc_113("1.1.3"),
                        Openrpc_112("1.1.2"),
                        Openrpc_111("1.1.1"),
                        Openrpc_110("1.1.0"),
                        Openrpc_100("1.0.0"),
                        Openrpc_100rc1("1.0.0-rc1"),
                        Openrpc_100rc0("1.0.0-rc0");

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
                import io.github.sebastiantoepfer.ddd.printables.core.NamedPrintable;
                import io.github.sebastiantoepfer.ddd.printables.core.NamedStringPrintable;
                import java.util.HashMap;
                import java.util.Map;
                import java.util.Objects;
                import java.util.regex.Pattern;
                import javax.annotation.processing.Generated;

                @Generated("jsongen")
                public final class ContactObject implements Printable {

                    private static final Pattern PROPERTY_NAME_PATTERN = Pattern.compile("^x-");
                    private final Map<String, Printable> additionalValues;
                    private final CompositePrintable values;

                    public ContactObject() {
                        this(
                            new CompositePrintable()
                            , Map.of()
                        );
                    }

                    private ContactObject(
                        final CompositePrintable values,
                        final Map<String, Printable> additionalValues
                    ) {
                        this.values = Objects.requireNonNull(values);
                        this.additionalValues = Map.copyOf(additionalValues);
                    }

                    public ContactObject withValue(final String name, final Printable value) {
                        if (PROPERTY_NAME_PATTERN.asPredicate().negate().test(name)) {
                            throw new IllegalArgumentException("provided name is not valid!");
                        }
                        final var newAdditionalValues = new HashMap(additionalValues);
                        newAdditionalValues.put(name, value);
                        return new ContactObject(values, newAdditionalValues);
                    }

                    public ContactObject withName(final String name) {
                        return new ContactObject(values.withPrintable(new NamedStringPrintable("name", name)), this.additionalValues);
                    }

                    public ContactObject withEmail(final String email) {
                        return new ContactObject(values.withPrintable(new NamedStringPrintable("email", email)), this.additionalValues);
                    }

                    public ContactObject withUrl(final String url) {
                        return new ContactObject(values.withPrintable(new NamedStringPrintable("url", url)), this.additionalValues);
                    }

                    @Override
                    public final <T extends Media<T>> T printOn(final T media) {
                        return additionalValues.entrySet()
                                .stream()
                                .map(e -> new NamedPrintable(e.getKey(), e.getValue()))
                                .reduce(values, CompositePrintable::withPrintable, (l,r) -> null)
                                .printOn(media);
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

    @Test
    void should_generate_javaclass_from_property() throws Exception {
        assertThat(
            generateOpenRPCSpecClassWithName("Components"),
            is(
                """
                package io.github;

                import io.github.sebastiantoepfer.ddd.common.Media;
                import io.github.sebastiantoepfer.ddd.common.Printable;
                import io.github.sebastiantoepfer.ddd.printables.core.CompositePrintable;
                import io.github.sebastiantoepfer.ddd.printables.core.NamedPrintable;
                import java.util.Objects;
                import javax.annotation.processing.Generated;

                @Generated("jsongen")
                public final class Components implements Printable {

                    private final CompositePrintable values;

                    public Components() {
                        this(
                            new CompositePrintable()
                        );
                    }

                    private Components(final CompositePrintable values) {
                        this.values = Objects.requireNonNull(values);
                    }

                    public Components withSchemas(final SchemaComponents schemas) {
                        return new Components(values.withPrintable(new NamedPrintable("schemas", schemas)));
                    }

                    public Components withLinks(final LinkComponents links) {
                        return new Components(values.withPrintable(new NamedPrintable("links", links)));
                    }

                    public Components withErrors(final ErrorComponents errors) {
                        return new Components(values.withPrintable(new NamedPrintable("errors", errors)));
                    }

                    public Components withExamples(final ExampleComponents examples) {
                        return new Components(values.withPrintable(new NamedPrintable("examples", examples)));
                    }

                    public Components withExamplePairings(final ExamplePairingComponents examplePairings) {
                        return new Components(values.withPrintable(new NamedPrintable("examplePairings", examplePairings)));
                    }

                    public Components withContentDescriptors(final ContentDescriptorComponents contentDescriptors) {
                        return new Components(values.withPrintable(new NamedPrintable("contentDescriptors", contentDescriptors)));
                    }

                    public Components withTags(final TagComponents tags) {
                        return new Components(values.withPrintable(new NamedPrintable("tags", tags)));
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
    void should_generate_javaclass_from_property_inside_property() throws Exception {
        assertThat(
            generateOpenRPCSpecClassWithName("SchemaComponents"),
            is(
                """
                package io.github;

                import io.github.sebastiantoepfer.ddd.common.Media;
                import io.github.sebastiantoepfer.ddd.common.Printable;
                import io.github.sebastiantoepfer.ddd.printables.core.CompositePrintable;
                import io.github.sebastiantoepfer.ddd.printables.core.NamedPrintable;
                import java.util.HashMap;
                import java.util.Map;
                import java.util.Objects;
                import java.util.regex.Pattern;
                import javax.annotation.processing.Generated;

                @Generated("jsongen")
                public final class SchemaComponents implements Printable {

                    private static final Pattern PROPERTY_NAME_PATTERN = Pattern.compile("[0-z]+");
                    private final Map<String, JsonSchemaOrReference> additionalValues;
                    private final CompositePrintable values;

                    public SchemaComponents() {
                        this(
                            new CompositePrintable()
                            , Map.of()
                        );
                    }

                    private SchemaComponents(
                        final CompositePrintable values,
                        final Map<String, JsonSchemaOrReference> additionalValues
                    ) {
                        this.values = Objects.requireNonNull(values);
                        this.additionalValues = Map.copyOf(additionalValues);
                    }

                    public SchemaComponents withValue(final String name, final JsonSchemaOrReference value) {
                        if (PROPERTY_NAME_PATTERN.asPredicate().negate().test(name)) {
                            throw new IllegalArgumentException("provided name is not valid!");
                        }
                        final var newAdditionalValues = new HashMap(additionalValues);
                        newAdditionalValues.put(name, value);
                        return new SchemaComponents(values, newAdditionalValues);
                    }

                    @Override
                    public final <T extends Media<T>> T printOn(final T media) {
                        return additionalValues.entrySet()
                                .stream()
                                .map(e -> new NamedPrintable(e.getKey(), e.getValue()))
                                .reduce(values, CompositePrintable::withPrintable, (l,r) -> null)
                                .printOn(media);
                    }

                }
                """
            )
        );
    }

    @Test
    void should_generate_javaclass_with_or_parameters() throws Exception {
        assertThat(
            generateOpenRPCSpecClassWithName("MethodObject"),
            is(
                """
                package io.github;

                import io.github.sebastiantoepfer.ddd.common.Media;
                import io.github.sebastiantoepfer.ddd.common.Printable;
                import io.github.sebastiantoepfer.ddd.printables.core.CompositePrintable;
                import io.github.sebastiantoepfer.ddd.printables.core.NamedBooleanPrintable;
                import io.github.sebastiantoepfer.ddd.printables.core.NamedListPrintable;
                import io.github.sebastiantoepfer.ddd.printables.core.NamedPrintable;
                import io.github.sebastiantoepfer.ddd.printables.core.NamedStringPrintable;
                import java.util.HashMap;
                import java.util.List;
                import java.util.Map;
                import java.util.Objects;
                import java.util.regex.Pattern;
                import javax.annotation.processing.Generated;

                @Generated("jsongen")
                public final class MethodObject implements Printable {

                    private static final Pattern PROPERTY_NAME_PATTERN = Pattern.compile("^x-");
                    private final Map<String, Printable> additionalValues;
                    private final CompositePrintable values;

                    public MethodObject(final String name, final List<ContentDescriptorOrReference> params) {
                        this(
                            new CompositePrintable()
                                .withPrintable(new NamedStringPrintable("name", Objects.requireNonNull(name)))
                                .withPrintable(new NamedListPrintable("params", Objects.requireNonNull(params)))
                            , Map.of()
                        );
                    }

                    private MethodObject(
                        final CompositePrintable values,
                        final Map<String, Printable> additionalValues
                    ) {
                        this.values = Objects.requireNonNull(values);
                        this.additionalValues = Map.copyOf(additionalValues);
                    }

                    public MethodObject withValue(final String name, final Printable value) {
                        if (PROPERTY_NAME_PATTERN.asPredicate().negate().test(name)) {
                            throw new IllegalArgumentException("provided name is not valid!");
                        }
                        final var newAdditionalValues = new HashMap(additionalValues);
                        newAdditionalValues.put(name, value);
                        return new MethodObject(values, newAdditionalValues);
                    }

                    public MethodObject withDescription(final String description) {
                        return new MethodObject(values.withPrintable(new NamedStringPrintable("description", description)), this.additionalValues);
                    }

                    public MethodObject withSummary(final String summary) {
                        return new MethodObject(values.withPrintable(new NamedStringPrintable("summary", summary)), this.additionalValues);
                    }

                    public MethodObject withServers(final List<ServerObject> servers) {
                        return new MethodObject(values.withPrintable(new NamedListPrintable("servers", servers)), this.additionalValues);
                    }

                    public MethodObject withTags(final List<TagOrReference> tags) {
                        return new MethodObject(values.withPrintable(new NamedListPrintable("tags", tags)), this.additionalValues);
                    }

                    public MethodObject withParamStructure(final MethodObjectParamStructure paramStructure) {
                        return new MethodObject(values.withPrintable(new NamedStringPrintable("paramStructure", paramStructure.toString())), this.additionalValues);
                    }

                    public MethodObject withResult(final ContentDescriptorOrReference result) {
                        return new MethodObject(values.withPrintable(new NamedPrintable("result", result)), this.additionalValues);
                    }

                    public MethodObject withErrors(final List<ErrorOrReference> errors) {
                        return new MethodObject(values.withPrintable(new NamedListPrintable("errors", errors)), this.additionalValues);
                    }

                    public MethodObject withLinks(final List<LinkOrReference> links) {
                        return new MethodObject(values.withPrintable(new NamedListPrintable("links", links)), this.additionalValues);
                    }

                    public MethodObject withExamples(final List<ExamplePairingOrReference> examples) {
                        return new MethodObject(values.withPrintable(new NamedListPrintable("examples", examples)), this.additionalValues);
                    }

                    public MethodObject withDeprecated(final boolean deprecated) {
                        return new MethodObject(values.withPrintable(new NamedBooleanPrintable("deprecated", deprecated)), this.additionalValues);
                    }

                    public MethodObject withExternalDocs(final ExternalDocumentationObject externalDocs) {
                        return new MethodObject(values.withPrintable(new NamedPrintable("externalDocs", externalDocs)), this.additionalValues);
                    }

                    @Override
                    public final <T extends Media<T>> T printOn(final T media) {
                        return additionalValues.entrySet()
                                .stream()
                                .map(e -> new NamedPrintable(e.getKey(), e.getValue()))
                                .reduce(values, CompositePrintable::withPrintable, (l,r) -> null)
                                .printOn(media);
                    }

                    public enum MethodObjectParamStructure {
                        byposition("by-position"),
                        byname("by-name"),
                        either("either");

                        private final String value;

                        private MethodObjectParamStructure(final String value) {
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
    void should_generate_class_referenced_by_pattern_properties() throws Exception {
        assertThat(
            generateOpenRPCSpecClassWithName("ServerObjectVariable"),
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
                public final class ServerObjectVariable implements Printable {

                    private final CompositePrintable values;

                    public ServerObjectVariable(final String defaultValue) {
                        this(
                            new CompositePrintable()
                                .withPrintable(new NamedStringPrintable("default", Objects.requireNonNull(defaultValue)))
                        );
                    }

                    private ServerObjectVariable(final CompositePrintable values) {
                        this.values = Objects.requireNonNull(values);
                    }

                    public ServerObjectVariable withDescription(final String description) {
                        return new ServerObjectVariable(values.withPrintable(new NamedStringPrintable("description", description)));
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
    void should_generate_class_reference_a_pattern_property_defining_the_whole_object() throws Exception {
        assertThat(
            generateOpenRPCSpecClassWithName("ServerObjectVariables"),
            is(
                """
                package io.github;

                import io.github.sebastiantoepfer.ddd.common.Media;
                import io.github.sebastiantoepfer.ddd.common.Printable;
                import io.github.sebastiantoepfer.ddd.printables.core.CompositePrintable;
                import io.github.sebastiantoepfer.ddd.printables.core.NamedPrintable;
                import java.util.HashMap;
                import java.util.Map;
                import java.util.Objects;
                import java.util.regex.Pattern;
                import javax.annotation.processing.Generated;

                @Generated("jsongen")
                public final class ServerObjectVariables implements Printable {

                    private static final Pattern PROPERTY_NAME_PATTERN = Pattern.compile("[0-z]+");
                    private final Map<String, ServerObjectVariable> additionalValues;
                    private final CompositePrintable values;

                    public ServerObjectVariables() {
                        this(
                            new CompositePrintable()
                            , Map.of()
                        );
                    }

                    private ServerObjectVariables(
                        final CompositePrintable values,
                        final Map<String, ServerObjectVariable> additionalValues
                    ) {
                        this.values = Objects.requireNonNull(values);
                        this.additionalValues = Map.copyOf(additionalValues);
                    }

                    public ServerObjectVariables withValue(final String name, final ServerObjectVariable value) {
                        if (PROPERTY_NAME_PATTERN.asPredicate().negate().test(name)) {
                            throw new IllegalArgumentException("provided name is not valid!");
                        }
                        final var newAdditionalValues = new HashMap(additionalValues);
                        newAdditionalValues.put(name, value);
                        return new ServerObjectVariables(values, newAdditionalValues);
                    }

                    @Override
                    public final <T extends Media<T>> T printOn(final T media) {
                        return additionalValues.entrySet()
                                .stream()
                                .map(e -> new NamedPrintable(e.getKey(), e.getValue()))
                                .reduce(values, CompositePrintable::withPrintable, (l,r) -> null)
                                .printOn(media);
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
