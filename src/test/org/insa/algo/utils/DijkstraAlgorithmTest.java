package org.insa.algo.utils;

import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import org.insa.algo.shortestpath.BellmanFordAlgorithm;
import org.insa.algo.shortestpath.DijkstraAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;
import org.junit.BeforeClass;
import org.junit.Test;
import org.insa.graph.RoadInformation;
import org.insa.graph.RoadInformation.RoadType;
import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;

public class DijkstraAlgorithmTest {
	// Small graph use for tests
    private static Graph graph;

    // List of nodes
    private static Node[] nodes;

    //List of arcs
    @SuppressWarnings("unused")
    private static Arc x1_x2, x1_x3, x2_x4, x2_x5, x2_x6, x3_x1, x3_x2, x3_x6, x5_x3, x5_x4, x5_x6, x6_x5;
    //Les résultats de l'éxecution des algos de chaque sommet à chaque autre
    private static Path[][] pathsD = new Path[6][6];
    private static Path[][] pathsB = new Path[6][6];
    //Pour créer les algos à chaque tour de boucle
    private static DijkstraAlgorithm algoD;
    private static BellmanFordAlgorithm algoB;
    
    @BeforeClass
    public static void init() {
    	//Pour pouvoir récupérer les différents filtres à appliquer aux chemins
    	java.util.List<ArcInspector> filters = new ArrayList<ArcInspector>();
    	filters = ArcInspectorFactory.getAllFilters();
    	//Les noeuds de notreg graphe de test
    	nodes = new Node[6];
    	int ligne, colonne;
    	for (int i = 0 ; i < 6 ; i++) {
    		nodes[i] = new Node(i, null);
    	}
    	//Infos sur le type des arcs
    	RoadInformation info = new RoadInformation(RoadType.MOTORWAY, null, true, 36, "");
    	//Création des arcs
    	x1_x2 = Node.linkNodes(nodes[0], nodes[1], 7, info, null);
    	x1_x3 = Node.linkNodes(nodes[0], nodes[2], 8, info, null);
    	x2_x4 = Node.linkNodes(nodes[1], nodes[3], 4, info, null);
    	x2_x5 = Node.linkNodes(nodes[1], nodes[4], 1, info, null);
    	x2_x6 = Node.linkNodes(nodes[1], nodes[5], 5, info, null);
    	x3_x1 = Node.linkNodes(nodes[2], nodes[0], 7, info, null);
    	x3_x2 = Node.linkNodes(nodes[2], nodes[1], 2, info, null);
    	x3_x6 = Node.linkNodes(nodes[2], nodes[5], 2, info, null);
    	x5_x3 = Node.linkNodes(nodes[4], nodes[2], 2, info, null);
    	x5_x4 = Node.linkNodes(nodes[4], nodes[3], 2, info, null);
    	x5_x6 = Node.linkNodes(nodes[4], nodes[5], 3, info, null);
    	x6_x5 = Node.linkNodes(nodes[5], nodes[4], 3, info, null);
    	
    	//Construction du graphe
    	graph = new Graph("Mandouj", "TestDijkstra", Arrays.asList(nodes), null);
    	 
    	//Construction des résultats 
    	for(ligne = 0 ; ligne < 6 ; ligne++) {
    		for(colonne = 0 ; colonne < 6 ; colonne++) {
    			algoD = new DijkstraAlgorithm(new ShortestPathData(graph, nodes[ligne], nodes[colonne], filters.get(0)));
    			algoB = new BellmanFordAlgorithm(new ShortestPathData(graph, nodes[ligne], nodes[colonne], filters.get(0)));
    			//System.out.println("["+ ligne + ";" + colonne + "]");
    			pathsD[ligne][colonne] = algoD.doRun().getPath();
        		pathsB[ligne][colonne] = algoB.doRun().getPath();
    		}
    	}
    }
    
    @Test
    //Pour vérifier que les solutions de notre algo de Dijkstra correspondent bien à celles de BF
    public void testDijkstra() {
    	int ligne, colonne;
    	for(ligne = 0 ; ligne < 6 ; ligne++) {
    		for(colonne = 0 ; colonne < 6 ; colonne++) {
    			//Pour visualiser la case qui pose problème
    			System.out.println("["+ ligne + ";" + colonne + "]");
    			assertEquals(pathsB[ligne][colonne], pathsD[ligne][colonne]); 
    		}
    	}
    }
}
