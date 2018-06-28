/*
 * Copyright (C) 2015 New York City Department of Health and Mental Hygiene, Bureau of Immunization
 * Contributions by HLN Consulting, LLC
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. You should have received a copy of the GNU Lesser
 * General Public License along with this program. If not, see <http://www.gnu.org/licenses/> for more
 * details.
 *
 * The above-named contributors (HLN Consulting, LLC) are also licensed by the New York City
 * Department of Health and Mental Hygiene, Bureau of Immunization to have (without restriction,
 * limitation, and warranty) complete irrevocable access and rights to this project.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; THE
 *
 * SOFTWARE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING,
 * BUT NOT LIMITED TO, WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE COPYRIGHT HOLDERS, IF ANY, OR DEVELOPERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES, OR OTHER LIABILITY OF ANY KIND, ARISING FROM, OUT OF, OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information about the this software, see http://www.hln.com/ice or send
 * correspondence to ice@hln.com.
 */
package org.cdsframework.rs.core.test.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.cdsframework.util.LogUtils;

/**
 *
 * @author HLN Consulting, LLC
 */
public class CoreTestUtils {

    private static final LogUtils logger = LogUtils.getLogger(CoreTestUtils.class);
    private static final Properties properties = new Properties();
    private static final String username;
    private static final String password;
    private static final String app;
    private static final String coreBaseUri;
    private static boolean loggingFilter = false;
    private static boolean gzipSupport = false;

    static {
        try {
            properties.load(new FileInputStream("src/test/resources/test.properties"));
            username = properties.getProperty("TEST_USERNAME");
            password = properties.getProperty("TEST_PASSWORD");
            app = properties.getProperty("TEST_APP");
            coreBaseUri = properties.getProperty("CORE_BASE_URI");
            String logFilter = properties.getProperty("LOGGING_FILTER");
            if (logFilter != null) {
                loggingFilter = Boolean.parseBoolean(logFilter);
            }
            String gzip = properties.getProperty("GZIP_SUPPORT");
            if (gzip != null) {
                gzipSupport = Boolean.parseBoolean(gzip);
            }

            logger.info("username=", username);
            logger.info("app=", app);
            logger.info("coreBaseUri=", coreBaseUri);
            
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static String getUsername() {
        return username;
    }
    public static String getPassword() {
        return password;
    }
    public static String getApp() {
        return app;
    }
    public static String getCoreBaseUri() {
        return coreBaseUri;
    }

    public static LogUtils getLogger() {
        return logger;
    }

    public static Properties getProperties() {
        return properties;
    }

    public static boolean isLoggingFilter() {
        return loggingFilter;
    }
    
    public static boolean isGzipSupport() {
        return gzipSupport;
    }

}
