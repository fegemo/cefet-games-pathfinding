package br.cefetmg.games.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public class TileConnection implements Connection<TileNode> {

    private final TileNode from, to;
    private final float cost;
    
    public TileConnection(TileNode from, TileNode to, float cost) {
        this.from = from;
        this.to = to;
        this.cost = cost;
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public TileNode getFromNode() {
        return from;
    }

    @Override
    public TileNode getToNode() {
        return to;
    }
}
