package br.cefetmg.games.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder.Metrics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 *
 * @author fegemo <coutinho@decom.cefetmg.br>
 */
public class MetricsRenderer {

    private final SpriteBatch batch;
    private final ShapeRenderer shapes;
    private final BitmapFont font;

    public MetricsRenderer(SpriteBatch batch, ShapeRenderer shapes,
            BitmapFont font) {
        this.batch = batch;
        this.shapes = shapes;
        this.font = font;
        font.setColor(Color.WHITE);
    }

    public void render(Metrics metrics, int totalNodes) {
        float verticalSpacing = 5;
        float fontHeight = font.getLineHeight() + verticalSpacing;
        float initialY = fontHeight + verticalSpacing;
        float initialX = 10;

        // desenha o fundo semi-transparente
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapes.setColor(0, 0, 0, 0.25f);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.rect(0, 0,
                Gdx.graphics.getWidth(),
                initialY + fontHeight + verticalSpacing);
        shapes.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // desenha o texto com as métricas
        batch.begin();
        font.draw(batch, String.format("Total de nós: %d", totalNodes), initialX,
                initialY + fontHeight);
        font.draw(batch, String.format("Nós visitados: %d (%.2f%%)",
                metrics.visitedNodes,
                (((float) metrics.visitedNodes * 100) / totalNodes)),
                initialX, initialY);
        batch.end();
    }
}
