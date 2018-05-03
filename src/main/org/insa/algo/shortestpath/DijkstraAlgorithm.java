package org.insa.algo.shortestpath;

import java.util.List;
import java.util.ArrayList;

import org.insa.algo.AbstractSolution;
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
    	int i = 0;
    	Path plusCourtChemin;
    	Label labelCourant;
    	ArrayList<Node> noeuds = new ArrayList<Node>();
        ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        Graph graph = data.getGraph(); 
        final int nbNodes = graph.size();
        Label[] labels = new Label[nbNodes];
        Node noeudCourant;
        //Init : on donne un label à tous les noeuds du graphe
        for (Node noeud : graph) {
    		labels[noeud.getId()] = new Label(noeud);
    	}
        //On met le sommet d'origine dans le tas et on met son coût à 0
        labels[data.getOrigin().getId()].setCout(0);
        tas.insert(labels[data.getOrigin().getId()]);
        //Tant que le sommet de destination n'est pas marqué, on itère Dijkstra
        while(!labels[data.getDestination().getId()].estMarque())
        {
        	//On récupère le sommet de coût minimal par rapport à l'origine
        	labelCourant = tas.findMin();
        	//On l'enlève du tas
        	tas.remove(labelCourant);
        	//On le flag comme marqué
        	noeudCourant = labelCourant.getNoeud();
        	notifyNodeReached(noeudCourant);
        	labels[noeudCourant.getId()].setMarque(true);
        	//On parcourt tous les successeurs
        	for (Arc successeur : noeudCourant) {
        		//Si le successeur courant n'est pas déjà marqué
        		if (!labels[successeur.getDestination().getId()].estMarque() && data.isAllowed(successeur)) {
        			//Si on trouve un cout inférieur au coût actuel du successeur
        			if(labels[noeudCourant.getId()].getCout()+successeur.getLength() < labels[successeur.getDestination().getId()].getCout()) {
        				//On met à jour le coût du successeur
        				labels[successeur.getDestination().getId()].setCout(labels[noeudCourant.getId()].getCout()+successeur.getLength());
        				//On met à jour le père du successeur
        				labels[successeur.getDestination().getId()].setFather(noeudCourant);
        				//On insère dans le tas le label du successeur
        				if(!labels[successeur.getDestination().getId()].estMarque())
        					tas.insert(labels[successeur.getDestination().getId()]);
        			}
        		}
        	}
        	i++;
        }
        System.out.println("Noeuds : " + graph.size());
        System.out.println("Itérations : " + i);
        
        //On récupère la destination
        noeudCourant = labels[data.getDestination().getId()].noeud;
        //On récupère son label
        labelCourant = labels[data.getDestination().getId()];
        //On l'ajoute au début de notre liste 
        noeuds.add(0, noeudCourant);
        //Tant qu'on a pas ajouté l'origine du chemin à la liste de noeuds
        while(noeuds.get(0) != labels[data.getOrigin().getId()].noeud) {
        	noeudCourant = labelCourant.getFather();
        	labelCourant = labels[noeudCourant.getId()];
        	noeuds.add(0, noeudCourant);
        }
        System.out.println("Arcs : " + (noeuds.size()-1));
        plusCourtChemin = Path.createShortestPathFromNodes(graph, noeuds);
        solution = new ShortestPathSolution(data, AbstractSolution.Status.valueOf("OPTIMAL"), plusCourtChemin);
        return solution;
    }

}
