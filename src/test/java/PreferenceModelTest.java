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
import uk.ac.soton.itinnovation.modelmyprivacy.privacyanlysis.PreferenceAnalysis;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyanlysis.PreferenceAnalysisAPI;
import uk.ac.soton.itinnovation.modelmyprivacy.privacymodel.PreferenceTree;



public class PreferenceModelTest {

    private static final String INPUT = "prefs.json";

    public static void main(String[] args) {
        // Initialise the configuration
        PreferenceModelTest tests = new PreferenceModelTest();
        TestHelperMethods tt = new TestHelperMethods();

        // Load the json inputs for the user preferences
        String prefsModel = tt.readJsonFromFile(INPUT);
        PreferenceAnalysisAPI prefsAPI = new PreferenceAnalysis();
        PreferenceTree userPrefs = prefsAPI.buildPreferences(prefsModel);

        // Analyse action: doctor read weight for medical
        System.out.println("Privacy Score - " + prefsAPI.privacyScore("access", "health_care_professional", "Primary Care", "weight", userPrefs));
        System.out.println("Agree - " + prefsAPI.classifyTransition("access", "health_care_professional", "Primary Care", "weight", userPrefs));

        System.out.println("Privacy Score - " + prefsAPI.privacyScore("disclose", "health_care_professional", "Primary Care", "weight", userPrefs));
        System.out.println("Agree - " + prefsAPI.classifyTransition("disclose", "health_care_professional", "Primary Care", "weight", userPrefs));

        System.out.println("Privacy Score - " + prefsAPI.privacyScore("access", "admin", "administration", "weight", userPrefs));
        System.out.println("Agree - " + prefsAPI.classifyTransition("access", "admin", "administration", "weight", userPrefs));

        System.out.println("Privacy Score - " + prefsAPI.privacyScore("access", "admin", "Primary Care", "weight", userPrefs));
        System.out.println("Agree - " + prefsAPI.classifyTransition("access", "admin", "Primary Care", "weight", userPrefs));

        System.out.println("Privacy Score - " + prefsAPI.privacyScore("access", "WebSite", "Marketing", "weight", userPrefs));
        System.out.println("Agree - " + prefsAPI.classifyTransition("access", "WebSite", "Marketing", "weight", userPrefs));

    }
}
