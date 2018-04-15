package org.insa.algo.shortestpath;

import java.util.List;
import java.util.ArrayList;

import org.insa.algo.utils.BinaryHeap;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
    	Path plusCourtChemin;
    	//UTILISER ARRAYLIST VOIR DOC
    	List<Arc> arcs = new List<Arc>();
        ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        BinaryHeap<Node> tas = new BinaryHeap<Node>();
        Graph graph = data.getGraph(); 
        final int nbNodes = graph.size();
        Label[] labels = new Label[nbNodes];
        Node noeudCourant;
        ArrayList<Arc> successeurs;
        //Init
        for (Node noeud : graph) { // Pour tous les noeuds du graphe
    		labels[noeud.getId()] = new Label(noeud);
    	}
        tas.insert(data.getOrigin());
        labels[data.getOrigin().getId()].setCout(0);
        while(!labels[data.getDestination().getId()].estMarque())
        {
        	noeudCourant = tas.findMin();
        	tas.remove(noeudCourant);
        	labels[noeudCourant.getId()].setMarque(true);
        	for (Arc successeur : noeudCourant) {
        		if (!labels[successeur.getDestination().getId()].estMarque()) {
        			if(labels[noeudCourant.getId()].getCout()+successeur.getLength() < labels[successeur.getDestination().getId()].getCout()) {
        				labels[successeur.getDestination().getId()].setCout(labels[noeudCourant.getId()].getCout()+successeur.getLength());
        				labels[successeur.getDestination().getId()].setFather(noeudCourant);
        				tas.insert(successeur.getDestination());
        			}
        		}
        	}
        }
        solution = new ShortestPathSolution(data);
        return solution;
    }

}
