package br.cefetmg.games.pathfinding;

import br.cefetmg.games.LevelManager;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public class GraphGenerator {

    public static TileGraph generateGraph(TiledMap map) {
        Array<TileNode> nodes = new Array<>();
        TiledMapTileLayer floor = (TiledMapTileLayer) map.getLayers().get(0);
//        TiledMapTileLayer objects = (TiledMapTileLayer) map.getLayers().get(1);

        for (int i = 0; i < LevelManager.verticalTiles; i++) {
            for (int j = 0; j < LevelManager.horizontalTiles; j++) {
                TileNode newNode = new TileNode();
                newNode.setIsObstacle(isObstacle(map, j, i));
                newNode.setIsWater(isWater(map, j, i));
                newNode.setPosition(
                        new Vector2(
                                j * LevelManager.tileWidth + LevelManager.tileWidth / 2,
                                i * LevelManager.tileHeight + LevelManager.tileHeight / 2
                        )
                );

                nodes.add(newNode);
            }
        }

        for (int i = 0; i < floor.getHeight(); i++) {
            for (int j = 0; j < floor.getWidth(); j++) {
                if (nodes.get(i * LevelManager.horizontalTiles + j).isObstacle()) {
                    continue;
                }
                connectWith(j, i, nodes, map, 1, 0);
                connectWith(j, i, nodes, map, 1, 1);
                connectWith(j, i, nodes, map, 0, 1);
                connectWith(j, i, nodes, map, -1, 1);
                connectWith(j, i, nodes, map, -1, 0);
                connectWith(j, i, nodes, map, -1, -1);
                connectWith(j, i, nodes, map, 0, -1);
                connectWith(j, i, nodes, map, 1, -1);
            }
        }

        return new TileGraph(nodes);
    }

    private static int connectWith(int j, int i, Array<TileNode> nodes, TiledMap map, int jOffset, int iOffset) {
        if (!isBetween(j + jOffset, 0, LevelManager.horizontalTiles - 1)
                || !isBetween(i + iOffset, 0, LevelManager.verticalTiles - 1)) {
            return 0;
        }
        int otherIndex = (i + iOffset) * LevelManager.horizontalTiles + (j + jOffset);

        TileNode n = nodes.get(i * LevelManager.horizontalTiles + j);
        TileNode other = nodes.get(otherIndex);

        if (other != null && !other.isObstacle()) {
            //Cell otherCell = tiles.getCell(j + jOffset, i + iOffset);
            if (!other.isObstacle()) {
                //n.createConnection(other, getCost(otherCell.getTile()));
                n.createConnection(other, getCost(map, j + jOffset, i + iOffset));
            }
        }
        return 1;
    }

    private static boolean isBetween(int n, int min, int max) {
        return n >= min && n <= max;
    }

    private static Array<Cell> getCellsAt(TiledMap map, int j, int i) {
        TiledMapTileLayer floor = (TiledMapTileLayer) map.getLayers().get(0);
        TiledMapTileLayer objects = (TiledMapTileLayer) map.getLayers().get(1);

        Array<Cell> cells = new Array<>(2);
        Cell floorCell = floor.getCell(j, i);
        Cell objectCell = objects.getCell(j, i);
        
        if (floorCell != null) cells.add(floorCell);
        if (objectCell != null) cells.add(objectCell);

        return cells;
    }

    private static boolean isObstacle(TiledMap map, int j, int i) {
        boolean isObstacle = false;
        for (Cell c : getCellsAt(map, j, i)) {
            MapProperties props = c.getTile().getProperties();
            isObstacle |= Boolean.parseBoolean(props.get("obstacle", "false", String.class));
        }
        return isObstacle;
    }

    private static boolean isWater(TiledMap map, int j, int i) {
        boolean isWater = false;
        for (Cell c : getCellsAt(map, j, i)) {
            MapProperties props = c.getTile().getProperties();
            isWater |= Boolean.parseBoolean(props.get("water", "false", String.class));
        }
        return isWater;
    }

    private static float getCost(TiledMap map, int j, int i) {
        int cost = 0;
        for (Cell c : getCellsAt(map, j, i)) {
            MapProperties props = c.getTile().getProperties();
            cost += Integer.parseInt(props.get("cost", "1", String.class))
                    + Integer.parseInt(props.get("cost_adder", "0", String.class));
            String override = props.get("cost_overrider", String.class);
            if (override != null) {
                cost = Integer.parseInt(override);
            }
        }
        return cost;
    }

}
