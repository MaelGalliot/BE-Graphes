package org.insa.algo.shortestpath;

import java.time.Duration;
import java.util.ArrayList;

import org.insa.algo.AbstractInputData.Mode;
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
    	//Pour calculer la distance estimée à la destination
    	double modificateur = 1;
    	//Pour compter le nombre d'itérations de A*
    	int i = 0;
    	//S'il existe une solution
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
    		/*labels[noeud.getId()].setCoutDest(labels[noeud.getId()].getNoeud().getPoint().distanceTo(
                		        				data.getDestination().getPoint()));*/
    	}
        
        //On met le sommet d'origine dans le tas, on met son coût à 0 et on met son coût estimé à la destination
        labels[data.getOrigin().getId()].setCout(0);
        labels[data.getOrigin().getId()].setCoutDest(
        		labels[data.getOrigin().getId()].getNoeud().getPoint().distanceTo(
        				labels[data.getDestination().getId()].getNoeud().getPoint()));
        tas.insert(labels[data.getOrigin().getId()]);
        
        //En fonction du mode choisi (Temps ou Distance), le calcul du coût estimé à la destination sera différent
		if (data.getMode().equals(Mode.TIME)) {
			modificateur = (double)1/((double)data.getGraph().getGraphInformation().getMaximumSpeed()) * ((double)3600/(double)1000);
		}
        
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
                			if(labels[noeudCourant.getId()].getCout()+data.getCost(successeur) < labels[successeur.getDestination().getId()].getCout()) {
                				//On met à jour le coût du successeur à l'origine 
                				labels[successeur.getDestination().getId()].setCout(labels[noeudCourant.getId()].getCout()+data.getCost(successeur));
                				//et à la destination
                				labels[successeur.getDestination().getId()].setCoutDest(modificateur *
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
       
        //S'il existe une solution
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
        /*
        System.out.println("Arcs : " + (noeuds.size()-1));
        System.out.println("Noeuds : " + graph.size());
        System.out.println("Itérations : " + i);
        */
        return solution;
    }
}
