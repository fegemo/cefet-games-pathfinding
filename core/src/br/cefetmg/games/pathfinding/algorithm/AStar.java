package br.cefetmg.games.pathfinding.algorithm;

import br.cefetmg.games.pathfinding.TileConnection;
import br.cefetmg.games.pathfinding.TileNode;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode;
import com.badlogic.gdx.utils.Array;
import java.util.HashMap;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public class AStar implements PathFinderAlgorithm<TileNode> {

    private final Heuristic<TileNode> heuristic;
    private IndexedGraph<TileNode> graph;

    private final HashMap<TileNode, NodeRecord> openNodesList;
    private final HashMap<TileNode, NodeRecord> closedNodesList;

    public AStar(IndexedGraph<TileNode> graph, Heuristic<TileNode> heuristic) {
        this.heuristic = heuristic;
        this.graph = graph;
        this.openNodesList = new HashMap<>();
        this.closedNodesList = new HashMap<>();
    }

    @Override
    public void search(TileNode startNode, TileNode targetNode, GraphPath<Connection<TileNode>> outPath) {
//        throw new UnsupportedOperationException("Você deve implementar a "
//                + "função 'search' do AStar.java e do Dijkstra.java.");
        // Inicializa o algoritmo
        initializeSearch(startNode);

        // Entra no loop - só termina quando a lista de abertos fica vazia
        NodeRecord current = null;
        Array<Connection<TileNode>> connections;
        while (openNodesList.size() > 0) {
            current = smallestNodeRecordFromOpenList();

            if (current.node == targetNode) {
                break;
            }

            connections = graph.getConnections(current.node);

            for (Connection<TileNode> c : connections) {
                TileNode endingNode = c.getToNode();
                NodeRecord endingNodeRecord = null;

                float endingNodeCost = current.costSoFar + c.getCost();

                if (closedNodesList.containsKey(endingNode)) {
                    continue;
                } else if (openNodesList.containsKey(endingNode)) {
                    endingNodeRecord = openNodesList.get(endingNode);
                    if (endingNodeRecord.costSoFar <= endingNodeCost) {
                        continue;
                    }
                } else {
                    endingNodeRecord = new NodeRecord(endingNode, (TileConnection) c);
                }
                
                 endingNodeRecord.connectionThatLedHere = (TileConnection) c;
                endingNodeRecord.costSoFar = endingNodeCost;
                
                if (!openNodesList.containsKey(endingNode)) {
                    openNodesList.put(endingNode, endingNodeRecord);
                }
                
                openNodesList.remove(current.node);
            }
            closedNodesList.put(current.node, current);
        }

        // Monta a resposta, navegando no sentido inverso da lista de NodeRecords
        if (current.node != targetNode) {
            return;
        } else {
            outPath.clear();
            while (current.node != startNode) {
                outPath.add(current.connectionThatLedHere);
                current = closedNodesList.get(current.connectionThatLedHere.getFromNode());
            }
            
            outPath.reverse();
        }
    }

    private void initializeSearch(TileNode startNode) {
        NodeRecord startRecord = new NodeRecord(startNode, null);
        startRecord.costSoFar = 0;

        openNodesList.clear();
        openNodesList.put(startNode, startRecord);
        closedNodesList.clear();
    }

    private NodeRecord smallestNodeRecordFromOpenList() {
        float minimum = Float.MAX_VALUE;
        NodeRecord smallest = null;
        for (NodeRecord r : openNodesList.values()) {
            if (r.costSoFar < minimum) {
                minimum = r.costSoFar;
                smallest = r;
            }
        }

        return smallest;
    }

    static class NodeRecord {

        TileNode node;
        TileConnection connectionThatLedHere;
        float costSoFar;

        public NodeRecord(TileNode node, TileConnection c) {
            this.node = node;
            this.connectionThatLedHere = c;
        }
    }

}
