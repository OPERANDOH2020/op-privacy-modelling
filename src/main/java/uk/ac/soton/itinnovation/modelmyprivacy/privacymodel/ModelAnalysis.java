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
package uk.ac.soton.itinnovation.modelmyprivacy.privacymodel;

import java.util.List;
import uk.ac.soton.itinnovation.PrivacyModel.GeneratedModel;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.InvalidStateMachineException;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.StateMachine;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.Transition;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyanlysis.PreferenceAnalysis;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyanlysis.PreferenceAnalysisAPI;
import uk.ac.soton.itinnovation.modelmyprivacy.utils.FileUtils;

/**
 *
 * Set of operations to take a LTS model and annotate the transitions with
 * privacy values {user preferences} and allowed actions.
 */
public class ModelAnalysis {

    public static void annotatePrivacyPreferences(String userInput, StateMachine model) throws InvalidStateMachineException {
        String prefsModel = FileUtils.readJsonFromFile(userInput);
        PreferenceAnalysisAPI prefsAPI = new PreferenceAnalysis();
        PreferenceTree userPrefs = prefsAPI.buildPreferences(prefsModel);

        GeneratedModel gm = new GeneratedModel();
        List<Transition> transitions = gm.getTransitions(model.getStartState(), model);
        for(Transition t: transitions){
            double privacyScore = prefsAPI.privacyScore(t, userPrefs);
            t.getLabel().setAttribute("privacy", privacyScore);
        }
    }

}
