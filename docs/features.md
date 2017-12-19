## Privacy Annotation
In this section we illustrate how privacy preference information is attached
to each transition in the LTS. This approach can then be followed to add
additional annotation to any transition.

```java
public static void main(String[] args) {
        String sMachine = null;

        /**
         * Get the data flow XML model document
         */
        try {
            sMachine = FileUtils.readFile(FILENAME, Charset.defaultCharset());
        } catch (IOException ex) {
            System.err.println("Error Reading file" +  FILENAME);
            System.err.println("Error:" +  ex.getMessage());
        }

        /**
         * Build a data flow directed graph and visualize
         */
        StateMachine stateMachine = new StateMachine();
        stateMachine.buildDataFlowLTS(sMachine);

        /**
         * Add the access policies
         */
        stateMachine.addAccessPolicies("unittests/twoDataFields.json");
        AccessPolicyModelGeneration gModel = new AccessPolicyModelGeneration();
        try {
            gModel.generateStates(stateMachine);
            ModelAnalysis.annotateCategoryData(stateMachine);
            ModelAnalysis.annotatePrivacyPreferences("unittests/prefs.json", stateMachine);
            stateMachine.visualiseAnnotatedGraph();
        } catch (InvalidStateMachineException ex) {
            Logger.getLogger(SimpleDataFlowModel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
```

This produces the following annotated LTS:

![Data flow visualisation output][viz3] 

## Annotating Transitions
The process to annotate a transition with any value is straightforward. While
there are certain fixed values for a transition (action, role, etc.) the approach is 
flexible to add a label and a value (via setAttribute and getAttribute methods).

The following is code to attach category information about the data fields; the code
calls getTransitions to return all transitions in the state machine (although
a traversal algorithm could also be used).

```java
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
```

The following is then similar code to attach the calculated privacy preferences
for a given user into the model.

```java
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
```
[viz3]: src/main/resources/images/visual3.png "Data flow visualisation"