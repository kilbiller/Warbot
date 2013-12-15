package edu.turtlekit2.warbot.dayz;

import java.util.Scanner;

/**
 * Classe d'�tat pour la recherche de nourriture
 * @author Florian
 *
 */
public class Rechercher extends Etat{
	boolean etatfinal;
	
	public Rechercher(BrainExplorer be)
	{
		etatfinal = false;
		SacPlein sv = new SacPlein(); //Transition permettant de v�rifier si le sac est plein
		BaseEnnemie b = new BaseEnnemie(); //Transition permettant de v�rifier si il y a une base ennemie a proximit�
		if(be.getJob().equals("cueilleur"))
		{
			ExistFood ef = new ExistFood(); //Transition permettant de v�rifier si il y a de la nourriture
			this.transitions.add(ef);
		}
		
		this.transitions.add(sv);
		this.transitions.add(b);
		
	}
	
	@Override
	void activite(BrainExplorer be) {
		// TODO Auto-generated method stub
		//Si l'agent est bloqu�
		while(be.isBlocked()){
			be.setRandomHeading();
		}
		//L'�tat rechercher devient un �tat final car il ne peut aller a aucune transition
		etatfinal = true;
		//return "move";
	}
	
	boolean etatFinal()
	{
		return etatfinal;
	}

}
