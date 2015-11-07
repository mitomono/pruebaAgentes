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
import java.util.ArrayList;

/**
 *
 * @author adri
 */
public class Bot extends SingleAgent {

    public Bot(AgentID aid) throws Exception {
        super(aid);
    }

    @Override
    public void execute() {
        // declaracion de las variables para mensages I/O
        ACLMessage inbox, outbox = new ACLMessage();

        // creaion de objetos Json para enviar al servidor como strines
        JsonObject conexion = Json.object();
        JsonObject movimiento = Json.object();
        JsonObject desconexion = Json.object();

        // rellenado del Json para conexion
        conexion.add("command", "login");
        conexion.add("world", "map1");
        conexion.add("radar", "bot");
        conexion.add("gps", "gps");
        conexion.add("scanner", "scanner");

        // creacion de un string a partir del Json
        String conexionS = conexion.toString();

        // configuracion del mensage de salida con la conexion
        outbox.setSender(this.getAid());
        outbox.setReceiver(new AgentID("Denebola"));
        outbox.setContent(conexionS);

        // envio del mensage al servidor
        this.send(outbox);

        try {
            // recepcion del mensage del servidor
            inbox = this.receiveACLMessage();

            // creacion de un Json con la respuesta del servidor
            JsonObject respuesta = Json.parse(inbox.getContent()).asObject();
            
            // creacion de un string para almacenar la clave enviada por el servidor
            // a partir del Json
            String clave = respuesta.get("result").asString();

            System.out.println("\nRecibido mensaje " + clave + " de " + inbox.getSender().getLocalName());

            // ejemplo para almacenar los mensages
            ArrayList<Integer> gps = new ArrayList<>();
            ArrayList<Double> scanner = new ArrayList<>();
            ArrayList<Integer> radar = new ArrayList<>();

            /****************************/
            /* recepcion de los mensges */
            /****************************/
            
            //radar
            //inbox = this.receiveACLMessage();
            //   System.out.println(inbox.toString());
            //scanner
            //   inbox = this.receiveACLMessage();
            //   System.out.println(inbox.toString());
            //gps
            //   inbox = this.receiveACLMessage();
            //   System.out.println(inbox.toString());
            
            // bucle para la escucha de los mensages
            for (int l = 0; l < 3; l++) {
                // recepcion del mensage
                inbox = this.receiveACLMessage();
                
                // comprobacion de que agente ha hablado
                switch (inbox.getSender().getLocalName()) {
                    case "scanner":
                        // se le pasa a la funcion el mensage devuelto 
                        // por el agente scaner
                        scanner = this.Ascanner(inbox.getContent());

                        System.out.println("salida scanner");
                        
                        // mostrar los datos del scaner
                        for (int i = 0; i < 5; i++) {
                            for (int k = 0; k < 5; k++) {
                                System.out.print(scanner.get(i * 5 + k) + " ");
                            }
                            System.out.print("\n");
                        }
                        System.out.print("\n");

                        break;
                        
                    case "gps":
                        // se le pasa a la funcion el mensage devuelto 
                        // por el agente gps
                        gps = this.Agps(inbox.getContent());

                        // mostrar los datos
                        System.out.println("salida gps");
                        System.out.println(gps.toString());
                        break;
                        
                        
                    case "Denebola_satellite": // Denebola_satellite es el nombre del agente
                        
                        // se le pasa a la funcion el mensage devuelto 
                        // por el radar (interno del agente)
                        radar = this.Aradar(inbox.getContent());

                        // mostrar datos
                        System.out.println("salida radar");
                        for (int i = 0; i < 5; i++) {
                            for (int k = 0; k < 5; k++) {
                                System.out.print(radar.get(i * 5 + k) + " ");
                            }
                            System.out.print("\n");
                        }
                        System.out.print("\n");
                        break;
                }
            }

            // preparacion del Json para movimiento
            movimiento.add("command", "moveS");
            movimiento.add("key", clave);
            
            // creacion del mensage de salida
            outbox.setContent(movimiento.toString());
            
            // envio del mensage
            this.send(outbox);

            System.out.println("me muevo al sur");
            
            //recepcion de la contestacion del servidor
            inbox = this.receiveACLMessage();
            System.out.println(inbox.getContent());

            // creacion del Json de desconexion
            desconexion.add("command", "logout");
            desconexion.add("key", clave);

            // creaciond del mensage de salida para desconexion
            outbox.setContent(desconexion.toString());
            
            // envio del mensage de desconexion
            this.send(outbox);
            System.out.println("desconectado");
        } catch (InterruptedException ex) {
        }
    }

   // funcion que transforma Json de radar a un array
    public ArrayList<Integer> Aradar(String s) {
        ArrayList<Integer> radar = new ArrayList<>();
        // System.out.println(s);

        // parseo del string con formato Json
        JsonArray arrayRadar = Json.parse(s).asObject().get("radar").asArray();

        for (int i = 0; i < 25; i++) {
            radar.add(arrayRadar.get(i).asInt());
        }

        return radar;
    }

    // funcion que transforma Json de scanner a un array
    public ArrayList<Double> Ascanner(String s) {

        ArrayList<Double> scanner = new ArrayList<>();

        // Parseo del string con formato Json
        // en este caso es un array Json
        JsonArray arrayScanner = Json.parse(s).asArray();

        for (int i = 0; i < 25; i++) {
            scanner.add(arrayScanner.get(i).asDouble());
        }

        return scanner;
    }

    // funcion que transforma Json de gps a un array
    public ArrayList<Integer> Agps(String s) {
        ArrayList<Integer> gps = new ArrayList<>();

        // parseo del string con formato Json
        JsonObject gpsJS = Json.parse(s).asObject();

        gps.add(gpsJS.getInt("x", -1));
        gps.add(gpsJS.getInt("y", -1));

        return gps;
    }
}
