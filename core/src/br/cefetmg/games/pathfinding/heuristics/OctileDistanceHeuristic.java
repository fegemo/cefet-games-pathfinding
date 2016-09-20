package br.cefetmg.games.pathfinding.heuristics;

import br.cefetmg.games.pathfinding.TileNode;
import com.badlogic.gdx.ai.pfa.Heuristic;

/**
 * Fonte da Octile Distance: http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class OctileDistanceHeuristic implements Heuristic<TileNode> {

    @Override
    public float estimate(TileNode n, TileNode n1) {
        int dx = (int) Math.abs(n.getTilePosition().x - n1.getTilePosition().x);
        int dy = (int) Math.abs(n.getTilePosition().y - n1.getTilePosition().y);
        return (float) (dx + dy + (Math.sqrt(2) - 2) * Math.min(dx, dy));
    }
    
}
