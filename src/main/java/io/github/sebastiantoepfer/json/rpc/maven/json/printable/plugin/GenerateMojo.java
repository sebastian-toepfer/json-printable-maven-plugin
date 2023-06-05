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
package io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin;

import com.samskivert.mustache.Mustache;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.generator.MustacheJavaClassTemplate;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.model.CompositeTypeRegistry;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.model.JsonObjectClassDefinition;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.model.JsonTypeToJavaTypeMapping;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.model.PrintableAdapters;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.model.Property;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.model.SchemaParser;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

@Mojo(
    name = "generate",
    defaultPhase = LifecyclePhase.GENERATE_SOURCES,
    threadSafe = true,
    requiresDependencyResolution = ResolutionScope.RUNTIME
)
public class GenerateMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    @Parameter(required = true)
    private String packageName;

    @Parameter(required = true)
    private String jsonSchemaClassName;

    @Parameter
    private URL schemaUrl;

    @Parameter(defaultValue = "${project.build.directory}/generated-sources/printablejson")
    private File sourceDestDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().debug("> execute");
        try {
            final JsonObject schema = loadSchema();
            JsonTypeToJavaTypeMapping mapping = new JsonTypeToJavaTypeMapping(jsonSchemaClassName);
            final List<JsonObjectClassDefinition> model = new SchemaParser(
                schema,
                new CompositeTypeRegistry(List.of(mapping, new PrintableAdapters())),
                mapping
            )
            .createModel();

            final MustacheJavaClassTemplate objectClassTemplate = createTemplate();

            model.stream().map(def -> def.withPackage(packageName)).forEach(objectClassTemplate::generate);

            final MustacheJavaClassTemplate parameterClassTemplate = createTemplate("parameter_class.mustache");
            model
                .stream()
                .flatMap(def -> Stream.concat(def.properties().stream(), def.required().stream()))
                .map(Property::alternatives)
                .filter(Objects::nonNull)
                .forEach(parameterClassTemplate::generate);

            addSourceRoot();
            getLog().debug("< execute");
        } catch (IOException e) {
            final MojoExecutionException thrown = new MojoExecutionException(e.getLocalizedMessage(), e);
            getLog().debug("< execute thrown ", thrown);
            throw thrown;
        }
    }

    private void addSourceRoot() {
        getLog().debug("> addSourceRoot");
        addSource(sourceDestDir.getAbsolutePath());
        getLog().debug("< addSourceRoot");
    }

    private void addSource(final String absolutePath) {
        getLog().debug(String.format("> addSourceRoot %s", absolutePath));
        if (project.getCompileSourceRoots().contains(absolutePath)) {
            getLog().debug("existing src root: " + sourceDestDir);
        } else {
            getLog().debug("adding src root: " + absolutePath);
            project.addCompileSourceRoot(absolutePath);
        }
        getLog().debug("< addSourceRoot");
    }

    private MustacheJavaClassTemplate createTemplate() {
        return createTemplate("default_json_object_class.mustache");
    }

    private MustacheJavaClassTemplate createTemplate(final String templateResource) {
        getLog().debug("> createTemplate");
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
            sourceDestDir.toPath()
        );
        getLog().debug("< createTemplate");
        return result;
    }

    private JsonObject loadSchema() throws IOException {
        getLog().debug("> loadSchema");
        final JsonObject schema;
        try (JsonReader reader = Json.createReader(schemaAsStream())) {
            schema = reader.readObject();
        }
        getLog().debug(String.format("< loadSchema %s", schema));
        return schema;
    }

    private InputStream schemaAsStream() throws IOException {
        getLog().debug("> schemaAsStream");
        final InputStream result;
        if (schemaUrl == null) {
            getLog().debug("use bundled schema.");
            result = GenerateMojo.class.getClassLoader().getResource("open-rpc-meta-schema.json").openStream();
        } else {
            getLog().debug(String.format("use url %s to retrieve schema.", schemaUrl));
            result = schemaUrl.openStream();
        }
        getLog().debug("< schemaAsStream");
        return result;
    }
}
