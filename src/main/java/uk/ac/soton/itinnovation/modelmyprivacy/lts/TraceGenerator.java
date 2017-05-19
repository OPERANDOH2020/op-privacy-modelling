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


package uk.ac.soton.itinnovation.modelmyprivacy.lts;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyevents.PrivacyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyevents.DataRequest;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Closeables;

/**
 * Create a trace of Privacy events from json data held on a file.
 *
 * The file is a trace of json events, that is an array of objects is
 * parsed to generate privacy events that are then sent to the
 *
 * @author pjg
 */
public final class TraceGenerator {
    /**
     * Utility class with private constructor.
     */
    public TraceGenerator() {
        // empty implementation
    }

    /**
     * Generate a set of REST events that have been saved to a file.
     *
     * @param jsonDataFile The file with json events stored as a trace
     * @param sMachine The state machine pattern that the events will be tested
     * against,
     */
    public void generateEvents(final String jsonDataFile, final StateMachine sMachine) {

        // Open the trace file
        try {
            InputStream fis = this.getClass().getClassLoader().getResourceAsStream(jsonDataFile);
            String content = CharStreams.toString(new InputStreamReader(fis, Charsets.UTF_8));
            Closeables.closeQuietly(fis);

            ObjectMapper mapper = new ObjectMapper();
            List<DataRequest> myObjects = mapper.readValue(content, new TypeReference<List<DataRequest>>(){});
            for (DataRequest e : myObjects) {
                // Notify the state machine of the event
                sMachine.pushEvent(new PrivacyEvent(e.toString()));
            }
        } catch (IOException e) {
            LTSLogger.LOG.error("Unable to open trace file: " + jsonDataFile, e);
            return;
        }

    }
}
