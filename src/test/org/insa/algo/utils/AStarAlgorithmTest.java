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
	//VALIDITE
	//Test avancé, on génère 10 couples de points aléatoires et on compare les résultats de A* et de Dijkstra
    //pour chaque couple (on compare juste les longueurs des paths retournés
    public void randomTest() throws Exception{
    	java.util.List<ArcInspector> filters = new ArrayList<ArcInspector>();
    	filters = ArcInspectorFactory.getAllFilters();
    	String mapName = "maps/haute-garonne/haute-garonne.mapgr";
    	ShortestPathSolution spsA, spsD;
    	AStarAlgorithm algoA;
    	DijkstraAlgorithm algoD;
    	GraphReader reader = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapName))));
    	Graph graph = reader.read();
    	for (int i = 0 ; i < 10 ; i++) {
    		//On génère un couple aléatoire
    		Node origine = graph.get((int)(graph.size()*Math.random()));
    		Node destination = graph.get((int)(graph.size()*Math.random()));
    		for (int j = 0 ; j < 3 ; j++) {
        		//Permet de visualiser l'avancement des tests
    			switch(j) {
    			case 0 : System.out.println("Défaut (longueur) ["+ origine.getId() + ";" + destination.getId() + "]"); break;
    			case 1 : System.out.println("Voitures (distance) ["+ origine.getId() + ";" + destination.getId() + "]"); break;
    			case 2 : System.out.println("Voitures (temps) ["+ origine.getId() + ";" + destination.getId() + "]"); break;
    			}
        		
        		algoD = new DijkstraAlgorithm(new ShortestPathData(graph, origine, destination, filters.get(j)));
            	algoA = new AStarAlgorithm(new ShortestPathData(graph, origine, destination, filters.get(j)));
            	spsD = algoD.doRun();
            	spsA = algoA.doRun();
            	System.out.println(spsD.getSolvingTime());
            	//Test en distance
            	if (spsD.getPath() != null) {
            		assertEquals(spsD.getPath().getLength(),spsA.getPath().getLength(), 0);
            		if (spsD.getSolvingTime() != null)
            			assertTrue(0 > spsA.getSolvingTime().compareTo(spsD.getSolvingTime()));
            	}
            	else
            		assertEquals(spsD.getPath(), spsA.getPath());
        		System.out.println("	---> OK !");
    		}
        } 	
    }
}
