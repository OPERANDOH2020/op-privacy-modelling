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

import java.io.IOException;
import java.nio.charset.Charset;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.FileUtils;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.StateMachine;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.TraceGenerator;


/**
 *
 * @author pjg
 */
public class PatternExecutionTest {


    /**
     * Test a given patterns executes correctly. This is a compliance test.
     */
    public final void PatternTest() {
        try {
            final String sMachine = FileUtils.readFile("HelloWorld.xml", Charset.defaultCharset());

                 StateMachine stateMachine = new StateMachine();
                 stateMachine.buildStates(sMachine);

            /*
             * Start the model monitoring
             */
            TraceGenerator tG = new TraceGenerator();
            tG.generateEvents("ServiceTrace.json", stateMachine);
            final String Report = stateMachine.start();
            System.out.println(Report);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
     }

    public static void main(String[] args) {
        PatternExecutionTest tests = new PatternExecutionTest();
        tests.PatternTest();
    }
}
