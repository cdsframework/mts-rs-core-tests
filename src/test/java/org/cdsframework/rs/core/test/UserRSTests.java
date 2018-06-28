/*
 * Copyright (C) 2017 New York City Department of Health and Mental Hygiene, Bureau of Immunization
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


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.cdsframework.client.RSClientFactory;
import org.cdsframework.dto.UserDTO;
import org.cdsframework.dto.UserSecurityMapDTO;
import org.cdsframework.rs.core.test.util.CoreRestServiceTest;
import org.cdsframework.rs.core.test.util.CoreTestUtils;
import org.cdsframework.rs.utils.CommonRsUtils;
import org.cdsframework.util.LogUtils;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author HLN Consulting, LLC
 */

public class UserRSTests extends CoreRestServiceTest {
    private static final LogUtils logger = LogUtils.getLogger(UserRSTests.class.getName());
    
    private static final String primaryKey = "4326780713b8f2513dd63826d78b298b";

    @BeforeClass
    public static void beforeSetUpClass() {
        init();
        RSClientFactory rsClientFactory = new RSClientFactory(CoreTestUtils.getCoreBaseUri(), 
                CoreTestUtils.isLoggingFilter(), CoreTestUtils.isGzipSupport());
        restClient = rsClientFactory.getRsClient();
    }

    @Test
    @Override
    public void findByPrimaryKey() {
        findByPrimaryKey(primaryKey);
    }
    
    @Test
    public void newUserInstance() {
        final String METHODNAME = "newUserInstance ";
        try {
            List<String> expand = null;
            Map<String, Object> propertyMap = new HashMap<String, Object>();
            propertyMap.put("queryClass", "ByUserId");
            UserDTO newUserDTO = restClient.newInstance(UserDTO.class, propertyMap, sessionId);
            assertNotNull(newUserDTO);
        } catch (Exception e) {
            CommonRsUtils.logException(e);
            fail(e.getMessage());
        }        
    }
    
    
    @Test
    public void findByQueryListUserSecurityMap() {
        //https://cds.hln.com/mts-rs-core/api/resources/usersecuritymaps?filter=userId=ddce5b65bceb5d26a449e9076d31da9d&property=queryClass=ByUserId&sessionId=36c773d020c62267efa9c424208c0741
        final String METHODNAME = "findByQueryListUserSecurityMap ";
        try {
            List<String> expand = null;
            Map<String, Object> propertyMap = new HashMap<String, Object>();
            propertyMap.put("queryClass", "ByUserId");
            Map<String, Object> filterMap = new HashMap<String, Object>();
            String userId = "ddce5b65bceb5d26a449e9076d31da9d";
            filterMap.put("userId", userId);
            List<UserSecurityMapDTO> foundDTOs = restClient.findByQueryList(UserSecurityMapDTO.class, filterMap, expand, propertyMap, sessionId);
            assertNotNull(foundDTOs);
            assertTrue(!foundDTOs.isEmpty());
            logger.info("foundDTOs.size()=", foundDTOs.size());
        } catch (Exception e) {
            CommonRsUtils.logException(e);
            fail(e.getMessage());
        }        
    }
    
    @Test
    public void findByQueryList() {
        final String METHODNAME = "findByQueryList ";
        logger.info(METHODNAME);
        try {
            List<String> expand = null;
            Map<String, Object> propertyMap = new HashMap<String, Object>();
            propertyMap.put("queryClass", "FindAll");
            Map<String, Object> filterMap = new HashMap<String,Object>();
            List<UserDTO> foundDTOs = restClient.findByQueryList(UserDTO.class, filterMap, expand, propertyMap, sessionId);
            assertNotNull(foundDTOs);
            assertTrue(!foundDTOs.isEmpty());
            logger.info("foundDTOs.size()=", foundDTOs.size());

        } catch (Exception e) {
            CommonRsUtils.logException(e);
            fail(e.getMessage());
        }
    }

    public UserDTO findByPrimaryKey(String primaryKey) {
        final String METHODNAME = "findByPrimaryKey ";
        logger.info(METHODNAME);
        UserDTO foundDTO = null;
        try {
            List<String> childclasses = new ArrayList<String>();
            //childclasses.add("UserSecurityMaps");
            childclasses.add("UserSecurityMapDTO");            
            foundDTO = restClient.findByPrimaryKey(UserDTO.class, primaryKey, childclasses, sessionId);
            assertNotNull(foundDTO);
            foundDTO.getSecuritySchemeDTOs();
            logger.info(METHODNAME, "size="+ foundDTO.getSecuritySchemeDTOs().size());
            assertTrue(foundDTO.getPrimaryKey().equals(primaryKey));
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(primaryKey);
            restClient.findByPrimaryKey(userDTO, sessionId);
        } catch (Exception e) {
            CommonRsUtils.logException(e);
            fail(e.getMessage());
        }
        return foundDTO;
    }
    
    @Override
    public void findByQueryListFindAll() {
        findByQueryListFindAll(UserDTO.class);
    }

    @Override
    public void newRecord(boolean returnResource) {
        final String METHODNAME = "newRecord ";
        notImplemented(METHODNAME);
    }

    @Override
    public void updateRecord(boolean returnResource) {
        final String METHODNAME = "updateRecord ";
        notImplemented(METHODNAME);
    }    
    

}
