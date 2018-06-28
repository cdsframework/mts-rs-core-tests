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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.cdsframework.dto.AppDTO;
import org.cdsframework.enumeration.DTOState;
import org.cdsframework.rs.provider.CoreJacksonJsonProvider;
import org.cdsframework.util.DTOUtils;
import org.cdsframework.util.support.CorePropertyChangeEvent;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author HLN Consulting, LLC
 */
public class PropertyChangeJsonTest {
    

    @Test
    public void appDTOJsonTest() throws JsonProcessingException, IOException {
        AppDTO appDTO = new AppDTO();
        appDTO.setAppName("Original App");
        DTOUtils.setDTOState(appDTO, DTOState.UNSET);
        appDTO.setAppName("New App");
        
        CorePropertyChangeEvent propertyChangeEvent = appDTO.getPropertyChangeEvent("appName");
        assertNotNull(propertyChangeEvent);
        //System.out.println("propertyChangeEvent=" + propertyChangeEvent.getNewValue() + " " + propertyChangeEvent.getOldValue());        
        
        CoreJacksonJsonProvider coreJacksonJsonProvider = new CoreJacksonJsonProvider(JsonInclude.Include.NON_NULL);
        ObjectMapper objectMapper = coreJacksonJsonProvider.createObjectMapper(JsonInclude.Include.NON_NULL, null);

        String serializedJsonAppDTO = objectMapper.writeValueAsString(appDTO);
        assertNotNull(serializedJsonAppDTO);
        
        AppDTO unserializedAppDTO = objectMapper.readValue(serializedJsonAppDTO, AppDTO.class);
        CorePropertyChangeEvent unserializedPropertyChangeEvent = unserializedAppDTO.getPropertyChangeEvent("appName");
        //System.out.println("unserializedPropertyChangeEvent=" + unserializedPropertyChangeEvent.getNewValue() + " " + unserializedPropertyChangeEvent.getOldValue());
        if (unserializedPropertyChangeEvent != null) {
            assertTrue(propertyChangeEvent.getPropertyName().equals(unserializedPropertyChangeEvent.getPropertyName()));
            assertTrue(propertyChangeEvent.getOldValue().equals(unserializedPropertyChangeEvent.getOldValue()));
            assertTrue(propertyChangeEvent.getNewValue().equals(unserializedPropertyChangeEvent.getNewValue()));        
        }
        else {
            System.out.println("IMPORTANT: To enable serializable CorePropertyChangedEvent start by removing @XmlTransient from BaseDTO.propertyChangeEventMap");
        }
        
    }    
    
}
