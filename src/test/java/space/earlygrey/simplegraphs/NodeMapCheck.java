package space.earlygrey.simplegraphs;

import squidpony.squidgrid.Direction;
import squidpony.squidgrid.mapping.DungeonGenerator;
import squidpony.squidmath.Coord;
import squidpony.squidmath.GreasedRegion;
import squidpony.squidmath.StatefulRNG;

public class NodeMapCheck {
    public int DIMENSION = 28;
    public DungeonGenerator dungeonGen = new DungeonGenerator(DIMENSION, DIMENSION, new StatefulRNG(0x1337BEEFDEAL));
    public char[][] map;
    public GreasedRegion floors;
    public int floorCount;
    public Coord[] floorArray;
    public Coord[][] nearbyMap;
    public StatefulRNG srng;

    public DirectedGraph<Coord> simpleDirectedGraph;
    public UndirectedGraph<Coord> simpleUndirectedGraph;

    public space.earlygrey.simplegraphs.utils.Heuristic<Coord> simpleHeu;

    public NodeMapCheck(){
        Coord.expandPoolTo(DIMENSION, DIMENSION);
        map = dungeonGen.generate();
        floors = new GreasedRegion(map, '.');
        floorCount = floors.size();
        floorArray = floors.asCoords();
        System.out.println("Floors: " + floorCount);
        System.out.println("Percentage walkable: " + floorCount * 100.0 / (DIMENSION * DIMENSION) + "%");
        nearbyMap = new Coord[DIMENSION][DIMENSION];
        GreasedRegion tmp = new GreasedRegion(DIMENSION, DIMENSION);
        srng = new StatefulRNG(0x1337BEEF1337CA77L);
        Coord c;
        for (int i = 1; i < DIMENSION - 1; i++) {
            for (int j = 1; j < DIMENSION - 1; j++) {
                if(map[i][j] == '#')
                    continue;
                c = tmp.empty().insert(i, j).flood(floors, 8).remove(i, j).singleRandom(srng);
                nearbyMap[i][j] = c;
            }
        }

        simpleDirectedGraph = new DirectedGraph<>(floors);

        // should print true
        System.out.println(floors.contains(Coord.get(22, 21)));
        // should print true. Before, it did not due to get() not seeing the Coord in the NodeMap
        System.out.println(simpleDirectedGraph.getVertices().contains(Coord.get(22, 21)));

        simpleUndirectedGraph = new UndirectedGraph<>(floors);

        simpleHeu = new space.earlygrey.simplegraphs.utils.Heuristic<Coord>() {
            @Override
            public float getEstimate(Coord currentNode, Coord targetNode) {
                return Math.max(Math.abs(currentNode.x - targetNode.x), Math.abs(currentNode.y - targetNode.y));
            }
        };
        Coord center;
        Direction[] outer = Direction.CLOCKWISE;
        Direction dir;
        for (int i = floorCount - 1; i >= 0; i--) {
            center = floorArray[i];
            for (int j = 0; j < 8; j++) {
                dir = outer[j];
                if(floors.contains(center.x + dir.deltaX, center.y + dir.deltaY))
                {
                    simpleDirectedGraph.addEdge(center, center.translate(dir));
                    if(!simpleUndirectedGraph.edgeExists(center, center.translate(dir)))
                    {
                        simpleUndirectedGraph.addEdge(center, center.translate(dir));
                    }
                }
            }
        }
    }

    public static void main(String[] args){
        NodeMapCheck nmc = new NodeMapCheck();
        System.out.println(nmc.floorCount);
        System.out.println(nmc.simpleDirectedGraph.getVertices().size());
    }
}
