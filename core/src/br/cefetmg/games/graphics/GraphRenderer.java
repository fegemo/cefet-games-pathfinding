package br.cefetmg.games.graphics;

import br.cefetmg.games.pathfinding.TileGraph;
import br.cefetmg.games.pathfinding.TileNode;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public class GraphRenderer {

    private final ShapeRenderer shapeRenderer;
    private final Camera camera;
    private final Color[] connectionColors = new Color[]{
        new Color(0.14f, 1, 0.14f, 0.4f),   // verde
        new Color(0.14f, 0.8f, 0.14f, 0.4f),// verde
        new Color(0.14f, 0.6f, 0.14f, 0.4f),// verde
        new Color(0.14f, 1, 1, 0.4f),       // amarelo
        new Color(0.14f, 0.8f, 0.8f, 0.4f), // amarelo
        new Color(0.14f, 0.6f, 0.6f, 0.4f), // amarelo
        new Color(1, 0.14f, 0.14f, 0.4f),   // vermelho
        new Color(0.7f, 0.14f, 0.14f, 0.4f),// vermelho
        new Color(0.5f, 0.14f, 0.14f, 0.4f) // vermelho
    };
    private final Color[] nodeColors = new Color[]{
        new Color(0.14f, 0.14f, 1, 0.6f),
        new Color(1, 0.14f, 0.14f, 0.6f)
    };

    public GraphRenderer(Camera camera) {
        shapeRenderer = new ShapeRenderer();
        this.camera = camera;
    }

    private Color getConnectionColor(Connection<TileNode> c) {
        return connectionColors[(int) ((c.getCost() - 1) % connectionColors.length)];
    }

    private void renderConnection(Connection<TileNode> c) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(getConnectionColor(c));
        shapeRenderer.line(c.getFromNode().getPosition(), c.getToNode().getPosition());
        shapeRenderer.end();
    }

    private void renderNode(TileNode n) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(nodeColors[n.isObstacle() ? 1 : 0]);
        shapeRenderer.translate(n.getPosition().x, n.getPosition().y, 0);
        shapeRenderer.circle(0, 0, 6);
        shapeRenderer.identity();
        shapeRenderer.end();
    }

    public void renderGraph(TileGraph g) {
        Array<TileNode> nodes = g.getAllNodes();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        for (TileNode n : nodes) {
            for (Connection<TileNode> c : n.getConnections()) {
                renderConnection(c);
            }
        }
        for (TileNode n : nodes) {
            renderNode(n);
        }
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
}
