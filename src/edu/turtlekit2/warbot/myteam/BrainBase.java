package edu.turtlekit2.warbot.myteam;

import java.util.List;

import edu.turtlekit2.warbot.WarBrain;
import edu.turtlekit2.warbot.message.WarMessage;

public class BrainBase extends WarBrain{

	public BrainBase(){
		
	}

	@Override
	public String action() {
		
		if(!emptyBag()){
			return "eat";
		}

		List<WarMessage> messages = getMessage();
		
		for(WarMessage m : messages){
			//Si on demande les coordonnées de la base
			if(m.getMessage() == "baseLocation")
				reply(m, "baseLocation", null);
		}
		
		if(getEnergy() > 12000){
			setNextAgentCreate("Explorer");
			return "create";
		}
		
		//TODO : Protection de la base
		//Pour protéger le base il faudrait faire une transaction:
		//base : help!
		//warlaunchers : nous sommes dispo
		//base : je prend les 2/3 tanks les plus proches
		//warlaunchers choisi : ok on arrive
		//warlaunchers pas choisi : on continue notre vie
		//Si par la suite la base est safe, retourne les tanks en mode defense en mode attaque
		
		return "idle";
	}
}
