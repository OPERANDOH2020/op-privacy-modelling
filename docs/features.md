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

[viz3]: src/main/resources/images/visual3.png "Data flow visualisation"