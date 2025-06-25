package ru.ifmo.clientCommands;

import com.sun.source.tree.BreakTree;
import ru.ifmo.coll.IRoutesHandler;
import ru.ifmo.coll.Location;
import ru.ifmo.coll.Route;
import ru.ifmo.graphs.GraphRoute;
import ru.ifmo.transfer.Request;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class GetindependentCommand implements Icommand{
    private IRoutesHandler executor;
    private String Commandname;

    public GetindependentCommand(IRoutesHandler executor) {
        this.executor = executor;
    }



    @Override
    public String execute(Request command) {
        Route[] allRoutes = executor.getAllRoutes();
        if(allRoutes==null|allRoutes.length==0) return "найдено 0 групп маршрутов";
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        var ans = forkJoinPool.invoke(new IncrementTask(executor.getAllRoutes()));
        StringBuilder builder = new StringBuilder("найдено "+ans.length+" связных графа. их узлы представлены ниже.\n\n");
        for (Graph an : ans) {
            builder.append(an).append('\n');
        }
        return builder.toString().strip();
    }

    @Override
    public String getName() {
        return "get_groups";
    }

    class IncrementTask extends RecursiveTask<Graph[]> {
        private Graph[] graphs;

        public IncrementTask(Route[] routes) {
            ArrayList<Graph> t1 = new ArrayList<>();
            for (Route route : routes) {
                t1.add(new Graph(route));
            }
            graphs = t1.toArray(new Graph[0]);
        }

        private IncrementTask(Graph[] graphs){
            this.graphs=graphs;
        }

        @Override
        protected Graph[] compute() {
            if(graphs==null|graphs.length==1){
                return graphs;
            }else{
                IncrementTask leftTask = new IncrementTask(Arrays.copyOfRange(graphs,0, graphs.length/2));
                IncrementTask rightTask = new IncrementTask(Arrays.copyOfRange(graphs,graphs.length/2, graphs.length));

                leftTask.fork();
                rightTask.fork();

                ArrayList<Graph> leftres = new ArrayList<>(Arrays.asList(leftTask.join()));
                ArrayList<Graph> rightres = new ArrayList<>(Arrays.asList(rightTask.join()));
                //Graph[] leftres = leftTask.join();
                //Graph[] rightres = rightTask.join();

                for (Graph rightelem : rightres) {
                    boolean joined = false;
                    for (Graph leftelem : leftres) {
                        if(leftelem.joinable(rightelem)){
                            leftelem.join(rightelem);
                            joined = true;
                            break;
                        }
                    }
                    if(!joined) leftres.add(rightelem);
                }

                return leftres.toArray(new Graph[0]);
            }
        }
    }

    class Graph{
        private Set<Route> routes=new HashSet<>();
        private Set<Location> knots = new HashSet<>();


        public Graph(Route firstRoute) {
            routes.add(firstRoute);
            knots.add(firstRoute.getFromLocation());
            knots.add(firstRoute.getToLocation());
        }

        private void addRoute(Route route){
            routes.add(route);
            knots.add(route.getFromLocation());
            knots.add(route.getToLocation());
        }

        public void join(Graph graph){
            for (Route route : graph.getRoutes()) {
                addRoute(route);
            }
        }

        public boolean joinable(Graph graph){
            for (Location knot : knots) {
                for (Location location : graph.getLocations()) {
                    if(location.equals(knot)) return true;
                }
            }
            return false;
        }

        public Set<Route> getRoutes(){
            return routes;
        }

        public Set<Location> getLocations(){
            return knots;
        }

        @Override
        public String toString() {
            StringBuilder builer = new StringBuilder();
            for (Location knot : knots) {
                builer.append(knot.getName()).append(", ");
            }
            return builer.substring(0, builer.length()-2);
        }
    }
}
