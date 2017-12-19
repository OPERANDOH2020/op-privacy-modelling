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

import java.util.ArrayList;
import java.util.List;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.Field;
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


    public static void generateEndFields(List<Field> recordStructure, List<Field> outputNodes) {
        for (Field f: recordStructure) {
            if(f.getRecord()) {
                generateEndFields(f.getRecordField(), outputNodes);
            }
            else {
                outputNodes.add(f);
            }
        }
    }

    /**
     * For a data field return the list of categories attached to it.
     * @param dataID The field name id.
     * @param model The model the field is specified in.
     * @return The list of categories.
     */
    public static List<String> getCategory(String dataID, StateMachine model) {
        List<String> categories = new ArrayList<>();

        List<Field> outputFields = new ArrayList<>();
        generateEndFields(model.getData(), outputFields);
        for(Field f: outputFields) {
            if(f.getName().equalsIgnoreCase(dataID))
                return f.getCategory();
        }
        return categories;
    }

    /**
     * Attach the data category value to each transition is the model.
     * @param model
     * @throws InvalidStateMachineException
     */
    public static void annotateCategoryData(StateMachine model) throws InvalidStateMachineException {

        List<Transition> transitions = new ArrayList<>();
        StateMachine.getTransitions(model.getStartState(), model, transitions);
        for (Transition t : transitions) {
            List<String> category = getCategory(t.getLabel().getData(), model);
            if(category.size() > 0) {
                t.getLabel().setAttribute("category", category.get(0));
            }
        }
    }

    public static void annotatePrivacyPreferences(String userInput, StateMachine model) throws InvalidStateMachineException {
        String prefsModel = FileUtils.readJsonFromFile(userInput);
        PreferenceAnalysisAPI prefsAPI = new PreferenceAnalysis();
        PreferenceTree userPrefs = prefsAPI.buildPreferences(prefsModel);

        List<Transition> transitions = new ArrayList<>();
        StateMachine.getTransitions(model.getStartState(), model, transitions);
        for (Transition t : transitions) {
            double privacyScore = prefsAPI.privacyScore(t, userPrefs);
            System.out.println(t.getLabel().getRole() + ":" + privacyScore);
            t.getLabel().setAttribute("privacy", privacyScore);
        }
    }

}
