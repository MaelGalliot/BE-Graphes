package org.insa.algo.shortestpath;

import org.insa.graph.Node;

public class Label implements Comparable<Label> {
	protected Node noeud;
	protected Node father;
	protected double cout;
	protected boolean marque;
	
	public Label(Node noeud) {
		this.noeud = noeud;
		this.cout = Double.POSITIVE_INFINITY;
		this.marque = false;
		this.father = null;
	}
	
	public double getCout() {
		return this.cout;
	}
	
	public double getTotalCost() {
		return this.getCout();
	}
	
	public void setCout(double cout) {
		this.cout = cout;
	}
	
	public boolean estMarque() {
		return this.marque;
	}
	
	public void setMarque(boolean marque) {
		this.marque = marque;
	}
	
	public Node getFather() {
		return this.father;
	}
	
	public void setFather(Node father) {
		this.father = father;
	}
	
	public int compareTo(Label other) {
        return Double.compare(getTotalCost(), other.getTotalCost());
    }
	
	public Node getNoeud() {
		return this.noeud;
	}
}
