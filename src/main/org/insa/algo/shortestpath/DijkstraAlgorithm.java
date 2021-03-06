package org.insa.algo.shortestpath;

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
    public ShortestPathSolution doRun() {
    	//Pour compter le nombre d'itérations de Dijkstra
    	int i = 0;
    	//Si le chemin demandé est faisable
    	boolean feasible = true;
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
    	Label[] labels = new Label[nbNodes];
    	
    	//Pour itérer dans Dijkstra
    	Node noeudCourant;
    	Label labelCourant;
    	
    	//Les données du graphes, va notamment nous servir pour origine et destination
        ShortestPathData data = getInputData();
        
        //Le tas qui va contenir les labels de nos noeuds
        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        
        //Init : on donne un label à tous les noeuds du graphe
        for (Node noeud : graph) {
    		labels[noeud.getId()] = new Label(noeud);
    	}
        
        //On met le sommet d'origine dans le tas et on met son coût à 0
        labels[data.getOrigin().getId()].setCout(0);
        tas.insert(labels[data.getOrigin().getId()]);
        //Si l'origine et la destination diffèrent
        if(data.getOrigin().getId() != data.getDestination().getId()) {
        	//Tant que le sommet de destination n'est pas marqué, on itère Dijkstra
            while(!labels[data.getDestination().getId()].estMarque())
            {
            	//On enlève le sommet de coût minimal par rapport à l'origine
            	if(!tas.isEmpty()) {
                	labelCourant = tas.deleteMin();
                	noeudCourant = labelCourant.getNoeud();
                	//On le flag comme marqué
                	labels[noeudCourant.getId()].setMarque(true);
                	
                	//Pour visualiser le déroulement de l'algo sur la carte
                	notifyNodeReached(noeudCourant);
                	
                	//On parcourt tous les successeurs pour actualiser les coûts
                	for (Arc successeur : noeudCourant) {
                		//Si le successeur est autorisé dans le trajet et si sa destination n'est pas déjà marquée
                		if (data.isAllowed(successeur) && !labels[successeur.getDestination().getId()].estMarque()) {
                			//Si on trouve un cout inférieur au coût actuel du successeur
                			if(labels[noeudCourant.getId()].getTotalCost()+data.getCost(successeur) < labels[successeur.getDestination().getId()].getTotalCost()) {
                				//On met à jour le coût du successeur
                				labels[successeur.getDestination().getId()].setCout(labels[noeudCourant.getId()].getTotalCost()+data.getCost(successeur));
                				//On met à jour le père du successeur
                				labels[successeur.getDestination().getId()].setFather(noeudCourant);
                				//On insère dans le tas le label du successeur 
                				tas.insert(labels[successeur.getDestination().getId()]);
                			}
                		}
                	}
                	//On incrémente le nombre d'itérations
                	i++;
            	}
            	else {
            		//Le tas est vide alors que la destination n'a pas été atteinte => Il n'existe pas de solution
            		feasible = false;
            		break;
            	}
            }
        }
        else {
        	//L'origine et la destination sont le même noeud => Il existe une solution : le chemin null
        	nullPath = true;
        }
        
        //S'il existe une solution non nulle
        if(feasible && !nullPath) {
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
            plusCourtChemin = Path.createShortestPathFromNodes(graph, noeuds);
            solution = new ShortestPathSolution(data, AbstractSolution.Status.valueOf("OPTIMAL"), plusCourtChemin);
        }
        else {
        	//Chemin null
        	if (nullPath) {
        		solution = new ShortestPathSolution(data, AbstractSolution.Status.valueOf("OPTIMAL"), null);
        	}
        	//Pas de solution
        	else {
            	solution = new ShortestPathSolution(data, AbstractSolution.Status.valueOf("INFEASIBLE"));
            }
        }
        //BILAN DE L'EXECUTION
        
        //System.out.println("Arcs : " + (noeuds.size()-1));
        //System.out.println("Noeuds : " + graph.size());
        //System.out.println("Itérations : " + i);
        
        return solution;
    }

}
