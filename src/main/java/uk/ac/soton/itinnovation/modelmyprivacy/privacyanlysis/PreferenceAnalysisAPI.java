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

import uk.ac.soton.itinnovation.modelmyprivacy.lts.Transition;
import uk.ac.soton.itinnovation.modelmyprivacy.privacymodel.PreferenceTree;

/**
 * The library API for performing evaluations of privacy actions against
 * the defined user preferences.
 */
public interface PreferenceAnalysisAPI {

    /**
     * Get the privacy score from the preference model
     * @param t Transition action
     * @param prefs The preference model of an individual user
     * @return A score between 0 and 10.
     */
    public double privacyScore(Transition t, PreferenceTree prefs);

     /**
     * Get the privacy score from the preference model
     * @param action The action string e.g. read, disclose etc.
     * @param role The role of the requester
     * @param purpose The legal reason for performing an action.
     * @param data The type of data e.g. field name, data category etc.
     * @param prefs The preference model of an individual user
     * @return A score between 0 and 10.
     */
    public double privacyScore(String action, String role, String purpose, String data, PreferenceTree prefs);

    /**
     * Return a yes/no statement that determines if the transition matches the
     * user preferences.
     * @param t Transition action
     * @param prefs The preference model of an individual user
     * @return True if the preferences allow the transition.
     */
    public boolean classifyTransition(Transition t, PreferenceTree prefs);

    /**
     * Return a yes/no statement that determines if the transition matches the
     * user preferences.
     * @param action The action string e.g. read, disclose etc.
     * @param role The role of the requester
     * @param purpose The legal reason for performing an action.
     * @param data The type of data e.g. field name, data category etc.
     * @param prefs The preference model of an individual user
     * @return True if the preferences allow the transition.
     */
    public boolean classifyTransition(String action, String role, String purpose, String data, PreferenceTree prefs);

    /**
     * Build a user preferences model from the entries in a json string.
     * @param jsonModel The json array of preferences
     * @return The tree model of the user preferences.
     */
    PreferenceTree buildPreferences(String jsonModel);

}
