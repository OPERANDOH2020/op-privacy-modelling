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
package uk.ac.soton.itinnovation.modelmyprivacy.lts;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * A role is the specification of a type of person performing actions
 * in a given context. This context gives them explicit or implicit
 * permissions to perform actions on data.
 *
 * A role also bestows trust based on the user's perception.
 */
public class Role {
    /**
     * The string name identifier for a given role. This can be used to both
     * identify the class, and also for display purposes.
     */
    private final String roleIdentity;

    /**
     * Category. Reference to a category of person this role belongs to. Could
     * be a fixed set of categories in a model (healthcare professional, admin}
     * or from an ontology (URL ref).
     */
    private final String roleCategory;

    /**
     * URL reference to the category definition of this role.
     */
    private String reference = null;

    /**
     * Roles are not dynamic. Hence, the constructor creates the Role object
     * which is fixed for the duration of the model usage.
     * @param id The role name identifier.
     * @param category The category of the role
     * @throws uk.ac.soton.itinnovation.modelmyprivacy.lts.InvalidRoleException
     */
    public Role(String id, String category) throws InvalidRoleException{
        if((id==null) || (category==null)) {
            throw new InvalidRoleException("Id or category cannot be null");
        }
        this.roleIdentity = id;
        this.roleCategory = category;
    }

    /**
     * Constructor for an ontology classified role.
     * @param id The role name identifier.
     * @param category The category of the role
     * @param reference The reference to the base ontology
     * @throws uk.ac.soton.itinnovation.modelmyprivacy.lts.InvalidRoleException
     */
    public Role(String id, String category, String reference) throws InvalidRoleException {
        this(id, category);

        try {
            /**
             * Check the category is a URL
             */
            URL url = new URL(reference);
            this.reference = reference;
        } catch (MalformedURLException ex) {
            throw new InvalidRoleException("The ontology category must be a fully qualified URL");
        }
    }

    /**
     * Getter for Identity name field.
     * @return The string identity of the role.
     */
    public String getRoleIdentity(){
        return this.roleIdentity;
    }

    /**
     * Getter for Category name field.
     * @return The string category of the role.
     */
    public String getRoleCategory(){
        return this.roleCategory;
    }

    /**
     * Override the toString method for displaying role information.
     * @return The string description of the role.
     */
    @Override
    public String toString() {
        StringBuilder roleString = new StringBuilder();
        roleString.append("{ \n \t \"roleid\": \"").append(this.roleIdentity).append("\",");
        roleString.append("\n \t \"rolecategory\": \"").append(this.roleCategory).append("\"");
        roleString.append("\n }");
        return roleString.toString();
    }

    /**
     * Display helper method to output role information for the list of roles
     * passed as a parameter.
     * @param roles The list of roles to display
     */
    public static void printRoles(List<Role> roles) {
        System.out.println("[");
        for(int i=0; i< roles.size(); i++) {
            System.out.print(roles.get(i).toString());

            if(i!=roles.size()-1){
                System.out.print(",\n");
            }
        }
        System.out.println("]");
    }
}
