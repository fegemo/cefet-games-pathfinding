package br.cefetmg.games.graphics;

import br.cefetmg.games.pathfinding.TileGraph;
import br.cefetmg.games.pathfinding.TileNode;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public class GraphRenderer {

    private final ShapeRenderer shapeRenderer;
    private final SpriteBatch batch;
    private final float connectionAlpha = 1;
    private final float nodeAlpha = 1;
    private final Color[] connectionColors = new Color[]{
        new Color(0.14f, 1, 0.14f, connectionAlpha), // verde
        new Color(0.14f, 0.8f, 0.14f, connectionAlpha),// verde
        new Color(0.14f, 0.6f, 0.14f, connectionAlpha),// verde
        new Color(0.14f, 1, 1, connectionAlpha), // amarelo
        new Color(0.14f, 0.8f, 0.8f, connectionAlpha), // amarelo
        new Color(0.14f, 0.6f, 0.6f, connectionAlpha), // amarelo
        new Color(1, 0.14f, 0.14f, connectionAlpha), // vermelho
        new Color(0.7f, 0.14f, 0.14f, connectionAlpha),// vermelho
        new Color(0.5f, 0.14f, 0.14f, connectionAlpha) // vermelho
    };
    private final Color[] nodeColors = new Color[]{
        new Color(0.14f, 0.14f, 1, nodeAlpha),
        new Color(1, 0.14f, 0.14f, nodeAlpha)
    };

    private final FrameBuffer fbo;
    private TextureRegion offlineRenderedGraph;

    public GraphRenderer(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        this.batch = batch;
        this.shapeRenderer = shapeRenderer;
        fbo = new FrameBuffer(Pixmap.Format.RGBA8888,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
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
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(nodeColors[n.isObstacle() ? 1 : 0]);
        shapeRenderer.translate(n.getPosition().x, n.getPosition().y, 0);
        shapeRenderer.circle(0, 0, 4);
        shapeRenderer.identity();
        shapeRenderer.end();
    }

    public void renderGraphToTexture(TileGraph g) {
        fbo.begin();
        renderGraph(g);
        fbo.end();
        offlineRenderedGraph = new TextureRegion(fbo.getColorBufferTexture());
        offlineRenderedGraph.flip(false, true);
    }

    public void renderOffScreenedGraph() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.setColor(1, 1, 1, 0.65f);
        batch.draw(offlineRenderedGraph, 0, 0);
        batch.setColor(1, 1, 1, 1);
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void renderGraph(TileGraph g) {
        Array<TileNode> nodes = g.getAllNodes();

        for (TileNode n : nodes) {
            for (Connection<TileNode> c : n.getConnections()) {
                renderConnection(c);
            }
        }
        for (TileNode n : nodes) {
            renderNode(n);
        }
    }
}
