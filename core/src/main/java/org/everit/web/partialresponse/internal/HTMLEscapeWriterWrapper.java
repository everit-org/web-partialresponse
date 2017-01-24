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
package org.everit.web.partialresponse.internal;

import java.io.IOException;
import java.io.Writer;

/**
 * Writer wrapper that escapes all quotes, apostrophes, ampersands, lt and gt characters with
 * ampersand expressions.
 */
public class HTMLEscapeWriterWrapper extends Writer {

  private final Writer wrapped;

  public HTMLEscapeWriterWrapper(final Writer wrapped) {
    this.wrapped = wrapped;
  }

  @Override
  public void close() throws IOException {
    wrapped.close();
  }

  @Override
  public void flush() throws IOException {
    wrapped.flush();
  }

  @Override
  public void write(final char[] cbuf, final int off, final int len) throws IOException {
    StringBuilder sb = new StringBuilder(len);
    for (int i = off, n = off + len; i < n; i++) {
      char c = cbuf[i];
      switch (c) {
        case '&':
          sb.append("&amp;");
          break;
        case '\'':
          sb.append("&apos;");
          break;
        case '"':
          sb.append("&quot;");
          break;
        case '<':
          sb.append("&lt;");
          break;
        case '>':
          sb.append("&gt;");
          break;
        default:
          sb.append(c);
          break;
      }
    }
    wrapped.write(sb.toString());
  }

}
