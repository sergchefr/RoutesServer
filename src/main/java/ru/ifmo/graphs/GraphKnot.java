package ru.ifmo.graphs;

import java.util.ArrayList;

public class GraphKnot {
    private ArrayList<GraphRoute> routes;
    private int id;

    public GraphKnot(int id) {
        this.id = id;
    }

    public void addRoute(GraphRoute route){
        routes.add(route);
    }
}
