
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.util.FileManager;
import java.io.InputStream;
import java.util.Iterator;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pjg
 */
public class PrivacyOntologyTest {

    public static OntClass queryOntClass(OntModel model, String labelOfTheClassIWant) {
        String queryString =
            "prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
            "\n" +
            "select ?class where { \n" +
            "  ?class rdfs:label \""+labelOfTheClassIWant+"\"\n" +
            "}";

        final ResultSet results = QueryExecutionFactory.create( queryString, model ).execSelect();
        if ( results.hasNext() ) {
            final QuerySolution solution = results.nextSolution();
            RDFNode get = solution.get( "class" );
            return (OntClass) get.as(OntClass.class);
        }
        return null;
    }

    public static OntModel getOntologyModel(String ontoFile)
    {
        OntModel ontoModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, null);
        try
        {
            InputStream in = FileManager.get().open(ontoFile);
            try
            {
                ontoModel.read(in, "RDF/XML");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            System.out.println("Ontology " + ontoFile + " loaded.");
        }
        catch (JenaException je)
        {
            System.err.println("ERROR" + je.getMessage());
            je.printStackTrace();
            System.exit(0);
        }
        return ontoModel;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // create the base model
        String SOURCE = "http://www.eswc2006.org/technologies/ontology";
        String NS = SOURCE + "#";

        OntModel base = getOntologyModel("root-ontology.owl");
                // ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM );
//        base.read( SOURCE, "RDF/XML" );
//
//        // create the reasoning model using the base
//        OntModel inf = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM_MICRO_RULE_INF, base );

        // create a dummy paper for this example

        OntClass paper = queryOntClass(base, "Medical");
        Individual p1 = base.createIndividual( "paper1", paper );

        // list the asserted types
        for (Iterator<Resource> i = p1.listRDFTypes(true); i.hasNext(); ) {
            System.out.println( p1.getURI() + " is asserted in class " + i );
        }

    }

}
