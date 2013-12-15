package edu.turtlekit2.warbot.dayz;

import java.util.List;

import edu.turtlekit2.warbot.percepts.Percept;

/**
 * Classe d'état final permettant d'envoyer le message au rocketlauncher
 * @author Florian
 *
 */
public class EnvoyerSignal extends Etat {

	public EnvoyerSignal()
	{}
	
	@Override
	void activite(BrainExplorer be) {
		// TODO Auto-generated method stub
		List<Percept> liste = be.getPercepts();
		
		for(Percept p : liste){
			//Si on trouve la base ennemie, on envoie un message au rocketLauncher
			if(p.getType().equals("WarBase") && p.getTeam() != be.getTeam())
			{
				be.broadcastMessage("WarRocketLauncher", "baseEnnemy", null);
			}
		}
	}
	
	 boolean etatFinal(){
		 return true;
	 }
}
