package org.insa.algo.shortestpath;

import java.util.ArrayList;

import org.insa.algo.utils.BinaryHeap;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    public ShortestPathSolution doRun() {
    	//A faire getCout()
    
    	//Les donnÃ©es du graphes, va notamment nous servir pour origine et destination
        ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
    	//Le tas qui va contenir les labels de nos noeuds
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        Arc[] Arcs = new Arc[graph.size()];
        ArrayList<Node> noeuds = new ArrayList<Node>();
        //On recupère le coût pour chaque noeud
        for(int i; i<graph.size();i++)
        {
        	for (Arc arc : i) {
	        	int coutDestination = i.getPoint().distanceTo(data.getDestination().getPoint();
	            BinaryHeap<Label> labelStar = new LabelStar<Label>(i,coutDestination);
	            Arrays.sort(labelStar.getTotalCost())

        	}
        }
        
    }

}
