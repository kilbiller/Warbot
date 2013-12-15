package edu.turtlekit2.warbot.dayz;

import java.util.Scanner;

/**
 * Classe d'état transition permettant d'effectuer une seconde transition
 * @author Florian
 *
 */
public class EtatTransitionFood extends Etat {

	public EtatTransitionFood()
	{
		SoFar sf = new SoFar();
		this.transitions.add(sf);
	}
	
	void activite(BrainExplorer be)
	{}
	
	 boolean etatFinal(){
		 return false;
	 }
}
