/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cdsframework.rs.core.test.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.cdsframework.base.BaseDTO;
import org.cdsframework.client.RSClient;
import org.cdsframework.exceptions.AuthenticationException;
import org.cdsframework.exceptions.AuthorizationException;
import org.cdsframework.exceptions.ConstraintViolationException;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.exceptions.NotFoundException;
import org.cdsframework.exceptions.ValidationException;
import org.cdsframework.rs.client.SessionRSClient;
import org.cdsframework.rs.support.CoreRsConstants;
import org.cdsframework.rs.utils.CommonRsUtils;
import org.cdsframework.util.DTOUtils;
import org.cdsframework.util.LogUtils;
import org.junit.AfterClass;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author HLN Consulting, LLC
 */
public abstract class CoreRestServiceTest {
 
    private static final LogUtils logger = LogUtils.getLogger(CoreRestServiceTest.class.getName());
    
    private static String username = null;
    private static String password = null;
    private static String application = null;
    public static String coreBaseUri = null;
    public static String sessionId;
    public static boolean loggingFilter = false;
    public static SessionRSClient securityClient;
    public static RSClient restClient;

    public static void init() {
        logger.info("init");
        username = CoreTestUtils.getUsername();
        password = CoreTestUtils.getPassword();
        application = CoreTestUtils.getApp();
        coreBaseUri = CoreTestUtils.getCoreBaseUri();
        securityClient = new SessionRSClient(coreBaseUri, CoreTestUtils.isLoggingFilter());
        try {
            sessionId = securityClient.login(username, password, application);
        } catch (MtsException | AuthenticationException | AuthorizationException | ConstraintViolationException | ValidationException | NotFoundException ex) {
            logger.error("An ", ex.getClass().getSimpleName(), " has occurred; Message: ", ex.getMessage(), ex);
        }
        logger.info("sessionId=" + sessionId);
    }

    @AfterClass
    public static void tearDownClass() {
        try {
            restClient.close();
            securityClient.logout(sessionId);
            securityClient.close();
        } catch (ConstraintViolationException | ValidationException | NotFoundException | AuthenticationException | AuthorizationException | MtsException ex) {
            logger.error("An ", ex.getClass().getSimpleName(), " has occurred; Message: ", ex.getMessage(), ex);
        }
    }

    @Test
    public void testPing() {
        final String METHODNAME = "testPing ";
        logger.info(METHODNAME);
        String message = "hello";
        assertTrue(restClient.ping(message).contains(message));
    }    
    
    @Test
    public void testFindByPrimaryKey() {
        final String METHODNAME = "testFindByPrimaryKey ";
        logger.info(METHODNAME);
        findByPrimaryKey();
    }
    
    @Test
    public void testFindByQueryListFindAll() {
        final String METHODNAME = "testFindByQueryListFindAll ";
        logger.info(METHODNAME);
        findByQueryListFindAll();
    }
    
    @Test
    public void testNew() {
        logger.info("testNew");
        newRecord(false);
    }

    @Test
    public void testNewReturnResource() {
        logger.info("testNewReturnResource");
        newRecord(true);
    }
    
    @Test
    public void testUpdateReturnResource() {
        final String METHODNAME = "testUpdateReturnResource ";
        logger.info(METHODNAME);
        updateRecord(true);
    }

    @Test
    public void testUpdate() {
        final String METHODNAME = "testUpdate ";
        logger.info(METHODNAME);
        updateRecord(false);
    }

    public abstract void findByQueryListFindAll() ;
    public abstract void findByPrimaryKey() ;
    public abstract void newRecord(boolean returnResource) ;
    public abstract void updateRecord(boolean returnResource) ;
    
    
    public static Integer getRandom(int seed) {
        Random randmom = new Random( System.currentTimeMillis() );
        return ((1 + randmom.nextInt(2)) * seed + randmom.nextInt(seed));
    }    
    
    public <T> List<T> findByQueryListFindAll(Class<T> classType ) {
        final String METHODNAME = "findByQueryListFindAll ";
        logger.info(METHODNAME);
        List<T> foundDTOs = null;
        try {
            Map<String, Object> propertyMap = new HashMap<String, Object>();
            propertyMap.put("queryClass", "FindAll");
            foundDTOs = restClient.findByQueryList(classType, null, null, propertyMap, sessionId);
            assertNotNull(foundDTOs);
            assertTrue(!foundDTOs.isEmpty());
            logger.info("foundDTOs.size()=", foundDTOs.size());

        } catch (Exception e) {
            CommonRsUtils.logException(e);
            fail(e.getMessage());
        }
        return foundDTOs;
    }
    
    public void notImplemented(String METHODNAME) {
        logger.info(METHODNAME, "TEST NOT IMPLEMENTED");    
    }
    
    public void validatePrimaryKeys(BaseDTO dto, List<Object> primaryKeys) {
        int counter = 0;
        
        // Get the source primary keys
        List<Object> sourcePrimaryKeys = DTOUtils.getPrimaryKeys(dto);
        for ( Object sourceKey : sourcePrimaryKeys) {
            assertTrue(sourceKey.equals(primaryKeys.get(counter)));
            counter ++;
        }
    }
    
    public Map<String, Object> getReturnResourcePropertyMap() {
        return getReturnResourcePropertyMap(true);
    }

    public Map<String, Object> getReturnResourcePropertyMap(boolean returnResource) {
        Map<String, Object> propertyMap = new HashMap<String, Object>();
        propertyMap.put(CoreRsConstants.RS_RETURN_RESOURCE, returnResource);
        return propertyMap;
    }
    
}
