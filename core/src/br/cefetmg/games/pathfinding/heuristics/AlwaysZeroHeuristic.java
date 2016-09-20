package br.cefetmg.games;

import br.cefetmg.games.pathfinding.TileNode;
import com.badlogic.gdx.ai.pfa.Heuristic;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
class AlwaysZeroHeuristic implements Heuristic<TileNode> {

    @Override
    public float estimate(TileNode currentNode, TileNode targetNode) {
        return 0;
    }
    
}
