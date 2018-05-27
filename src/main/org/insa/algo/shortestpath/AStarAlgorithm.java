package org.insa.algo.shortestpath;

import java.util.ArrayList;

import org.insa.algo.AbstractSolution;
import org.insa.algo.utils.BinaryHeap;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    public ShortestPathSolution doRun() {
    	//Pour compter le nombre d'itérations de A*
    	int i = 0;
    	//Si le chemin demandé existe
    	boolean pathExists = true;
    	//Si l'origine est égale à la destination
    	boolean nullPath = false;
    	//Pour construire la solution, on va créer plusCourtChemin à partir de noeuds, puis solution à partir de
    	//plusCourtChemin
    	ArrayList<Node> noeuds = new ArrayList<Node>();
    	Path plusCourtChemin;
        ShortestPathSolution solution = null;
    	
    	//Le graphe à explorer et sa taille
    	Graph graph = data.getGraph(); 
    	final int nbNodes = graph.size();
    	
    	//On fait correspondre un label à chaque noeud du graphe
    	LabelStar[] labels = new LabelStar[nbNodes];
    	
    	//Pour itérer dans A*
    	Node noeudCourant;
    	LabelStar labelCourant;
    	
    	//Les données du graphes, va notamment nous servir pour origine et destination
        ShortestPathData data = getInputData();
        
        //Le tas qui va contenir les labels de nos noeuds
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        
        //Init : on donne un label à tous les noeuds du graphe
        for (Node noeud : graph) {
    		labels[noeud.getId()] = new LabelStar(noeud);
    	}
        
        //On met le sommet d'origine dans le tas, on met son coût à 0 et on met son coût estimé à la destination
        labels[data.getOrigin().getId()].setCout(0);
        labels[data.getOrigin().getId()].setCoutDest(
        		labels[data.getOrigin().getId()].getNoeud().getPoint().distanceTo(
        				labels[data.getDestination().getId()].getNoeud().getPoint()));
        tas.insert(labels[data.getOrigin().getId()]);
        //Si l'origine et la destination diffèrent
        if(data.getOrigin().getId() != data.getDestination().getId()) {
        	//Tant que le sommet de destination n'est pas marqué, on itère Dijkstra
            while(!labels[data.getDestination().getId()].estMarque())
            {
            	//On récupère le sommet de coût minimal par rapport à l'origine
            	if(!tas.isEmpty()) {
                	labelCourant = (LabelStar) tas.findMin();
                	//On l'enlève du tas
                	tas.remove(labelCourant);
                	noeudCourant = labelCourant.getNoeud();
                	//On le flag comme marqué
                	labels[noeudCourant.getId()].setMarque(true);
                	//Pour visualiser le déroulement de l'algo sur la carte
                	notifyNodeReached(noeudCourant);
                	
                	//On parcourt tous les successeurs
                	for (Arc successeur : noeudCourant) {
                		//Si le successeur est autorisé dans le trajet
                		if (data.isAllowed(successeur)) {
                			//Si on trouve un cout inférieur au coût actuel du successeur
                			if(labels[noeudCourant.getId()].getTotalCost()+successeur.getLength() < labels[successeur.getDestination().getId()].getTotalCost()) {
                				//On met à jour le coût du successeur à l'origine 
                				labels[successeur.getDestination().getId()].setCout(labels[noeudCourant.getId()].getTotalCost()+successeur.getLength());
                				//et à la destination
                				labels[successeur.getDestination().getId()].setCoutDest(
                						labels[successeur.getDestination().getId()].getNoeud().getPoint().distanceTo(
                		        				labels[data.getDestination().getId()].getNoeud().getPoint()));
                				//On met à jour le père du successeur
                				labels[successeur.getDestination().getId()].setFather(noeudCourant);
                				//On insère dans le tas le label du successeur s'il n'y est pas déjà
                				if(!labels[successeur.getDestination().getId()].estMarque())
                					tas.insert(labels[successeur.getDestination().getId()]);
                			}
                		}
                	}
                	i++;
            	}
            	else {
            		pathExists = false;
            		break;
            	}
            }
        }
        else {
        	nullPath = true;
        }
       
        //S'il existe une solution
        if(pathExists && !nullPath) {
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
            //System.out.println("Arcs : " + (noeuds.size()-1));
            //System.out.println("Noeuds : " + graph.size());
            //System.out.println("Itérations : " + i);
            plusCourtChemin = Path.createShortestPathFromNodes(graph, noeuds);
            solution = new ShortestPathSolution(data, AbstractSolution.Status.valueOf("OPTIMAL"), plusCourtChemin);
        }
        else {
        	if (nullPath) {
        		solution = new ShortestPathSolution(data, AbstractSolution.Status.valueOf("OPTIMAL"), null);
        	}
        	else {
            	solution = new ShortestPathSolution(data, AbstractSolution.Status.valueOf("INFEASIBLE"));
            }
        }
        
        return solution;
    }
}
