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

/**
 *
 * @author adri
 */
public class Gps extends SingleAgent{

    public Gps(AgentID aid) throws Exception {
        super(aid);
    }
    
    @Override
    public void execute() {
        // mensages de I/O
        ACLMessage inbox, outbox = new ACLMessage();

        // Json donde se guarda el mensage recibido
        JsonObject mensageRecibido;

        try {
            // recepcion del mensage del servidor
            inbox = this.receiveACLMessage();

            // parseo del mensage y extraccion de los datos (x,y)
            mensageRecibido = Json.parse(inbox.getContent()).asObject();
            JsonValue coordenadas = mensageRecibido.get("gps");

            // relleno del mensage de salida
            outbox.setSender(this.getAid());
            outbox.setReceiver(new AgentID("bot"));
            outbox.setContent(coordenadas.toString());

            // envio del mensage
            this.send(outbox);

        } catch (InterruptedException ex) {
        }
    }
    
}
