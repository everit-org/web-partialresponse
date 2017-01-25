/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.web.partialresponse;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Consumer;

import javax.servlet.ServletResponse;

import org.everit.web.partialresponse.internal.HTMLEscapeWriterWrapper;

/**
 * Builder class to create partial response.
 */
public class PartialResponseBuilder implements Closeable {

  private PrintWriter escapedWriter;

  private final PrintWriter writer;

  /**
   * Constructor.
   *
   * @param response
   *          The response where the partial response will be written to. Cannot be
   *          <code>null</code>!
   */
  public PartialResponseBuilder(final ServletResponse response) {
    Objects.requireNonNull(response, "Response cannot be null!");

    response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    response.setContentType("text/html");
    try {
      this.writer = response.getWriter();
      this.escapedWriter = new PrintWriter(new HTMLEscapeWriterWrapper(response.getWriter()));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    this.writer.write("<partial-response>");
  }

  /**
   * Appends new content to the HTML.
   *
   * @param selector
   *          The CSS selector of the parent element in which the new content will be appended.
   * @param contentProvider
   *          The implementation of the functional interface should write the content that should be
   *          appended to the writer.
   * @return The builder.
   */
  public PartialResponseBuilder append(final String selector,
      final Consumer<PrintWriter> contentProvider) {
    this.writer.write("<partial-append ");
    this.writer.write("selector='");
    this.escapedWriter.write(selector);
    this.writer.write("'>");
    contentProvider.accept(this.escapedWriter);
    this.writer.write("</partial-append>");
    return this;
  }

  /**
   * Appends new content to the HTML.
   *
   * @param selector
   *          The CSS selector of the parent element in which the new content will be appended.
   * @param content
   *          The new content that should be appended.
   * @return The builder.
   */
  public PartialResponseBuilder append(final String selector, final String content) {
    append(selector, (pwriter) -> pwriter.write(content));
    return this;
  }

  @Override
  public void close() {
    this.writer.write("</partial-response>");
  }

  /**
   * Adds a javascript code snippet to the partial response that will be executed on the client.
   *
   * @param javascriptProvider
   *          The implementation of the functional interface should write the javascript code
   *          snippet onto the provided writer.
   * @return The builder.
   */
  public PartialResponseBuilder eval(final Consumer<PrintWriter> javascriptProvider) {
    this.writer.write("<partial-eval>");
    javascriptProvider.accept(this.escapedWriter);
    this.writer.write("</partial-eval>");
    return this;
  }

  /**
   * Adds a Javascript code snippet to the partial response that will be executed on the client.
   *
   * @param javascript
   *          The Javascript code snippet that will be evaluated on the client.
   * @return The builder.
   */
  public PartialResponseBuilder eval(final String javascript) {
    eval((pwriter) -> pwriter.write(javascript));
    return this;
  }

  /**
   * Appends new content to the HTML.
   *
   * @param selector
   *          The CSS selector of the parent element in which the new content will be appended.
   * @param contentProvider
   *          The implementation of the functional interface should write the content that is
   *          prepended to the provided writer. The content that should be appended.
   * @return The builder.
   */
  public PartialResponseBuilder prepend(final String selector,
      final Consumer<PrintWriter> contentProvider) {
    this.writer.write("<partial-prepend ");
    this.writer.write("selector='");
    this.escapedWriter.write(selector);
    this.writer.write("'>");
    contentProvider.accept(this.escapedWriter);
    this.writer.write("</partial-prepend>");
    return this;
  }

  /**
   * Appends new content to the HTML.
   *
   * @param selector
   *          The CSS selector of the parent element in which the new content will be appended.
   * @param content
   *          The content that should be appended.
   * @return The builder.
   */
  public PartialResponseBuilder prepend(final String selector, final String content) {
    prepend(selector, (pwriter) -> pwriter.write(content));
    return this;
  }

  /**
   * Replaces the selected HTML element.
   *
   * @param selector
   *          The CSS selector of element that will be replaced with the new content.
   * @param contentProvider
   *          Writes the content that should replace the old one(s) to the provided writer.
   * @return The builder.
   */
  public PartialResponseBuilder replace(final String selector,
      final Consumer<PrintWriter> contentProvider) {
    this.writer.write("<partial-replace ");
    this.writer.write("selector='");
    this.escapedWriter.write(selector);
    this.writer.write("'>");
    contentProvider.accept(this.escapedWriter);
    this.writer.write("</partial-replace>");
    return this;
  }

  /**
   * Replaces the selected HTML element.
   *
   * @param selector
   *          The CSS selector of element that will be replaced with the new content.
   * @param newContent
   *          The new content that should replace the old one.
   * @return The builder.
   */
  public PartialResponseBuilder replace(final String selector, final String newContent) {
    replace(selector, (pwriter) -> pwriter.write(newContent));
    return this;
  }

  /**
   * Replaces an existing HTML element that has the same id as the one in newContent.
   *
   * @param contentProvider
   *          Writes the new element that should replace the original element to the writer of the
   *          provided writer.
   * @return the builder.
   */
  public PartialResponseBuilder replaceById(final Consumer<PrintWriter> contentProvider) {
    this.writer.write("<partial-replace>");
    contentProvider.accept(this.escapedWriter);
    this.writer.write("</partial-replace>");
    return this;
  }

  /**
   * Replaces an existing HTML element that has the same id as the one in newContent.
   *
   * @param newContent
   *          One ore more HTML elements that will be replaced in the HTML by their ids.
   * @return the builder.
   */
  public PartialResponseBuilder replaceById(final String newContent) {
    replaceById((pwriter) -> pwriter.write(newContent));
    return this;
  }

}
