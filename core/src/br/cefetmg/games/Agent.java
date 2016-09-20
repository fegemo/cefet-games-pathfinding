package br.cefetmg.games;

import br.cefetmg.games.graphics.Facing;
import br.cefetmg.games.movement.Position;
import br.cefetmg.games.movement.Steering;
import br.cefetmg.games.movement.Target;
import br.cefetmg.games.movement.behavior.Algorithm;
import br.cefetmg.games.movement.behavior.Seek;
import br.cefetmg.games.pathfinding.TileConnection;
import br.cefetmg.games.pathfinding.TileNode;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder.Metrics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import java.util.Iterator;

/**
 *
 * @author Flávio Coutinho <fegemo@gmail.com>
 */
public class Agent {

    public Position position;
    private Algorithm behavior;
    private final IndexedAStarPathFinder pathFinder;
    private final DefaultGraphPath<TileConnection> path;
    private Iterator<TileConnection> pathIterator;
    private final Target steeringTarget;
    private final float fullSpeed = 75;
    private static final float MIN_DISTANCE_CONSIDERED_ZERO_SQUARED = (float) Math.pow(2.0f, 2);
    private Facing facing;

    public Color color;
    private boolean shouldMove;

    public Agent(Vector2 position, Color color) {
        this.position = new Position(position);
        this.color = color;
        this.steeringTarget = new Target(position);
        this.behavior = new Seek(fullSpeed);
        this.behavior.target = steeringTarget;
        this.pathFinder = new IndexedAStarPathFinder(LevelManager.graph, true);
        this.path = new DefaultGraphPath<>();
        this.pathIterator = this.path.iterator();
        this.facing = Facing.EAST;
        this.shouldMove = false;
    }

    public void update(float delta) {
        shouldMove = true;

        // verifica se atingimos nosso objetivo imediato
        if (position.coords.dst2(steeringTarget.coords) < MIN_DISTANCE_CONSIDERED_ZERO_SQUARED) {
            // procurar se temos outra conexão na nossa rota
            // e, caso afirmativo, definir o nó de chegada como novo target
            if (shouldMove = pathIterator.hasNext()) {
                TileConnection nextConnection = pathIterator.next();
                nextNode = nextConnection.getToNode();
                steeringTarget.coords = nextNode.getPosition();

                // atualiza a velocidade do "seek" de acordo com o terreno (a conexão)
                this.behavior.maxSpeed = fullSpeed - (fullSpeed / 2.0f) * (nextConnection.getCost() - 1) / (LevelManager.maxCost - 1);
            }
        } else if (position.coords.dst2(steeringTarget.coords) < MIN_DISTANCE_CONSIDERED_ZERO_SQUARED * 6) {
            currentNode = nextNode;
        }

        // integra
        if (shouldMove) {
            Steering steering = behavior.steer(this.position);
            position.integrate(steering, delta);

            // verifica o vetor velocidade para determinar a orientação
            float angle = steering.velocity.angle();
            int quadrant = (int) (((int) angle + (360 - 67.5f)) / 45) % 8;
            facing = Facing.values()[(8 - quadrant) % 8];
        }
    }

    public void setGoal(int x, int y) {
        TileNode startNode = LevelManager.graph.getNodeAtCoordinates((int) this.position.coords.x, (int) this.position.coords.y);
        TileNode targetNode = LevelManager.graph.getNodeAtCoordinates(x, y);

        path.clear();
        pathFinder.metrics.reset();
//        pathFinder.searchConnectionPath(startNode, targetNode, new EuclideanDistanceHeuristic(), path);
        pathFinder.searchConnectionPath(startNode, targetNode, new AlwaysZeroHeuristic(), path);
        pathIterator = path.iterator();
    }

    public Facing getFacing() {
        return facing;
    }

    public boolean isMoving() {
        return shouldMove;
    }

    public Metrics getPathFindingMetrics() {
        return pathFinder.metrics;
    }

    public boolean isUnderWater() {
        return currentNode == null ? false : currentNode.isWater();
    }
}
