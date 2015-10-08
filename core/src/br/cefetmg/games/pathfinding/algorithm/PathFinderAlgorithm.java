package br.cefetmg.games.pathfinding.algorithm;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 * @param <N>
 */
public interface PathFinderAlgorithm<N> {
    /**
     * Asks the algorithm to find a path from the `startNode` to `targetNode`.
     * @param startNode Initial graph node, where the agent is.
     * @param targetNode Target graph node, where it wants to go.
     * @param heuristic An object following the Heuristic interface that 
     *                  calculates an estimate of the remaining effort to get 
     *                  from the current graph node to the target node.
     * @param outPath The resulting path - list of graph connections to follow.
     */
    void search(N startNode, N targetNode, GraphPath<Connection<N>> outPath);
}
