/////////////////////////////////////////////////////////////////////////
//
// © University of Southampton IT Innovation Centre, 2017
//
// Copyright in this library belongs to the University of Southampton
// University Road, Highfield, Southampton, UK, SO17 1BJ
//
// This software may not be used, sold, licensed, transferred, copied
// or reproduced in whole or in part in any manner or form or in or
// on any media by any person other than in accordance with the terms
// of the Licence Agreement supplied with the software, or otherwise
// without the prior written consent of the copyright owners.
//
// This software is distributed WITHOUT ANY WARRANTY, without even the
// implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
// PURPOSE, except where stated in the Licence Agreement supplied with
// the software.
//
// Created By : Paul Grace
//
/////////////////////////////////////////////////////////////////////////
//
//  License : GNU Lesser General Public License, version 3
//
/////////////////////////////////////////////////////////////////////////

package uk.ac.soton.itinnovation.modelmyprivacy.modelframework.data;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;
import com.jayway.jsonpath.JsonPath;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import net.minidev.json.JSONArray;

public class AccessPolicies {

    /**
     * The JSON string storing the access policies for a given record.
     */
    private String accessPolicy = null;

    public void loadAccessPolicy(String filename) throws IOException {
        InputStream fis = this.getClass().getClassLoader().getResourceAsStream(filename);
        accessPolicy = CharStreams.toString(new InputStreamReader(fis, Charsets.UTF_8));
        Closeables.closeQuietly(fis);

    }

    public boolean canAccess(String roleName, String fieldName) {
        JSONArray access_policies = JsonPath.read(accessPolicy, "$.policies[?(@.subject=='"+ roleName +"')]");
        for(Object aP: access_policies) {
            String resource = JsonPath.read(aP, "$.resource");
            if(resource.equalsIgnoreCase(fieldName)) {
                return (boolean) JsonPath.read(aP, "$.permission");
            }
        }
        // Split the dot notation
        String[] split = fieldName.split("\\.");
        for(int i=split.length-1; i>0; i--) {
            String resourceName = "";
            for(int j=0; j<i; j++){
                resourceName += split[j]+".";
            }
            if(resourceName.length()>0) {
                resourceName = resourceName.substring(0, resourceName.length()-1);
            }
            for(Object aP: access_policies) {
                String resource = JsonPath.read(aP, "$.resource");
                if(resource.equalsIgnoreCase(resourceName)) {
                    return (boolean) JsonPath.read(aP, "$.permission");
                }
            }
        }
        return false;
    }

    /**
     * Get the fields that a role can access
     * @param fieldName The role to get access policies for on this record
     * @return The list of fields that can be accessed.
     */
    public List<String> canAccessField(String fieldName) {
        List<String> accessTrue = new ArrayList<>();
        JSONArray access_policies = JsonPath.read(accessPolicy, "$.policies[?(@.resource=='"+ fieldName +"')]");
        for(Object aP: access_policies) {
            boolean permission = JsonPath.read(aP, "$.permission");
            if(permission) {
                accessTrue.add((String) JsonPath.read(aP, "$.subject"));
            }
        }
        return accessTrue;
    }

    /**
     * Get the fields that a role can access
     * @param roleName The role to get access policies for on this record
     * @return The list of fields that can be accessed.
     */
    public List<String> canAccess(String roleName) {
        List<String> accessTrue = new ArrayList<>();
        JSONArray access_policies = JsonPath.read(accessPolicy, "$.policies[?(@.subject=='"+ roleName +"')]");
        for(Object aP: access_policies) {
            boolean permission = JsonPath.read(aP, "$.permission");
            if(permission) {
                accessTrue.add((String) JsonPath.read(aP, "$.resource"));
            }
        }
        return accessTrue;
    }

    /**
     * Get the fields that a role can access
     * @param roleName The role to get access policies for on this record
     * @return The list of fields that can be accessed.
     */
    public List<String> cantAccess(String roleName) {
        List<String> accessFalse = new ArrayList<>();
        JSONArray access_policies = JsonPath.read(accessPolicy, "$.policies[?(@.subject=='"+ roleName +"')]");
        for(Object aP: access_policies) {
            boolean permission = JsonPath.read(aP, "$.permission");
            if(!permission) {
                accessFalse.add((String) JsonPath.read(aP, "$.resource"));
            }
        }
        return accessFalse;
    }

}
