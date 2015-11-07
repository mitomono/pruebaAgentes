/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebaAgentes;

import com.eclipsesource.json.*;
import es.upv.dsic.gti_ia.core.ACLMessage;
import java.util.ArrayList;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;
import es.upv.dsic.gti_ia.core.SingleAgent;
import org.codehaus.jettison.json.JSONString;

/**
 *
 * @author adri
 */
public class Scanner extends SingleAgent {

    public Scanner(AgentID aid) throws Exception {
        super(aid);
    }

    @Override
    public void execute() {
        // mensages de I/O
        ACLMessage inbox, outbox = new ACLMessage();

        // Json donde se guarda el mensage recibido
        JsonArray mensageRecibido;

        try {
            //recepcion del mensage del servidor
            inbox = this.receiveACLMessage();

            // se parsea el mensage y se extrae el array con los datos
            // que es lo que se guarda y se envia
            mensageRecibido = Json.parse(inbox.getContent()).asObject().get("scanner").asArray();

            // relleno del mensage de salida
            outbox.setSender(this.getAid());
            outbox.setReceiver(new AgentID("bot"));
            outbox.setContent(mensageRecibido.toString());

            // envio del mensage
            this.send(outbox);

        } catch (InterruptedException ex) {
        }
    }

}
