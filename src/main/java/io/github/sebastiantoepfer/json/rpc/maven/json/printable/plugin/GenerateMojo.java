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

import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.generator.JavaClassOutput;
import io.github.sebastiantoepfer.json.rpc.maven.json.printable.plugin.model.JsonTypeToJavaTypeMapping;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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
            new CodeGenerator(
                new ModelCreator(new JsonSchemaProvider(schemaUrl), new JsonTypeToJavaTypeMapping(jsonSchemaClassName)),
                packageName,
                new Templates(new JavaClassOutput(sourceDestDir.toPath()))
            )
                .generateModel();
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
}
