package br.cefetmg.games.pathfinding;

import br.cefetmg.games.LevelManager;
import com.badlogic.gdx.ai.pfa.indexed.DefaultIndexedGraph;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public class TileGraph extends DefaultIndexedGraph<TileNode> {
    
    
    public TileGraph(Array<TileNode> nodes) {
        super(nodes);
    }

    public TileNode getNodeAtCoordinates(int x, int y) {
        int tileX = x / LevelManager.tileWidth;
        int tileY = y / LevelManager.tileHeight;
        
        return super.nodes.get(tileY * LevelManager.horizontalTiles + tileX);
    }
    
    public Array<TileNode> getAllNodes() {
        return nodes;
    }
}
