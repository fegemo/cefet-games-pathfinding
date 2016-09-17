package br.cefetmg.games.pathfinding;

import br.cefetmg.games.LevelManager;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public class TileGraph implements IndexedGraph<TileNode> {
    
    private final Array<TileNode> nodes;
    
    public TileGraph(Array<TileNode> nodes) {
        this.nodes = nodes;
    }

    public TileNode getNodeAtCoordinates(int x, int y) {
        int tileX = x / LevelManager.tileWidth;
        int tileY = y / LevelManager.tileHeight;
        
        return nodes.get(tileY * LevelManager.horizontalTiles + tileX);
    }
    
    public Array<TileNode> getAllNodes() {
        return nodes;
    }

    @Override
    public int getNodeCount() {
        return nodes.size;
    }

    @Override
    public Array<Connection<TileNode>> getConnections(TileNode n) {
        return n.getConnections();
    }

    @Override
    public int getIndex(TileNode node) {
        return node.getIndex();
    }
}
