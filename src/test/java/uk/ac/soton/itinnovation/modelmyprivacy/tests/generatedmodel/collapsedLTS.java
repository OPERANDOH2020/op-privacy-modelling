package uk.ac.soton.itinnovation.modelmyprivacy.tests.generatedmodel;

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

import uk.ac.soton.itinnovation.modelmyprivacy.tests.dataflowmodel.SimpleDataFlowModel;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.Field;
import uk.ac.soton.itinnovation.modelmyprivacy.utils.FileUtils;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.Role;
import uk.ac.soton.itinnovation.modelmyprivacy.dataflowmodel.XMLDocument;
import uk.ac.soton.itinnovation.modelmyprivacy.dataflowmodel.XMLStateMachine;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.InvalidStateMachineException;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.StateMachine;
import uk.ac.soton.itinnovation.modelmyprivacy.modelgeneration.AccessPolicyModelGeneration;

/**
 * Test class to test the creation of a data flow model from the XML specification.
 */

public class collapsedLTS {

    private final static String FILENAME = "unittests/collapse.xml";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String sMachine = null;

        /**
         * Get the data flow XML model document
         */
        try {
            sMachine = FileUtils.readFile(FILENAME, Charset.defaultCharset());
        } catch (IOException ex) {
            System.err.println("Error Reading file" +  FILENAME);
            System.err.println("Error:" +  ex.getMessage());
        }
        final Document model = XMLDocument.jDomReadXmlStream(
                    new ByteArrayInputStream(sMachine.getBytes(StandardCharsets.UTF_8)));

        /**
         * View the roles in the data flow model
         */
        try {
            List<Role> roles = XMLStateMachine.addRoles(model.getRootElement());
            Role.printRoles(roles);
        } catch (InvalidStateMachineException ex) {
            System.err.println("Error adding roles to the model:" +  ex.getMessage());
        }

        /**
         * View the data in the data flow model
         */
        try {
            List<Field> data = XMLStateMachine.addRecords(model.getRootElement());
            Field.printData(data);
        } catch (InvalidStateMachineException ex) {
            System.err.println("Error adding roles to the model:" +  ex.getMessage());
        }

        /**
         * Build a data flow directed graph and visualize
         */
        StateMachine stateMachine = new StateMachine();
        stateMachine.buildDataFlowLTS(sMachine);
//        stateMachine.visualiseDataFlowGraph();
        /**
         * Add the access policies
         */
        stateMachine.addAccessPolicies("unittests/collapse.json");
        AccessPolicyModelGeneration gModel = new AccessPolicyModelGeneration();
        try {
            gModel.generateStates(stateMachine);
            stateMachine.visualiseAutomatedGraph(true);
            stateMachine.visualiseAutomatedGraph(false);
        } catch (InvalidStateMachineException ex) {
            Logger.getLogger(SimpleDataFlowModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
