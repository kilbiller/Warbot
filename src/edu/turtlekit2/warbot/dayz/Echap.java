package edu.turtlekit2.warbot.dayz;

/**
 * Classe d'�tat final permettant d'arr�ter l'automate
 * @author Florian
 *
 */
public class Echap extends Etat {

	public Echap()
	{}
	
	 boolean etatFinal(){
		 return true;
	 }

	@Override
	void activite(BrainExplorer be) {
		// TODO Auto-generated method stub
	}
}
