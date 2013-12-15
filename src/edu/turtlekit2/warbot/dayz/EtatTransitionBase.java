package edu.turtlekit2.warbot.dayz;

public class EtatTransitionBase extends Etat {

	public EtatTransitionBase()
	{
		BaseSoFar bsf = new BaseSoFar();
		this.transitions.add(bsf);
	}
	
	@Override
	void activite(BrainExplorer be) {
		// TODO Auto-generated method stub
		
	}
	
	 boolean etatFinal(){
		 return false;
	 }

}
