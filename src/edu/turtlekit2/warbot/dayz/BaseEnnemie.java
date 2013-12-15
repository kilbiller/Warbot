package edu.turtlekit2.warbot.dayz;

import java.util.List;
import java.util.Scanner;

import edu.turtlekit2.warbot.percepts.Percept;

/**
 * Classe de transition permettant de v�rifier si une base ennemie est a proximit�
 * @author Florian
 *
 */
public class BaseEnnemie extends Transition{

	public BaseEnnemie() {}
	
	@Override
	boolean valide(BrainExplorer be) {
		List<Percept> liste = be.getPercepts();
		
		for(Percept p : liste)
		{
			//Si une base ennemie est pr�sente, on envoie alors un signal � tout les rocketlauncher
			if(p.getType().equals("WarBase") && p.getTeam() != be.getTeam())
			{
				this.etat = new EnvoyerSignal();
				return true;
			}
		}
		
		return false;
	}

}
