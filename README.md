# Model-Privacy
Model-Privacy is a tool to create, annotate and analyse 
a general purpose user-centric privacy model. This is
specified as a Labeled Transition System
(LTS). Here, actions on personal data (e.g., a third-party
reads personal data) model the change of a user’s state
of privacy. Analysis of this model can then determine if
actions carried out by an online service conflict with a user’s
privacy preferences. Therefore, such a model provides two
main benefits: 

* abstraction: the ability to abstract a complex
system’s behaviour with regards to personal data and then
machine analyze the privacy implications;
* re-usability: the ability to use the model to analyze a wide range of policies
using different analysis algorithms.

## Privacy implication analysis

A service’s usage of private data can be analyzed against a user’s privacy
preferences to determine the risk of a privacy breach.
From this, the implications can be presented to the
user along with recommendations on how to manage
the risk:
* Fine-grained privacy aware access control: a cloudbased
service’s access control policies can be adapted
to match user privacy preferences, blocking some
service actions (where these are not critical to the
service operation). We present an algorithm to generate
the user privacy model (as an LTS) for a service,
and identify transitions that should be blocked such
that service actions that pose too great a risk to
privacy become inaccessible


## Install and Run

The tool is made available as a maven project. In order to build the tool, Java 1.8 or 
higher must be installed on your machine.

To install the tool. Simply download the zip file for the git repository, extract and 
go to the root folder location. Then type at the command prompt:

```
mvn  install
```

To run the tool at any time, type at the command prompt:

```
mvn  test
```

## User Guides

The following are a list of further documents in order to use the tool to develop different types of interoperability tests.
* [How to create a data flow model](docs/dataflow.md)
* [How to autogenerate transitions based on policies](docs/auto.md)
* [How to annotate transitions](docs/features.md)
 

## Contributors
The following developers contributed to the creation of the ModelInterop tool: 
* Paul Grace, IT Innovation, University of Southampton [Web page] (http://www.ecs.soton.ac.uk/people/pjg1f12) [Github page] (https://github.com/pjgrace)
* Dan Burns, IT Innovation, University of Southampton  
* Geoff Neuman, IT Innovation, University of Southampton
