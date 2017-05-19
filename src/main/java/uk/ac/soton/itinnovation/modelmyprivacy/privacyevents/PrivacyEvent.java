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

package uk.ac.soton.itinnovation.modelmyprivacy.privacyevents;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;

/**
 * Construct the privacy event object that is used to inform the transitions
 * through the LTS model.
 *
 * @author pjg
 */
public class PrivacyEvent implements Serializable {

    /**
     * Portable serializable class.
     */
    public static final long serialVersionUID = 1L;

    /**
     * XML <guards> tag constant.
     */
    public static final String GUARDS_LABEL = "guards";

    /**
     * XML <guards><action> tag constant.
     */
    public static final String ACTION_LABEL = "action";

    /**
     * XML <guards><role> tag constant.
     */
    public static final String ROLE_LABEL = "role";

    /**
     * XML <guards><purpose> tag constant.
     */
    public static final String PURPOSE_LABEL = "purpose";

    /**
     * XML <param>tag constant.
     */
    public static final String DATA_LABEL = "data";

    /**
     * XML <value>tag constant.
     */
    public static final String DISCLOSE_LABEL = "disclose";

    /**
     * A privacy action can take one of the following actions only.
     */
    public enum Action {
        ANONYMIZE("Anonymize"),
        READ("Read"),
        DISCLOSE("Disclose"),
        ARCHIVE("Archive"),
        UPDATE("Update"),
        DELETE("Delete"),
        CREATE("Create");

        private final String value;

        Action(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * The action field of the event.
     */
    private String actionField;

    /**
     * Getter for the action field. There is no setter as the event is created
     * from the json input object.
     *
     * @return
     */
    public String getActionField() {
        return this.actionField.toString();
    }

    /**
     * The role field of the access event. A role is aligned with a taxonomy of
     * roles for the application request.
     */
    private String role;

    /**
     * Getter for the role field. There is no setter as the event is created
     * from the json input object.
     *
     * @return
     */
    public String getRoleField() {
        return this.role;
    }

    /**
     * Validate that the role is part of the privacy application schema. For
     * example, if the role is plumber and the application is medical--then
     * there is a mismatch in the schemas, and the role isn't valid in this
     * event.
     *
     * @param schema The application schema, with the role taxonomy.
     * @return boolean indicating the validation outcome.
     */
    private boolean validateRole(String schema) {
        return true;
    }

    /**
     * The data field of the access event. Tne identifier of the field - it
     * could be a full URL resource, or a name (Age), or field id (a.b.age),
     * etc.
     */
    private String data;

    /**
     * Getter for the role field. There is no setter as the event is created
     * from the json input object.
     *
     * @return
     */
    public String getDataField() {
        return this.data;
    }

    /**
     * Operando defines a set of core categories of private data that a data
     * field concerns e.g. contact data, location data, medical data, financial
     * data etc.
     *
     * @param schema
     * @return
     */
    public String getDataCategory(String schema) {
        return "";
    }

    /**
     * A privacy event is input in JSON format. With 0 or more of the class
     * fields given a value.
     *
     * @param jsonInput The json event input.
     */
    public PrivacyEvent(String jsonInput) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            DataRequest ospData = mapper.readValue(jsonInput, DataRequest.class);
            this.actionField = ospData.getAction().toString();
            this.role = ospData.getSubject();
            this.data = ospData.getRequestedUrl();
        } catch (IOException e) {
            e.printStackTrace();
        }

//Object to JSON in String
    }

}
