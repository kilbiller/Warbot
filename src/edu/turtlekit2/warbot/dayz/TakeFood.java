package edu.turtlekit2.warbot.dayz;

import java.util.List;
import java.util.Scanner;

import edu.turtlekit2.warbot.percepts.Percept;
import edu.turtlekit2.warbot.waritems.WarFood;

/**
 * Classe d'etat final pour prendre la nourriture
 * @author Florian
 *
 */
public class TakeFood extends Etat {

	public TakeFood()
	{}
	@Override
	void activite(BrainExplorer be) {
	}
	
	 boolean etatFinal(){
		 return true;
	 }

}
