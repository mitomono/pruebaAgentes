/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebaAgentes;

import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;

/**
 *
 * @author adri
 */
public class PruebaAgentes {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        
        // declaracion de agentes.
        Bot bot;
        Scanner scanner;
        Gps gps;
        
        // conexion con el servidor
        AgentsConnection.connect("isg2.ugr.es", 6000, "Denebola", "Leon", "Russo", false);
        
        // instanciacion de los agentes
        bot = new Bot(new AgentID("bot"));
        scanner=new Scanner(new AgentID("scanner"));
        gps = new Gps(new AgentID("gps"));
        
        // comienzo de la ejecucion de los agentes
        scanner.start();
        gps.start();
        bot.start();
    }
    
}
