package org.insa.graph;

import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class representing a path between nodes in a graph.
 * 
 * A path is represented as a list of {@link Arc} and not a list of {@link Node}
 * due to the multigraph nature of the considered graphs.
 *
 */
public class Path {

    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the fastest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     * 
     * IMPLEMENTED.
     */
	
    public static Path createFastestPathFromNodes(Graph graph, List<Node> nodes) throws IllegalArgumentException {
        List<Arc> arcs = new ArrayList<Arc>();
        //Pour chaque noeud de la liste, on va stocker l'arc le plus rapide menant au suivant
        Arc fastestArc = null;
        //Pour savoir si notre fastestArc contient bien un arc valide du noeud courant menant au suivant
        boolean initialized = false;
        if (nodes.size() == 1) {
        	return new Path(graph, nodes.get(0));
        }
        else {
        	for (int i = 0 ; i < nodes.size() - 1 ; i++) {
            	//On vérifie que le noeud courant a bien des successeurs
            	if (nodes.get(i).hasSuccessors()) {
            		//Pour chaque noeud, on parcourt les arcs qui en partent
        			for (Arc arc : nodes.get(i)) {
        				if (arc.getDestination().compareTo(nodes.get(i+1)) == 0) {
        					//On stocke dans fastestArc le premier arc valide que l'on trouve si fastestArc n'est pas
        					//encore initialisé
        					if(!initialized) {
        						fastestArc = arc;
        						initialized= true;
        					}
        					//Si fastestArc est déjà valide, on le compare à notre arc courant
        					else {
        						if (arc.getMinimumTravelTime() < fastestArc.getMinimumTravelTime()) {
        							//On met à jour l'arc le plus rapide
        							fastestArc = arc;
        						}
        					}
        				}
        			}
        			//Si on a trouvé aucun arc qui mène au noeud suivant
        			if (!initialized) {
        				throw new IllegalArgumentException();
        			}
        			//Sinon on remet fastestArc comme non initialisé puis on passe au noeud suivant après avoir mis à jour 
        			//notre path
        			initialized = false;
        			arcs.add(fastestArc);
            	}
            	//Si le noeud courant ne possède aucun successeur
            	else {
            		throw new IllegalArgumentException();
            	}
            }
        }
        return new Path(graph, arcs);
    }

    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the shortest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     * 
     * Implemented.
     */

    public static Path createShortestPathFromNodes(Graph graph, List<Node> nodes) throws IllegalArgumentException { /*Si il n'y a pas de noeud exception */
    	List<Arc> arcs = new ArrayList<Arc>();
        //Pour chaque noeud de la liste, on va stocker l'arc le plus rapide menant au suivant
        Arc fastestArc = null;
        //Pour savoir si notre fastestArc contient bien un arc valide du noeud courant menant au suivant
        boolean initialized = false;
        if (nodes.size() == 1) {
        	return new Path(graph, nodes.get(0));
        }
        else {
        	for (int i = 0 ; i < nodes.size() - 1 ; i++) {
            	//On vérifie que le noeud courant a bien des successeurs
            	if (nodes.get(i).hasSuccessors()) {
            		//Pour chaque noeud, on parcourt les arcs qui en partent
        			for (Arc arc : nodes.get(i)) {
        				if (arc.getDestination().compareTo(nodes.get(i+1)) == 0) {
        					//On stocke dans fastestArc le premier arc valide que l'on trouve si fastestArc n'est pas
        					//encore initialisé
        					if(!initialized) {
        						fastestArc = arc;
        						initialized= true;
        					}
        					//Si fastestArc est déjà valide, on le compare à notre arc courant
        					else {
        						if (arc.getLength() < fastestArc.getLength()) {
        							//On met à jour l'arc le plus rapide
        							fastestArc = arc;
        						}
        					}
        				}
        			}
        			//Si on a trouvé aucun arc qui mène au noeud suivant
        			if (!initialized) {
        				throw new IllegalArgumentException();
        			}
        			//Sinon on remet fastestArc comme non initialisé puis on passe au noeud suivant après avoir mis à jour 
        			//notre path
        			initialized = false;
        			arcs.add(fastestArc);
            	}
            	//Si le noeud courant ne possède aucun successeur
            	else {
            		throw new IllegalArgumentException();
            	}
            }
        }
        return new Path(graph, arcs);
    }

    /**
     * Concatenate the given paths.
     * 
     * @param paths Array of paths to concatenate.
     * 
     * @return Concatenated path.
     * 
     * @throws IllegalArgumentException if the paths cannot be concatenated (IDs of
     *         map do not match, or the end of a path is not the beginning of the
     *         next).
     */
    public static Path concatenate(Path... paths) throws IllegalArgumentException {
        if (paths.length == 0) {
            throw new IllegalArgumentException("Cannot concatenate an empty list of paths.");
        }
        final String mapId = paths[0].getGraph().getMapId();
        for (int i = 1; i < paths.length; ++i) {
            if (!paths[i].getGraph().getMapId().equals(mapId)) {
                throw new IllegalArgumentException(
                        "Cannot concatenate paths from different graphs.");
            }
        }
        ArrayList<Arc> arcs = new ArrayList<>();
        for (Path path: paths) {
            arcs.addAll(path.getArcs());
        }
        Path path = new Path(paths[0].getGraph(), arcs);
        if (!path.isValid()) {
            throw new IllegalArgumentException(
                    "Cannot concatenate paths that do not form a single path.");
        }
        return path;
    }

    // Graph containing this path.
    private final Graph graph;

    // Origin of the path
    private final Node origin;

    // List of arcs in this path.
    private final List<Arc> arcs;

    /**
     * Create an empty path corresponding to the given graph.
     * 
     * @param graph Graph containing the path.
     */
    public Path(Graph graph) {
        this.graph = graph;
        this.origin = null;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path containing a single node.
     * 
     * @param graph Graph containing the path.
     * @param node Single node of the path.
     */
    public Path(Graph graph, Node node) {
        this.graph = graph;
        this.origin = node;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path with the given list of arcs.
     * 
     * @param graph Graph containing the path.
     * @param arcs Arcs to construct the path.
     */
    public Path(Graph graph, List<Arc> arcs) {
        this.graph = graph;
        this.arcs = arcs;
        this.origin = arcs.size() > 0 ? arcs.get(0).getOrigin() : null;
    }

    /**
     * @return Graph containing the path.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @return First node of the path.
     */
    public Node getOrigin() {
        return origin;
    }

    /**
     * @return Last node of the path.
     */
    public Node getDestination() {
        return arcs.get(arcs.size() - 1).getDestination();
    }

    /**
     * @return List of arcs in the path.
     */
    public List<Arc> getArcs() {
        return Collections.unmodifiableList(arcs);
    }

    /**
     * Check if this path is empty (it does not contain any node).
     * 
     * @return true if this path is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.origin == null;
    }

    /**
     * Get the number of <b>nodes</b> in this path.
     * 
     * @return Number of nodes in this path.
     */
    public int size() {
        return isEmpty() ? 0 : 1 + this.arcs.size();
    }

    /**
     * Check if this path is valid.
     * 
     * A path is valid if any of the following is true:
     * <ul>
     * <li>it is empty;</li>
     * <li>it contains a single node (without arcs);</li>
     * <li>the first arc has for origin the origin of the path and, for two
     * consecutive arcs, the destination of the first one is the origin of the
     * second one.</li>
     * </ul>
     * 
     * @return true if the path is valid, false otherwise.
     * 
     * IMPLEMENTED.
     */
    public boolean isValid() {
        boolean valid = false;
        //Si le chemin est vide, il est valide
        if (this.isEmpty()) {
        	valid = true;
        }
        else {
        	//Sinon s'il ne contient qu'un seul noeud, il est aussi valide
        	if (this.size() == 1) {
        		valid = true;
        	}
        	else {
        		//Si l'origine du chemin est la même que l'origine du premier arc
        		if (this.getOrigin().compareTo(arcs.get(0).getOrigin()) == 0) {
        			//On considère momentanément le chemin comme valide
        			valid = true;
        			//Mais si un seul de ses arcs a une destination qui diffère de celle du suivant,
        			//Le chemin devient non valide
        			for (int i = 0 ; i < arcs.size() - 1 ; i++) {
        				if (arcs.get(i).getDestination().compareTo(arcs.get(i+1).getOrigin()) != 0) {
        					valid = false;
        					break;
        				}
        			}
        		}
        	}
        }
        return valid;
    }

    /**
     * Compute the length of this path (in meters).
     * 
     * @return Total length of the path (in meters).
     * 
     * IMPLEMENTED
     */
    public float getLength() {
    	//La longueur totale du chemin
        float pathLength = 0;
        //Pour chaque arc du chemin, on récupère sa longueur et on fait la somme
        for (Arc arc : arcs) {
        	pathLength += arc.getLength();
        }
        return pathLength;
    }

    /**
     * Compute the time required to travel this path if moving at the given speed.
     * 
     * @param speed Speed to compute the travel time.
     * 
     * @return Time (in seconds) required to travel this path at the given speed (in
     *         kilometers-per-hour).
     * 
     * IMPLEMENTED.
     */
    public double getTravelTime(double speed) {
    	//On convertit la vitesse donnée en en m/s 
    	double speedInMetersSeconds = speed/3.6; 
    	//On divise la longueur du chemin par la vitesse en m.s
    	double travelTime = this.getLength() / speedInMetersSeconds;
    	return travelTime;
    }

    /**
     * Compute the time to travel this path if moving at the maximum allowed speed
     * on every arc.
     * 
     * @return Minimum travel time to travel this path (in seconds).
     * 
     * IMPLEMENTED.
     */
    public double getMinimumTravelTime() {
    	//La durée minimale
    	double travelTime = 0;
    	//Pour chaque arc, on cherche sa durée minimale et on fait la somme totale
    	for (Arc arc : arcs) {
    		travelTime += arc.getMinimumTravelTime();
    	}
    	return travelTime;
    }

}
