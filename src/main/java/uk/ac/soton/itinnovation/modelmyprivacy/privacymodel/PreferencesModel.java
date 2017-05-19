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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Methods to build and analyse User Preferences.
 *
 * @author Paul Grace
 */
public class PreferencesModel {

    /**
     * The model is a tree structure of tables representing user preferences.
     */



    /**
     * Load a user's preference from a Json string.
     * @param jsonContent The preferences in Json string format
     * @return The array of user preferences for the user.
     * @throws uk.ac.soton.itinnovation.modelmyprivacy.privacymodel.InvalidJSONException
     */
    public List<UserPreference> loadPrefsFromJsonString(String jsonContent)
            throws InvalidJSONException{
        ArrayList<UserPreference> userPrefs = new ArrayList<>();

        // Create a jackson mapper to read the json into a Java object.
        ObjectMapper mapper = new ObjectMapper();

        try {
            userPrefs = mapper.readValue(jsonContent, new TypeReference<List<UserPreference>>() {});
        } catch (IOException ex) {
            System.err.println("Error reading List<UserPreference> from json string: " + ex.getLocalizedMessage());
            throw new InvalidJSONException("JSON content is invalid - it does not match the schema of a UserPreference");
        }
        return userPrefs;
    }

    public GraphPreference findData(String label, GraphPreference node){
        /**
         * Search the tree. Found the node
         */
        if(node.getData().equalsIgnoreCase(label)){
            return node;
        }
        if(!node.getChildren().isEmpty()){
            for(GraphPreference indexNode: node.getChildren()) {
                GraphPreference temp =  findData(label, indexNode);
                if (temp != null) {
                    return temp;
                }
            }
        }
        return null;
    }

    public List<UserPreference> findPreference(String data, String Category, List<UserPreference> preferences) {

        List<UserPreference> prefsRes = new ArrayList<>();

        for(UserPreference indexPref: preferences) {
            if(indexPref.getInformationtype().equalsIgnoreCase(data) && indexPref.getCategory().equalsIgnoreCase(Category)) {
                prefsRes.add(indexPref);
            }
        }

        return prefsRes;
    }

    public List<String> listCategories(List<UserPreference> preferences){
        List<String> categories = new ArrayList<>();

        for(UserPreference indexPref: preferences) {
            String Category = indexPref.getCategory();
            if(!categories.contains(Category)){
                categories.add(Category);
            }
        }
        categories.remove("ANY");
        return categories;
    }

    public List<String> listDataCategories(String categoryFind, List<UserPreference> preferences){
        List<String> dataTypes = new ArrayList<>();

        for(UserPreference indexPref: preferences) {
            if(indexPref.getCategory().equalsIgnoreCase(categoryFind)){
                if(!dataTypes.contains(indexPref.getInformationtype())){
                    dataTypes.add(indexPref.getInformationtype());
                }
            }
        }
        dataTypes.remove("ANY");
        return dataTypes;
    }

    /**
     * Load a user's preference from a Json string.
     * @param load
     * @return The array of user preferences for the user.
     */
    public PreferenceTree buildModel(List<UserPreference> load) {
        PreferenceTree model = new PreferenceTree("ANY");

        /**
         * There are only 3 levels to the tree. First find the root node and
         * insert that at top level.
         */
        List<UserPreference> rt =  findPreference("ANY", "ANY", load);
        model.loadPreferences(rt);

        /**
         * Fill the categories.
         */
        List<String> listCategories = listCategories(load);
        for(String category: listCategories) {
            PreferenceNode child = new PreferenceNode(category);
            model.addChild(child);
            List<UserPreference> catPrefs =  findPreference("ANY", category, load);
            child.loadPreferences(catPrefs);
            List<String> listData = listDataCategories(category, load);
            for(String data: listData) {
                PreferenceNode childData = new PreferenceNode(data);
                child.addChild(childData);
                List<UserPreference> dPrefs =  findPreference(data, category, load);
                childData.loadPreferences(dPrefs);
            }
        }
        return model;

    }
}
