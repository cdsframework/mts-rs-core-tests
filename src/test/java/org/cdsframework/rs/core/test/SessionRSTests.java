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
package org.cdsframework.rs.core.test;

import java.util.Date;
import javax.ws.rs.core.Form;
import org.cdsframework.exceptions.AuthenticationException;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.rs.core.test.util.CoreTestUtils;
import org.cdsframework.rs.client.SessionRSClient;
import org.cdsframework.rs.utils.CommonRsUtils;
import org.cdsframework.util.LogUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author HLN Consulting, LLC
 */
public class SessionRSTests {
    
    private static final LogUtils logger = LogUtils.getLogger(SessionRSTests.class.getName());
    
    private static String username = null;
    private static String password = null;
    private static String application = null;
    private static String coreBaseUri = null;
    private static SessionRSClient securityRestServiceClient;

    public SessionRSTests() {
    }

    @BeforeClass
    public static void setUpClass() {
        username = CoreTestUtils.getUsername();
        password = CoreTestUtils.getPassword();
        application = CoreTestUtils.getApp();
        coreBaseUri = CoreTestUtils.getCoreBaseUri();
        securityRestServiceClient = new SessionRSClient(coreBaseUri, CoreTestUtils.isLoggingFilter());
    }

    @AfterClass
    public static void tearDownClass() {
        securityRestServiceClient.close();
    }    
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void login() {
        final String METHODNAME = "login ";
        logger.info(METHODNAME);
        try {
            String sessionId = securityRestServiceClient.login(username, password, application);
            logger.info(METHODNAME, "sessionId=", sessionId);
            assertNotNull(sessionId);
        } catch (Exception e) {
            CommonRsUtils.logException(e);
            fail(e.getMessage());
        }            
    }
    
    @Test
    public void getORProxiedUserSession() {
        final String METHODNAME = "getORProxiedUserSession ";
        logger.info(METHODNAME);
        try {
            String sessionId = securityRestServiceClient.login(
                    CoreTestUtils.getProperties().getProperty("OR_TEST_USERNAME"), 
                    CoreTestUtils.getProperties().getProperty("OR_TEST_PASSWORD"),
                    CoreTestUtils.getProperties().getProperty("OR_TEST_APP"));
            assertNotNull(sessionId);
            logger.info(METHODNAME, "sessionId=", sessionId);
            String proxySessionId = securityRestServiceClient.getProxiedUserSession(sessionId, 
                    CoreTestUtils.getProperties().getProperty("OR_TEST_PROXYUSER"));
            assertNotNull(proxySessionId);
            logger.info(METHODNAME, "proxySessionId=", proxySessionId);
        }
        catch (MtsException e) {
            if (!(e.getMessage().indexOf("No app proxy registered for: FindOnlineRegistryProxyUser") >=0)) {
                fail(e.getMessage());
            }
        } catch (Exception e) {
            CommonRsUtils.logException(e);
            fail(e.getMessage());
        }            
    }    
    
    @Test
    public void getHL7ProxiedUserSession() {
        final String METHODNAME = "getHL7ProxiedUserSession ";
        logger.info(METHODNAME);
        try {
            String sessionId = securityRestServiceClient.login(
                    CoreTestUtils.getProperties().getProperty("HL7_TEST_USERNAME"), 
                    CoreTestUtils.getProperties().getProperty("HL7_TEST_PASSWORD"),
                    CoreTestUtils.getProperties().getProperty("HL7_TEST_APP"));
            assertNotNull(sessionId);
            logger.info(METHODNAME, "sessionId=", sessionId);
            String proxySessionId = securityRestServiceClient.getProxiedUserSession(sessionId, 
                    CoreTestUtils.getProperties().getProperty("HL7_TEST_PROXYUSER"));
            assertNotNull(proxySessionId);
            logger.info(METHODNAME, "proxySessionId=", proxySessionId);
        }
        catch (MtsException e) {
            if (!(e.getMessage().indexOf("No app proxy registered for: FindHl7ServiceProxyUser") >=0)) {
                fail(e.getMessage());
            }            
        } catch (Exception e) {
            CommonRsUtils.logException(e);
            fail(e.getMessage());
        }            
    }        

    @Test
    public void getHL7ProxiedUserSessionBadProxyUser() {
        final String METHODNAME = "getHL7ProxiedUserSession ";
        logger.info(METHODNAME);
        try {
            String sessionId = securityRestServiceClient.login(
                    CoreTestUtils.getProperties().getProperty("HL7_TEST_USERNAME"), 
                    CoreTestUtils.getProperties().getProperty("HL7_TEST_PASSWORD"),
                    CoreTestUtils.getProperties().getProperty("HL7_TEST_APP"));
            assertNotNull(sessionId);
            logger.info(METHODNAME, "sessionId=", sessionId);
            String proxySessionId = securityRestServiceClient.getProxiedUserSession(sessionId, "baduser");
            assertNotNull(proxySessionId);
            logger.info(METHODNAME, "proxySessionId=", proxySessionId);
        }
        catch (MtsException e) {
            if (!(e.getMessage().indexOf("Table: MT_USER") >=0)) {
                fail(e.getMessage());
            }            
            
        } catch (AuthenticationException e) {

        } catch (Exception e) {
            CommonRsUtils.logException(e);
            fail(e.getMessage());
        }            
    }
    
    @Test
    public void badLogin() {
        final String METHODNAME = "badLogin ";
        logger.info(METHODNAME);
        try {
            String sessionId = securityRestServiceClient.login(password, username, application);
            logger.info(METHODNAME, "sessionId=", sessionId);
            assertNotNull(sessionId);
        }
        catch (AuthenticationException e) {
            
        } catch (Exception e) {
            CommonRsUtils.logException(e);
            fail(e.getMessage());
        }            
    }
        
    @Test
    public void loginForm() {
        final String METHODNAME = "loginForm ";
        logger.info(METHODNAME);
        try {
            Form form = new Form();    
            form.param("username", username);
            form.param("password", password);
            form.param("applicationName", application);
            String sessionId = securityRestServiceClient.login(form);
            logger.info(METHODNAME, "sessionId=", sessionId);
            assertNotNull(sessionId);
        } catch (Exception e) {
            CommonRsUtils.logException(e);
            fail(e.getMessage());
        }            
    }
    
    @Test
    public void isSessionValid() {
        logger.info("isValidSession");
        long start = System.nanoTime();
        try {
            String sessionId = securityRestServiceClient.login(username, password, application);
            logger.info("sessionId=", sessionId);
            boolean sessionValid = securityRestServiceClient.isSessionValid(sessionId + "6252423424234");
            logger.info("sessionValid=", sessionValid);

            assertFalse(sessionValid);
        } catch (Exception e) {
            CommonRsUtils.logException(e);
            fail(e.getMessage());
        } finally{
            logger.logDuration("isValidSession", start);
        }
    }
    
    @Test
    public void isSessionValid2() {
        logger.info("isValidSession");
        long start = System.nanoTime();
        try {
            String sessionId = securityRestServiceClient.login(username, password, application);
            logger.info("sessionId=", sessionId);
            boolean sessionValid = securityRestServiceClient.isSessionValid(sessionId);
            logger.info("sessionValid=", sessionValid);
            assertTrue(sessionValid);
        } catch (Exception e) {
            CommonRsUtils.logException(e);
            fail(e.getMessage());
        } finally{
            logger.logDuration("isSessionValid2", start);
        }
    }

    @Test
    public void badSession() {
        final String METHODNAME = "badSession ";
        logger.info(METHODNAME);
        try {
            String sessionId = new Date().getTime() + "";
            logger.info(METHODNAME, "sessionId=", sessionId);
            boolean sessionValid = securityRestServiceClient.isSessionValid(sessionId);
            logger.info("sessionValid=", sessionValid);
            assertFalse(sessionValid);
        } catch (Exception e) {
            CommonRsUtils.logException(e);
            fail(e.getMessage());
        }
    }
    
    @Test
    public void logout() {
        final String METHODNAME = "logout ";
        logger.info(METHODNAME);
        
        try {
            String sessionId = securityRestServiceClient.login(username, password, application);
            logger.info(METHODNAME, "sessionId=", sessionId);
            boolean loggedOut = securityRestServiceClient.logout(sessionId);
            assertTrue(loggedOut);
        } catch (Exception e) {
            CommonRsUtils.logException(e);
            fail(e.getMessage());
        }            
    }
    
    @Test
    public void badLogout() {
        final String METHODNAME = "badLogout ";
        logger.info(METHODNAME);
        
        try {
            String sessionId = securityRestServiceClient.login(username, password, application);
            logger.info(METHODNAME, "sessionId=", sessionId);
            boolean loggedOut = securityRestServiceClient.logout(sessionId + "zxcasdasd");
            logger.info(METHODNAME, "loggedOut=", loggedOut);
            assertTrue(loggedOut);
        } catch (Exception e) {
            CommonRsUtils.logException(e);
            fail(e.getMessage());
        }            
    }    
    
}
