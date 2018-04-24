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
package uk.ac.soton.modelmyprivacy.tests.dfd;

import java.io.IOException;
import java.nio.charset.Charset;
import uk.ac.soton.itinnovation.modelmyprivacy.DataFlowDiagram.FlowDiagram;
import uk.ac.soton.itinnovation.modelmyprivacy.utils.FileUtils;

/**
 * Test: Create a simple data flow model from the XML specification.
 * Display: the list of role, the list of data records, a visual display of the
 * data flow model.
 *
 */

public class DFDTest {

    private final static String FILENAME = "dataflows/simpleflow.xml";
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

       /**
        * Build the data flow diagram in memory
        */
       FlowDiagram fDiag = new FlowDiagram();
       fDiag.buildDataFlowDiagram(sMachine);


    }
}
