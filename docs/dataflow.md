## Tutorial: Modelling a privacy data flow

### Specify the data flow using XML
A data flow is modelled concretely in XML (in the future a GUI tool to specify
models will be made available). We now describe the key steps to fully
model the data flow.

### Step 1: Model the Roles
A role describes the context of a person (or system) that will perform a privacy
action on the personal data.

We define in the XML, the set of all roles in a particular system that we 
are modelling. A role is composed of:
* *name*: The role identifier
* *category*: The category that a role falls into ...
* *reference*: this category may be aligned to an ontology or taxonomy that defines the category (optional).

These elements are then specified in the XML as follows:

```xml
<?xml version="1.0"?>
<Model  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <roles>
        <role>
            <name>doctor</name>
            <category>health_care_professional</category>
            <reference>https://www.w3.org/TR/owl-ref#health_care_professional</reference>
        </role>
        <role>
            <name>nurse</name>
            <category>health_care_professional</category>
            <reference>https://www.w3.org/TR/owl-ref#health_care_professional</reference>
        </role>
    </roles>
```

### Step 2: Model the Data
Data is the set of information that privacy actions will be carried out upon. This
is modelled as a set of records. Where a record is made up of a set of fields (or further
records). A data field is the smallest element a privacy action can be carried out upon. 

We therefore specify data as follows in the XML model as follows. For each data
record we use the <records> tag (there can be N records). Each record then has
the following elements:

* *name*: the id name of the record.
* *categories*: a list of data categories that the record matches with.
* *fields*:  the set of data fields in the record. Where the element could be another record.

Each field in the list is then specified as follows:
* *name*: the field name. Note, to ID a field DOT notation is used e.g. record.field
* *categories*: a list of data categories that the field matches with.
* *type*: this is one of four labels to state the type of personal data. **"EI"** it 
can be considered as an explicit identifier field, or **"QI"** it can be considered to
be a quasi-identifier field, or **"sensitive"** the field contains sensitive (protected)
data.

In the example below, we create a simple record call "patient" with one field
"name"

```xml
    <records>
        <name>patient</name>
        <categories>
            <category>Medical</category>
        </categories>
        <fields>
            <field>
                <name>name</name>
                <categories>
                    <category>Medical</category>
                </categories>
                <type>ei</type>
            </field>
        </fields>
    </records>
```
### Step 3: Model the Data Flow as the LTS

The final step for the XML specification is to model the privacy actions
as a sequence of transtions in an LTS. To model the LTS, the first step is
to define the states, and then subsequently model the transitions from one
state to another. Each state can define one or more transitions to other states.

A state is defined in terms of:
* label: the label of the node to identify transitions to.
* type: either start, normal or end.
* transition: zero or more transitions that describe the privacy actions on the
data and the state label this transitions to.

For example in the XML that follows there is an LTS of two states and one transition
between them. The transition is a create field patient.name (i.e. the value is
inserted into the data record by a doctor).

```xml
    <lts>
        <state>
          <label>service_registration</label>
          <type>start</type>
          <transition>    
                <to>finish</to>
                <action>create</action>
                <role>doctor</role>
                <data>patient.name</data>
                <purpose>Service usage</purpose>
           </transition>
        </state>
        
        <state>
            <label>finish</label>
            <type>end</type>
        </state>
    </lts>
```

### Step 4: Programming with the LTS
The software libraries to work with the code can be used as follows:

Load the XML data model from a file, and then build the LTS statemachine. The
statemachine can then be used for further analysis and visualisation.

```java

    /**
     * Get the data flow XML model document
     */
    try {
        String xml = FileUtils.readFile(FILENAME, Charset.defaultCharset());
        StateMachine stateMachine = new StateMachine();
        stateMachine.buildDataFlowLTS(xml);
         
    } catch (IOException ex) {
        System.err.println("Error Reading file" +  FILENAME);
         System.err.println("Error:" +  ex.getMessage());
    }
```
To visualise the current data flow (using the GraphStream library)

```java

    stateMachine.visualiseDataFlowGraph();

```