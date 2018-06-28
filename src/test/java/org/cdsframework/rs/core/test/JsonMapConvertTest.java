package org.cdsframework.rs.core.test;

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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.cdsframework.util.JsonUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.cdsframework.base.BaseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.cdsframework.dto.UserDTO;
import org.cdsframework.enumeration.DTOState;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.rs.provider.CoreJacksonJsonProvider;
import org.cdsframework.rs.utils.CommonRsUtils;
import org.cdsframework.util.DTOUtils;
import org.cdsframework.util.DateUtils;
import org.cdsframework.util.ObjectUtils;
import org.cdsframework.util.support.CorePropertyChangeEvent;

/**
 *
 * @author HLN Consulting, LLC
 */
public class JsonMapConvertTest {
    
    public JsonMapConvertTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        System.setProperty("testroot", "src/test/resources");
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void jsonTest() throws JsonProcessingException {
        List<String> stringList = new ArrayList<>();
        stringList.add("test1");
        stringList.add("test2");
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("about to call writeValueAsString");
        String parameters = objectMapper.writeValueAsString(stringList);
        System.out.println("parameters=" + parameters);
        Map<String, Object> map = new HashMap<>();
        map.put("first", "pete");
        map.put("dob", new Date());
        map.put("id", 141212L);
        parameters = objectMapper.writeValueAsString(map);
        System.out.println("parameters=" + parameters);

    }

    @Test
    public void mapTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("first", "pete");
        map.put("dob", new Date());
        map.put("id", 14788881212L);
        String jsonMap = CommonRsUtils.getMapAsEncodedString(map, false);
        System.out.println("jsonMap=" + jsonMap);
        Map<String, Object> mapFromJSONString = CommonRsUtils.getMapFromEncodedString(jsonMap);
        Set<Map.Entry<String, Object>> entrySet = mapFromJSONString.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            System.out.println("entry key/value/class=" + entry.getKey() + "/" + entry.getValue() + "/" + entry.getValue().getClass().getCanonicalName());
        }
        
    }
    
    @Test
    public void mapAsEncodedString() {
        Map<String, Object> map = new HashMap<>();
        map.put("first", "pete");
        map.put("last", "smith");
        map.put("dob", new Date());
        map.put("nullString", null);
        map.put("emtryString", "");
        map.put("brownFox", "  the quick brown fox jumped over the fencse ");
        map.put("keyValueWithCommas", "  the quick, brown fox jumped, over the, fencse ");
        map.put("id", 14788881212L);
        String mapAsEncodedString = CommonRsUtils.getMapAsEncodedString(map);
        System.out.println("mapAsEncodedString=" + mapAsEncodedString);

        Map<String, Object> mapFromEncodedString = CommonRsUtils.getMapFromEncodedString(mapAsEncodedString);
        Set<Map.Entry<String, Object>> entrySet = mapFromEncodedString.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            System.out.println("entry key=" + entry.getKey());
            if (entry.getValue() != null) {
                System.out.println("entry value=" + entry.getValue());
                System.out.println("entry value class=" + entry.getValue().getClass().getCanonicalName());

            }

        }
    }

    private TimeZone getTimeZone(String tz) {
        return TimeZone.getTimeZone(tz);
    }
    
    @Test
    public void getDateTimeByTimeZone() {
        String tz = "Europe/Athens";
        //String tz = "Asia/Seoul";        
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.ISO8601_UTC_DATETIME);
        dateFormat.setTimeZone(getTimeZone("Europe/Athens"));
        GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
        //cal.add(Calendar.DATE, 0);
        // Should be in the tz above
        Date dateTime = cal.getTime();
        System.out.println(dateFormat.format(dateTime));     
        
        // Should be in the system timezone
        dateFormat = new SimpleDateFormat(DateUtils.ISO8601_UTC_DATETIME);
        dateFormat.setTimeZone(getTimeZone("US/Eastern"));
        System.out.println(dateFormat.format(dateTime));             
        
        dateFormat = new SimpleDateFormat(DateUtils.ISO8601_UTC_DATETIME);
        dateFormat.setTimeZone(getTimeZone("UTC"));
        System.out.println(dateFormat.format(dateTime));             
        
        Date expirationDate = DateUtils.parseDateFromString("07/03/2017", DateUtils.DATEINMASK);
        System.out.println(expirationDate);             
        
        dateFormat = new SimpleDateFormat(DateUtils.ISO8601_UTC_DATETIME);
        dateFormat.setTimeZone(getTimeZone("US/Eastern"));
        System.out.println(dateFormat.format(expirationDate));

        dateFormat = new SimpleDateFormat(DateUtils.ISO8601_UTC_DATETIME);
        dateFormat.setTimeZone(getTimeZone("UTC"));
        System.out.println(dateFormat.format(expirationDate));             
        
        
    }
    
    @Test
    public void dateTimeZone() throws ParseException, JsonProcessingException, IOException, MtsException {
        CoreJacksonJsonProvider coreJacksonJsonProvider = new CoreJacksonJsonProvider(JsonInclude.Include.NON_NULL);
        ObjectMapper objectMapper = coreJacksonJsonProvider.createObjectMapper(JsonInclude.Include.NON_NULL, null);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        UserDTO userDTOOut = new UserDTO();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateUtils.ISO8601_UTC_DATETIME);
        dateFormat.setTimeZone(getTimeZone("Europe/Athens"));
        GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
        Date dateTime = cal.getTime();
        System.out.println(dateFormat.format(dateTime));             
        
        userDTOOut.setExpirationDate(dateTime);
        DTOUtils.setDTOState(userDTOOut, DTOState.UNSET);
        
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        //Date dd = sdf.parse("2016-05-29T12:15:11.000+0000");        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dd = sdf.parse("2016-05-29");        
        
        userDTOOut.setExpirationDate(dd);
        
        String jsonString = objectMapper.writeValueAsString(userDTOOut);
        System.out.println("jsonString=" + jsonString);
        UserDTO userDTOIn = objectMapper.readValue(jsonString, UserDTO.class);
        System.out.println("userDTOIn=" + userDTOIn.getExpirationDate());

        // No support for CorePropertyChangeEvent needs more work to serialize it
        CorePropertyChangeEvent propertyChangeEvent = userDTOIn.getPropertyChangeEvent("expirationDate");
        if (propertyChangeEvent != null) {
            System.out.println("getOldValue=" + propertyChangeEvent.getOldValue() + " " + propertyChangeEvent.getOldValue().getClass().getCanonicalName());
            System.out.println("getNewValue=" + propertyChangeEvent.getNewValue() + " " + propertyChangeEvent.getNewValue().getClass().getCanonicalName());
            Date oldDate = ObjectUtils.objectToDate(propertyChangeEvent.getOldValue());
            System.out.println("oldDate=" + oldDate); 
            Date newDate = ObjectUtils.objectToDate(propertyChangeEvent.getNewValue());
            System.out.println("newDate=" + newDate); 
        }
        else {
            System.out.println("IMPORTANT: To enable serializable CorePropertyChangedEvent start by removing @XmlTransient from BaseDTO.propertyChangeEventMap");
        }
    }
    
}
