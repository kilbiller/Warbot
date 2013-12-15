package edu.turtlekit2.warbot.dayz;

import java.util.List;
import java.util.Scanner;

import edu.turtlekit2.warbot.message.WarMessage;
import edu.turtlekit2.warbot.percepts.Percept;
import edu.turtlekit2.warbot.waritems.WarFood;

/**
 * Classe d'état final permettant de donner la nourriture récupéré
 * @author Florian
 *
 */
public class Rapporter extends Etat{

	public Rapporter()
	{}
	@Override
	void activite(BrainExplorer be) {
	}
	
	 boolean etatFinal(){
		 return true;
	 }

}
