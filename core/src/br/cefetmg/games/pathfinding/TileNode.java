package br.cefetmg.games.pathfinding;

import br.cefetmg.games.LevelManager;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public class TileNode {

    private final Array<Connection<TileNode>> connections = new Array<>();
    private final int index;
    private boolean isObstacle;
    private boolean isWater;
    private Vector2 position;
    
    public TileNode() {
        index = Incrementer.nextIndex();
        position = Vector2.Zero;
    }
    
    public int getIndex() {
        return index;
    }

    /**
     * @return the isObstacle
     */
    public boolean isObstacle() {
        return isObstacle;
    }

    /**
     * @param isObstacle the isObstacle to set
     */
    public void setIsObstacle(boolean isObstacle) {
        this.isObstacle = isObstacle;
    }

    /**
     * Retorna a posição (x,y) no mundo (na janela, em pixels).
     * 
     * @return the position
     */
    public Vector2 getPosition() {
        return position;
    }
    
    /**
     * Retorna a coordenada (x,y) no grid de tiles.
     * (x,y) são os índices (começando de 0) com a ordem dos tiles - são
     * sempre números inteiros não-negativos, começando no canto esquerdo
     * inferior.
     * 
     * @return 
     */
    public Vector2 getTilePosition() {
        return new Vector2(
                ((int)position.x)/LevelManager.tileWidth,
                ((int)position.y)/LevelManager.tileHeight
        );
    }
    
    /**
     * @param position the position to set
     */
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Array<Connection<TileNode>> getConnections() {
        return connections;
    }

    public boolean isWater() {
        return isWater;
    }
    
    public void setIsWater(boolean water) {
        isWater = water;
    }
    
    private static class Incrementer {
        private static int id = 0;
        public static int nextIndex() {
            return id++;
        }
    }
    
    public void createConnection(TileNode to, float cost) {
        connections.add(new TileConnection(this, to, cost));
    }
}
