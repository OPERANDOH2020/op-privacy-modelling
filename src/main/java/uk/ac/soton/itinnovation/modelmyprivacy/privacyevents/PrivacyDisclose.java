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

import java.io.Serializable;

/**
 * Capture the data held in each rest event. Note we use a builder pattern
 * rather than a single constructor. Information is extracted from
 * multiple sources, and the event is built up over time.
 * @author pjg
 */
public class PrivacyDisclose implements Serializable {

    /**
     * Portable serializable class.
     */
    public static final long serialVersionUID = 1L;

    /** XML <guards> tag constant. */
    public static final String ORGANISATION_LABEL = "organisation";

    /** XML <guards><action> tag constant. */
    public static final String ROLE_LABEL = "role";

    /** XML <guards><action> tag constant. */
    public static final String INDIVIDUAL_LABEL = "individual";

    /**
     * Disclosure event to a general organisation. Not to an individual role
     * or person within. Organisation cannot be null.
     *
     */
    public PrivacyDisclose(String organisation){

    }

    /**
     *
     * @param organisation The organisation
     * @param role The role
     */
    public PrivacyDisclose(String organisation, String role){

    }

    /**
     * The content of the message - optional, not all REST events will
     * have associated data.
     */
    private Content dataBody;

    /**
     * Getter for the data content of the event.
     * @return The data content.
     */
    public final Content getDataBody() {
        return dataBody;
    }

    /**
     * A receiving state stores the response time for the server
     * to react to the request. This can be used for QoS metric testing.
     */
    private long responseTime;

    /**
     * get the event time stamp
     * @return the time stamp in milliseconds
     */
    public long getResponseTime() {
        return this.responseTime;
    }

    /**
     * Set the event time response
     * @respTime the time stamp in milliseconds
     */
    public void setResponseTime(long respTime) {
        this.responseTime = respTime;
    }

    /**
     * Set the event content.
     * @param newBody Content to set.
     */
    public final void setDataBody(final Content newBody) {
        dataBody = newBody;
    }

    /**
     * Add the event body.
     * @param type The type of the data content
     * @param body The data content itself.
     */
    public final void addContent(final String type, final String body) {
        setDataBody(new Content(type, body));
    }

}
