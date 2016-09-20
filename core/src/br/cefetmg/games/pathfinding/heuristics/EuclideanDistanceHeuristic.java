package br.cefetmg.games.pathfinding.heuristics;

import br.cefetmg.games.LevelManager;
import br.cefetmg.games.pathfinding.TileNode;
import com.badlogic.gdx.ai.pfa.Heuristic;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public class EuclideanDistanceHeuristic implements Heuristic<TileNode> {

    @Override
    public float estimate(TileNode currentNode, TileNode targetNode) {
        return currentNode.getPosition().dst(targetNode.getPosition())/LevelManager.maxCost;
    }
    
}
