
import java.io.ByteArrayInputStream;

import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pjg
 */

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.jdom.Document;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.Field;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.FileUtils;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.InvalidStateMachineException;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.Role;
import uk.ac.soton.itinnovation.modelmyprivacy.modelframework.data.XMLDocument;
import uk.ac.soton.itinnovation.modelmyprivacy.modelframework.data.XMLStateMachine;
import uk.ac.soton.itinnovation.modelmyprivacy.lts.StateMachine;

public class RolesDataTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InvalidStateMachineException{
        final String sMachine = FileUtils.readFile("YellowPages.xml", Charset.defaultCharset());
        final Document model = XMLDocument.jDomReadXmlStream(
                    new ByteArrayInputStream(sMachine.getBytes(StandardCharsets.UTF_8)));
        List<Role> addRoles = XMLStateMachine.addRoles(model.getRootElement());
        for(Role r: addRoles) {
            System.out.println("Role - " + r.getRoleIdentity());
            System.out.println("Category - " + r.getRoleCategory());
        }

        List<Field> addRecords = XMLStateMachine.addRecords(model.getRootElement());
        for(Field r: addRecords) {
            System.out.println("Record  Name - " + r.getName());
            if(r.getRecord()) {
                System.out.println("Fields: ");
                List<Field> recordFields = r.getRecordField();
                for(Field f: recordFields) {
                    System.out.println("Field  Name - " + f.getName());
                    System.out.println("Field Category - " + f.getCategory().get(0));
                    System.out.println("is Field Type Sensitive? - " + f.getSensitive());
                    System.out.println("is Type EI? - " + f.getEI());
                    System.out.println("is Type QI? - " + f.getQI());
                }
            }
        }

        StateMachine stateMachine = new StateMachine();
        stateMachine.buildStates(sMachine);
        stateMachine.generateStates(stateMachine.getStartState());
        stateMachine.visualiseGraph();

    }
}
