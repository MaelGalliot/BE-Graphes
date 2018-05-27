package org.insa.algo.shortestpath;

import org.insa.graph.Node;

public class LabelStar extends Label{
	protected double coutDest;
	
	public LabelStar (Node noeud) {
		super(noeud);
		this.coutDest = Double.POSITIVE_INFINITY;;
	}
	
	public void setCoutDest (double coutDest) {
		this.coutDest = coutDest;
	}
	
	public double getCoutDest() {
		return this.coutDest;
	}
	
	public double getTotalCost() {
		return (this.getCoutDest() + this.getCout());
	}
}
