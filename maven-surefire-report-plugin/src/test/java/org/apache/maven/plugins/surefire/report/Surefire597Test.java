/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.plugins.surefire.report;

import java.io.File;
import java.io.StringWriter;

import junit.framework.TestCase;
import org.apache.maven.doxia.module.xhtml5.Xhtml5Sink;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.tools.SiteTool;
import org.apache.maven.plugin.surefire.log.api.ConsoleLogger;
import org.apache.maven.plugin.surefire.log.api.NullConsoleLogger;
import org.codehaus.plexus.i18n.DefaultI18N;

import static java.util.Collections.singletonList;
import static org.apache.maven.plugins.surefire.report.Utils.toSystemNewLine;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Prevent fom NPE if failure type and message is null however detail presents.
 */
public class Surefire597Test extends TestCase {
    @SuppressWarnings("checkstyle:linelength")
    public void testCorruptedTestCaseFailureWithMissingErrorTypeAndMessage() throws Exception {
        File basedir = new File(".").getCanonicalFile();
        File report = new File(basedir, "target/test-classes/surefire-597");
        StringWriter writer = new StringWriter();
        Sink sink = new Xhtml5Sink(writer) {};
        DefaultI18N i18n = new DefaultI18N();
        i18n.initialize();
        ConsoleLogger consoleLogger = new NullConsoleLogger();
        SurefireReportRenderer r = new SurefireReportRenderer(
                sink, i18n, "surefire", SiteTool.DEFAULT_LOCALE, consoleLogger, true, singletonList(report), null);
        r.render();
        String xml = writer.toString();
        assertThat(
                xml,
                containsString(toSystemNewLine("<table class=\"bodyTable\">\n"
                        + "<tr class=\"a\">\n"
                        + "<th>Tests</th>\n"
                        + "<th>Errors</th>\n"
                        + "<th>Failures</th>\n"
                        + "<th>Skipped</th>\n"
                        + "<th>Success Rate</th>\n"
                        + "<th>Time</th></tr>\n"
                        + "<tr class=\"b\">\n"
                        + "<td>1</td>\n"
                        + "<td>1</td>\n"
                        + "<td>0</td>\n"
                        + "<td>0</td>\n"
                        + "<td>0%</td>\n"
                        + "<td>0 s</td>"
                        + "</tr>"
                        + "</table>")));
        assertThat(
                xml,
                containsString(toSystemNewLine("<table class=\"bodyTable\">\n"
                        + "<tr class=\"a\">\n"
                        + "<th>Package</th>\n"
                        + "<th>Tests</th>\n"
                        + "<th>Errors</th>\n"
                        + "<th>Failures</th>\n"
                        + "<th>Skipped</th>\n"
                        + "<th>Success Rate</th>\n"
                        + "<th>Time</th></tr>\n"
                        + "<tr class=\"b\">\n"
                        + "<td><a href=\"#surefire\">surefire</a></td>\n"
                        + "<td>1</td>\n"
                        + "<td>1</td>\n"
                        + "<td>0</td>\n"
                        + "<td>0</td>\n"
                        + "<td>0%</td>\n"
                        + "<td>0 s</td></tr></table>")));
        assertThat(
                xml,
                containsString(toSystemNewLine("<table class=\"bodyTable\">\n"
                        + "<tr class=\"a\">\n"
                        + "<th>-</th>\n"
                        + "<th>Class</th>\n"
                        + "<th>Tests</th>\n"
                        + "<th>Errors</th>\n"
                        + "<th>Failures</th>\n"
                        + "<th>Skipped</th>\n"
                        + "<th>Success Rate</th>\n"
                        + "<th>Time</th></tr>\n"
                        + "<tr class=\"b\">\n"
                        + "<td><a href=\"#surefire.MyTest\"><img src=\"images/icon_error_sml.gif\" /></a></td>\n"
                        + "<td><a href=\"#surefire.MyTest\">MyTest</a></td>\n"
                        + "<td>1</td>\n"
                        + "<td>1</td>\n"
                        + "<td>0</td>\n"
                        + "<td>0</td>\n"
                        + "<td>0%</td>\n"
                        + "<td>0 s</td></tr></table>")));
        assertThat(
                xml,
                containsString(toSystemNewLine("<table class=\"bodyTable\">\n"
                        + "<tr class=\"a\">\n"
                        + "<td><img src=\"images/icon_error_sml.gif\" /></td>\n"
                        + "<td><a id=\"surefire.MyTest.test\"></a>test</td></tr>\n"
                        + "<tr class=\"b\">\n"
                        + "<td>-</td>\n"
                        + "<td>java.lang.RuntimeException: java.lang.IndexOutOfBoundsException: msg</td></tr>\n"
                        + "<tr class=\"a\">\n"
                        + "<td>-</td>\n"
                        + "<td>\n"
                        + "<div id=\"test-error\">surefire.MyTest:13</div></td></tr></table>")));
    }
}
