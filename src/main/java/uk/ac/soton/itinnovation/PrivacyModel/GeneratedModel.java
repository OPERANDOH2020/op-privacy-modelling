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
package uk.ac.soton.itinnovation.PrivacyModel;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.Field;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.InvalidStateMachineException;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.InvalidTransitionException;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.Role;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.State;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.StateMachine;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.StateNode;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.StateVariable;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.Transition;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyevents.Guard;
import uk.ac.soton.itinnovation.modelmyprivacy.privacyevents.InvalidGuard;
/**
 * The set of methods for generating the privacy model based
 * upon two inputs:
 * 1) The dataflow model (including the roles and data)
 * 2) The access control policies on data.
 */
public class GeneratedModel {



//    private void generatePolicyTransition(State node, Map<String, State> states) {
//        /**
//         * For each can state variable - create a node and transition
//         */
//        StateNode s = new StateNode("", StateNode.StateType.NORMAL, this);
//        s.initialiseStateVariables(stateRoles, fieldRules);
//        /**
//         * calculate the can fields for the state variables
//         */
//        for(Field f: fieldRules){
//            for(Role r: this.stateRoles){
//                boolean can = policies.canAccess(f.getName(), r.getRoleIdentity());
//                s.changeStateVariable(f.getName(), r.getRoleIdentity(), can);
//            }
//        }
//
//    }

     /**
     * Create a state in the model based upon the create action in the
     * transition, and then generate the transitions that could lead to this
     * generated state.
     * @param dataFlowTransition
     * @param previousGeneratedState
     * @return
     */
    private State generateStateFromTable(Table<String, String, StateVariable> clonedTable,
            StateNode previousGeneratedState, StateMachine sm) throws InvalidStateMachineException {

        StateNode automatedNode  = new StateNode(clonedTable.toString(), State.StateType.END, sm);

        /**
         * Copy the state variables from the previous node
         */
        automatedNode.overwriteStateVariables(previousGeneratedState.getStateMachine().getRoles(),
                previousGeneratedState.getStateMachine().getData(), clonedTable);

        return automatedNode;
    }

    /**
     * Create a state in the model based upon the create action in the
     * transition, and then generate the transitions that could lead to this
     * generated state.
     * @param dataFlowTransition
     * @param previousGeneratedState
     * @return
     */
    private State generateStateFromCreateAction(Transition dataFlowTransition,
            StateNode previousGeneratedState, StateMachine sm) throws InvalidStateMachineException {

        StateNode createNode  = (StateNode) sm.getState(dataFlowTransition.readToLabel());

        /**
         * Copy the state variables from the previous node
         */
        createNode.overwriteStateVariables(previousGeneratedState.getStateMachine().getRoles(),
                previousGeneratedState.getStateMachine().getData(), previousGeneratedState.getStateVariables());

        /**
         * Update the stateVariables with the new created field permisions.
         * Currently assume it is a single field. To do - change to record with
         * n fields.
         */
        String fieldName = dataFlowTransition.getGuards().getData();

        // Get the permissions for the field.
        List<String> canAccess = sm.getAccessPolicies().canAccessField(fieldName);
        for(String allowedRole: canAccess){
            createNode.changeStateVariable(allowedRole, fieldName, true);
        }

        return createNode;
    }

    /**
     * Perform a pre order traversal of the data flow directed graph. As
     * it is a data flow model we assume there are no loops. If a loop is
     * mistakenly input then this traversal method will fail.
     *
     * @param node The start node of the data flow model
     * @param states The map to store the created states in.
     */
    private void preorderTraversal(State node, StateMachine dataFlowModel) throws InvalidStateMachineException {
        // Display the label of the data flow node current position
        System.out.println(node.getLabel());

        // Iterate over the outgoing transitions
        Iterator iter = node.getTransitions().iterator();
        while (iter.hasNext()) {
            Transition toProduce = (Transition) iter.next();
            /**
             * Based on the transition generate the automated states.
             */
            String action = toProduce.getGuards().getAction();
            switch (action) {
                case "create":
                    State createdNode = generateStateFromCreateAction(toProduce, (StateNode) node, dataFlowModel);

                    break;
                case "access":
                    // change the seen state variables of nodes that exist
            }
            preorderTraversal(dataFlowModel.getState(toProduce.readToLabel()), dataFlowModel);
        }
    }

    private List<StateNode> getNodesCanButNotSeen(String role, String field, StateMachine sm) {
        List<StateNode> foundStates = new ArrayList<>();
        List<State> allStates = sm.getStates();
        for(State sIndex: allStates) {
            StateVariable get = ((StateNode) sIndex).getStateVariables().get(role, field);
            if(get.getCanRead() && !get.getSeen()) {
                foundStates.add((StateNode) sIndex);
            }
        }
        return foundStates;
    }

    private StateNode getNodeMatch(Table<String, String, StateVariable> input, StateMachine sm) {
        String matchLabel = input.toString();
        List<State> allStates = sm.getStates();
        for(State sIndex: allStates) {
            if(((StateNode) sIndex).getStateVariables().toString().equalsIgnoreCase(matchLabel))
                return ((StateNode) sIndex);
        }
        return null;
    }

    private Table<String, String, StateVariable> cloneStateVariables(Table<String, String, StateVariable> input) {
        Table<String, String, StateVariable> clonedTable = HashBasedTable.create();
        for(String r: input.rowKeySet()) {
            for(String f: input.columnKeySet()) {
                StateVariable newVal = new StateVariable(r, f);
                newVal.setCanRead(input.get(r, f).getCanRead());
                newVal.setSeen(input.get(r, f).getSeen());
                clonedTable.put(r, f, newVal);
            }
        }
        return clonedTable;
    }

    /**
     * Primary method to call to generate a first version of the model
     * @param dataFlowModel The created data flow model from the initial specification.
     * @throws uk.ac.soton.itinnovation.modelmyprivacy.lts.InvalidStateMachineException
     */
    public void generateStates(StateMachine dataFlowModel) throws InvalidStateMachineException{

        /**
         * Create the start state. It is completely private so all
         * state variables are set to false.
         */
        StateNode startNode = (StateNode) dataFlowModel.getStartState();
        startNode.initialiseStateVariables(dataFlowModel.getRoles(), dataFlowModel.getData());

        /**
         * We now traverse the dataflow model and build the new states
         */
        preorderTraversal(startNode, dataFlowModel);

        /**
         * Add potential transition
         */
        for(int i=0; i<2; i++) {
            for(Role r: dataFlowModel.getRoles()) {
                for(Field f: dataFlowModel.getData()) {
                    List<StateNode> targetNodes = getNodesCanButNotSeen(r.getRoleIdentity(), f.getName(), dataFlowModel);
                    /**
                     * For each node - find the node where the same variables plus seen exist
                     */
                    for(StateNode toTest: targetNodes) {
                        Table<String, String, StateVariable> stateVariables = toTest.getStateVariables();
                        Table<String, String, StateVariable> clonedTable = cloneStateVariables(stateVariables);
                        clonedTable.get(r.getRoleIdentity(), f.getName()).setSeen(true);

                        /**
                         * Find matching node in the architecture
                         */
                        StateNode foundMatch= getNodeMatch(clonedTable, dataFlowModel);
                        if(foundMatch != null) {
                            // create a new transition between the two states (if the transition isn't there)
                            System.out.println("******************");
                            boolean found = false;
                            for(Transition existingT: toTest.getTransitions()) {
                                if(existingT.readToLabel().equalsIgnoreCase(foundMatch.getLabel())){
                                    found = true;
                                    break;
                                }
                            }
                            if(!found) {
                                Guard newGuard;
                                try {
                                    newGuard = new Guard(r.getRoleIdentity(), "access", f.getName(), "undefined purpose");
                                    Transition t = new Transition(foundMatch.getLabel(), toTest.getLabel(), newGuard);
                                    toTest.addToTransition(t, dataFlowModel.getSMStates());
                                } catch (InvalidGuard ex) {
                                    ex.printStackTrace();
                                } catch (InvalidTransitionException ex) {
                                    Logger.getLogger(GeneratedModel.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                        else {
                            // create the node and add a transition
                            State createdNode = generateStateFromTable(clonedTable, toTest, dataFlowModel);
                            dataFlowModel.addState(createdNode);
                            // Create a transition
                            Guard newGuard;
                            try {
                                newGuard = new Guard(r.getRoleIdentity(), "access", f.getName(), "undefined purpose");
                                Transition t = new Transition(createdNode.getLabel(), toTest.getLabel(), newGuard);
                                toTest.addToTransition(t, dataFlowModel.getSMStates());
                            } catch (InvalidGuard ex) {
                                ex.printStackTrace();
                            } catch (InvalidTransitionException ex) {
                                Logger.getLogger(GeneratedModel.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    }
                }
            }
        }
    }


}
