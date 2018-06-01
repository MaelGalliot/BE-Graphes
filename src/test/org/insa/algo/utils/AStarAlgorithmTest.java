package org.insa.algo.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.insa.algo.shortestpath.AStarAlgorithm;
import org.insa.algo.shortestpath.DijkstraAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.ShortestPathSolution;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.junit.Test;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graph.io.GraphReader;
import org.insa.algo.ArcInspector;
import org.insa.algo.ArcInspectorFactory;

public class AStarAlgorithmTest {
	
	@Test
	//VALIDITE & PERFORMANCE
	//Test avancé, on génère 100 couples de points aléatoires et on compare les résultats de A* et de Dijkstra
    //pour chaque couple (on compare juste les longueurs des paths retournés)
	//pour un total de 400 tests (chaque couple étant testé de 4 manières différentes)
    public void randomTest() throws Exception{
		/*
		 * Pour stocker les différents résultats de nos tests
		 * [0] : Nombre de fois où Dijkstra est plus rapide en mode distance
		 * [1] : Nombre de fois où A* est plus rapide en mode distance
		 * [2] : Nombre de fois où Dijkstra est plus rapide en mode temps
		 * [3] : Nombre de fois où A* est plus rapide en mode temps
		*/
		int statistiques[] = new int[4];
    	java.util.List<ArcInspector> filters = new ArrayList<ArcInspector>();
    	filters = ArcInspectorFactory.getAllFilters();
    	String mapName = "maps/haute-garonne/haute-garonne.mapgr";
    	ShortestPathSolution spsA, spsD;
    	AStarAlgorithm algoA;
    	DijkstraAlgorithm algoD;
    	GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
    	Graph graph = reader.read();
    	for (int i = 0 ; i < 100 ; i++) {
    		//On génère un couple aléatoire
    		Node origine = graph.get((int)(graph.size()*Math.random()));
    		Node destination = graph.get((int)(graph.size()*Math.random()));
    		System.out.println("Test N°" + (i+1));
    		for (int j = 0 ; j < 4 ; j++) {
        		//Permet de visualiser l'avancement des tests (debug)
    			switch(j) {
    			case 0 : System.out.println("Défaut        (distance) ["+ origine.getId() + ";" + destination.getId() + "]"); break;
    			case 1 : System.out.println("Voitures      (distance) ["+ origine.getId() + ";" + destination.getId() + "]"); break;
    			case 2 : System.out.println("Voitures      (temps)    ["+ origine.getId() + ";" + destination.getId() + "]"); break;
    			case 3 : System.out.println("Vélos/Piétons (temps)    ["+ origine.getId() + ";" + destination.getId() + "]"); break;
    			}
        		
        		algoD = new DijkstraAlgorithm(new ShortestPathData(graph, origine, destination, filters.get(j)));
            	algoA = new AStarAlgorithm(new ShortestPathData(graph, origine, destination, filters.get(j)));
            	spsD = algoD.run();
            	spsA = algoA.run();
            	//Test en distance
            	if (spsD.getPath() != null) {
            		//Tolérance de 100m
            		assertEquals(spsD.getPath().getLength(),spsA.getPath().getLength(), 100);
            	}
            	else
            		assertEquals(spsD.getPath(), spsA.getPath());
            	//On complète les statistiques
        		if (spsA.getSolvingTime().compareTo(spsD.getSolvingTime()) < 0) {
        			//System.out.println("A* plus rapide");
        			//Temps
        			if (j < 2) 
        				statistiques[1]++;
        			//Distance
        			else 
        				statistiques[3]++;
        		}
        		else {
        			//System.out.println("Dijkstra plus rapide");
        			//Temps
        			if (j < 2) 
        				statistiques[0]++;
        			//Distance
        			else 
        				statistiques[2]++;
        		}
    		}
    		System.out.println("***************************************");
    		
        } 	
    	System.out.println("***BILAN PERFORMANCE****");
    	System.out.println("	 Temps	Distance");
    	System.out.println("Dijkstra " + statistiques[2] + "   	" + statistiques [0]);
    	System.out.println("A*       " + statistiques[3] + "   	" + statistiques [1]);
    }
}
