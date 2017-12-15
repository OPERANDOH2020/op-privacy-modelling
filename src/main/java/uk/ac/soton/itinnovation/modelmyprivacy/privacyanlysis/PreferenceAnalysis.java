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

package uk.ac.soton.itinnovation.modelmyprivacy.privacyanlysis;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.Transition;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyevents.InvalidTransitionLabel;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyevents.TransitionLabel;
import uk.ac.soton.itinnovation.modelmyprivacy.privacymodel.GraphPreference;
import uk.ac.soton.itinnovation.modelmyprivacy.privacymodel.InvalidJSONException;
import uk.ac.soton.itinnovation.modelmyprivacy.privacymodel.PreferenceTree;
import uk.ac.soton.itinnovation.modelmyprivacy.privacymodel.PreferencesModel;
import uk.ac.soton.itinnovation.modelmyprivacy.privacymodel.UserPreference;

/**
 *
 * @author pjg
 */
public class PreferenceAnalysis implements PreferenceAnalysisAPI{

    private static double VARIATION = 2.0;
    private static double FUNDAMENTALIST = 8.0;
    private static double UNCONCERNED = 2.0;


    public double getMean(List<Integer> numberList) {
        int total = 0;
        for (int d: numberList) {
            total += d;
        }
        return total / (numberList.size());
    }

    public double getRange(List<Integer> numberList) {
        int initMin = numberList.get(0);
        int initMax = numberList.get(0);
        if(numberList.size()>1){
            for (int i = 1; i < numberList.size(); i++) {
                if (numberList.get(i) < initMin) initMin = numberList.get(i);
                if (numberList.get(i) > initMax) initMax = numberList.get(i);
            }
            return initMax - initMin;
        }
        return initMax;
    }

    public boolean classifyTransition(Transition t, PreferenceTree prefs){
        double score = privacyWeight(t, prefs);
        if(score<UNCONCERNED+1){
            return true;
        }
        else
            return false;
    }

    public double privacyWeight(Transition t, PreferenceTree prefs){

        double score = privacyScore(t, prefs);
        if(score>FUNDAMENTALIST)
            return score;

        TransitionLabel information  = t.getLabel();
        double risk = information.getRisk();
        return risk * score;
    }

    /**
     * Get the privacy score from the preference model
     * @param t
     * @param prefs
     * @return
     */
    public double privacyScore(Transition t, PreferenceTree prefs){
        TransitionLabel information  = t.getLabel();
        PreferencesModel model = new PreferencesModel();

        String category = (String) information.getAttrribute("category");
        /**
         * First - Get the data category
         */
        GraphPreference dataNode = model.findData(category, prefs);
        /**
         * If there is no data category return the westin classification.
         */
        if(dataNode == null) {
            return new Integer(prefs.getPreferenceList().get(0).getPreference());
        }

        // Search for an exact match: InformationType, Action, Role, Purpose
        List<UserPreference> preferenceListFromRole = dataNode.getPreferenceListFromRole(information.getRole());
        if(preferenceListFromRole != null) {
            for(UserPreference index: preferenceListFromRole){
                if(index.getAction().equalsIgnoreCase(information.getAction())) {
                    if(index.getPurpose().equalsIgnoreCase(information.getPurpose()))
                        return new Integer(index.getPreference());
                }
            }
        }

        /**
         * If the weights for other actions for this data are similar. Then return
         * the median.
         */
        if(preferenceListFromRole != null) {
            if(preferenceListFromRole.size()>0) {
                List<Integer> preferences = new ArrayList<>();
                for(UserPreference index: preferenceListFromRole){
                    preferences.add(new Integer(index.getPreference()));
                }
                if(getRange(preferences) <= VARIATION) {
                    return getMean(preferences);
                }
            }
        }

        /**
         * If a score has not been returned then we should move up the tree
         *
         */
        GraphPreference categoryNode = null;
        List<GraphPreference> children = prefs.getChildren();
        for(GraphPreference child: children) {
            if(child.getChildren().contains(dataNode)){
                categoryNode = child;
                break;
            }
        }
        if(categoryNode == null) {
            return new Integer(prefs.getPreferenceList().get(0).getPreference());
        }

        // Search for an exact match: InformationType, Action, Role, Purpose
        preferenceListFromRole = categoryNode.getPreferenceListFromRole(information.getRole());

        for(UserPreference index: preferenceListFromRole){
            if(index.getAction().equalsIgnoreCase(information.getAction()) || index.getAction().equalsIgnoreCase("ANY")) {
                if(index.getPurpose().equalsIgnoreCase(information.getPurpose()) || index.getPurpose().equalsIgnoreCase("ANY"))
                    return new Integer(index.getPreference());
            }
        }
        if(preferenceListFromRole.size() >= 3) {
            List<Integer> preferences = new ArrayList<>();
            for(UserPreference index: preferenceListFromRole){
                preferences.add(new Integer(index.getPreference()));
            }
            if(getRange(preferences) <= VARIATION) {
                return getMean(preferences);
            }
        }

        List<UserPreference> preferenceListFromPurpose = categoryNode.getPreferenceListPurpose(information.getPurpose());
        if(preferenceListFromPurpose.size() >= 3) {
            List<Integer> preferences = new ArrayList<>();
            for(UserPreference index: preferenceListFromRole){
                preferences.add(new Integer(index.getPreference()));
            }
            if(getRange(preferences) <= VARIATION) {
                return getMean(preferences);
            }
        }
        return categoryNode.getPreference();
    }

    @Override
    public double privacyScore(String action, String role, String purpose, String data, PreferenceTree prefs) {
        try {
             TransitionLabel guards = new TransitionLabel(role, action, data, purpose);
             Transition tNew = new Transition("Temp", "Temp2", guards );
            return privacyScore(tNew, prefs);
        } catch (InvalidTransitionLabel ex) {
            return 10;
        }
    }

    @Override
    public boolean classifyTransition(String action, String role, String purpose, String data, PreferenceTree prefs) {
        try{
            TransitionLabel guards = new TransitionLabel(role, action, data, purpose);
            Transition tNew = new Transition("Temp", "Temp2" , guards );
            return classifyTransition(tNew, prefs);
        } catch (InvalidTransitionLabel ex) {
            return false;
        }

    }

    @Override
    public PreferenceTree buildPreferences(String jsonModel) {
        try {
            PreferencesModel builder = new PreferencesModel();
            List<UserPreference> prefs = builder.loadPrefsFromJsonString(jsonModel);
            return builder.buildModel(prefs);
        } catch (InvalidJSONException ex) {
            Logger.getLogger(PreferenceAnalysis.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
