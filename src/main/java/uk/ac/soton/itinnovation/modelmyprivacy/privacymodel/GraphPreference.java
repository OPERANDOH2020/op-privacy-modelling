/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.soton.itinnovation.modelmyprivacy.privacymodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author pjg
 */
public abstract class GraphPreference implements GetPreference{

    /**
     * Each node in the graph, can have N children
     */
    private final List<GraphPreference> children;
    public List<GraphPreference> getChildren() {
        return this.children;
    }

    /**
     * One list of preferences.
     */
    private List<UserPreference> nodePreferences;

    /**
     * The list is hashed three times for querying of preferences.
     */
    private final HashMap<String, List<UserPreference>> actionHashMap;
    private final HashMap<String, List<UserPreference>> roleHashMap;
    private final HashMap<String, List<UserPreference>> purposeHashMap;

    /**
     * The identifier of the node in the search graph. This is the category
     * or information type.
     */
    private final String nodeLabel;

    /**
     * The preference of this label
     */
    private int preference;

    public GraphPreference(String dataType) {
        actionHashMap = new HashMap<>();
        roleHashMap = new HashMap<>();
        purposeHashMap = new HashMap<>();
        nodeLabel = dataType;
        children = new ArrayList<>();
    }

    @Override
    public String getData() {
        return this.nodeLabel;
    }

    protected void loadPreferences(List<UserPreference> model) {

        this.nodePreferences = model;

        /**
         * Search the model for the relevant preference category.
         */
        for (UserPreference pref: model) {
            if((pref.getInformationtype().equalsIgnoreCase(nodeLabel))||(pref.getCategory().equalsIgnoreCase(nodeLabel))){
                this.hashMapPreference(pref);
            }
        }
    }

    /**
     * Generic method to get a list of preferences hashed on a particular index.
     * @param key The key identifier e.g. doctor could be a key of Role
     * @param map The index map e.g. the role map is the collection of role prefs.
     * @return The list of all preferences that match the key in the given index.
     */
    protected List<UserPreference> getMapPreference(String key, HashMap<String, List<UserPreference>> map){
        List<UserPreference> mapPrefs = map.get(key);
        if(mapPrefs == null) {
            map.put(key, new ArrayList<UserPreference>());
            return map.get(key);
        }
        return mapPrefs;
    }

    /**
     * Hash the same preference three times, so that it can be found on multiple
     * lookups.
     * @param preference The preference to be hashed.
     */
    protected void hashMapPreference(UserPreference preference){
        // Index the role
        String role = preference.getRole();
        List<UserPreference> rolePrefs = getMapPreference(role, roleHashMap);
        rolePrefs.add(preference);

        // Index the action
        String action = preference.getAction();
        List<UserPreference> actionPrefs = getMapPreference(action, actionHashMap);
        actionPrefs.add(preference);

        // Index the purpose
        String purpose = preference.getPurpose();
        List<UserPreference> purpPrefs = getMapPreference(purpose, purposeHashMap);
        purpPrefs.add(preference);
    }

    /**
     * Add a child node to this graph node. The root node has N category
     * nodes. Each Category node has N data type nodes.
     * @param preferenceNode The node to add. It will be unique on label.
     * @return true if there is no labelled child, false if the node label exists.
     */
    public boolean addChild(GraphPreference preferenceNode){
        for(GraphPreference indexNode: this.children) {
            if(indexNode.getData().equalsIgnoreCase(preferenceNode.getData())) {
                return false;
            }
        }
        return this.children.add(preferenceNode);
    }

    /**
     * Delete a child node from this graph node. The root node has N category
     * nodes. Each Category node has N data type nodes.
     * @param label The node to remove. It will be the unique label.
     * @return true if there found and deleted, false if the node label doesn't exist.
     */
    public boolean deleteChild(String label){
        for(GraphPreference indexNode: this.children) {
            if(indexNode.getData().equalsIgnoreCase(label)) {
                return this.children.remove(indexNode);
            }
        }
        return false;
    }

    /**
     * Delete a child node from this graph node. The root node has N category
     * nodes. Each Category node has N data type nodes.
     * @param label The node to update. It will be the unique label.
     * @param updateNode. The content to change
     * @return true if there found and updated, false if the node label doesn't exist.
     */
    public boolean updateChild(String label, GraphPreference updateNode){
        if(!updateNode.getData().equalsIgnoreCase(label)) {
            return false;
        }
        for(GraphPreference indexNode: this.children) {
            if(indexNode.getData().equalsIgnoreCase(label)) {
                if(this.children.remove(indexNode)) {
                    return this.children.add(updateNode);
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Return the current list of all preferences registered at this node in
     * the model.
     * @return
     */
     @Override
    public List<UserPreference> getPreferenceList() {
        return this.nodePreferences;
    }

    @Override
    public List<UserPreference> getPreferenceListFromRole(String role) {
        List<UserPreference> uPrefs = this.roleHashMap.get(role);
        if(uPrefs == null){
            uPrefs = this.roleHashMap.get("ANY");
            if (uPrefs == null) {
                return new ArrayList<UserPreference>();
            }
        }
        return uPrefs;
    }

    @Override
    public List<UserPreference> getPreferenceListPurpose(String purpose) {
        List<UserPreference> uPrefs = this.purposeHashMap.get(purpose);
        if(uPrefs == null){
            uPrefs = this.purposeHashMap.get("ANY");
            if (uPrefs == null) {
                return new ArrayList<UserPreference>();
            }
        }
        return uPrefs;
    }

    @Override
    public List<UserPreference> getPreferenceListAction(String action) {

        List<UserPreference> uPrefs = this.actionHashMap.get(action);
        if(uPrefs == null){
            uPrefs = this.actionHashMap.get("ANY");
            if (uPrefs == null) {
                return new ArrayList<UserPreference>();
            }
        }
        return uPrefs;
    }

    @Override
    public int getPreference() {
       return this.preference;
    }

    @Override
    public void setPreference(int pref){
        this.preference = pref;
    }

}
