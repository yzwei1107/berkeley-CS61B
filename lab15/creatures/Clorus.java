package creatures;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.HugLifeUtils;
import huglife.Occupant;

import java.awt.Color;
import java.util.List;
import java.util.Map;

/**
 *  An implemenation of a fierce blue-colored predator that feasts on Plips.
 *  @author moboa
 */

public class Clorus extends Creature {

    /** red color. */
    private static final int R = 34;
    /** green color. */
    private static final int G = 0;
    /** blue color. */
    private static final int B = 231;

    public Clorus(double energy) {
        super("clorus");
        this.energy = energy;
    }
    @Override
    public void move() {
        energy -= 0.03;
    }

    @Override
    public void attack(Creature c) {
        energy += c.energy();
    }

    @Override
    public Clorus replicate() {
        energy /= 2;
        return new Clorus(energy);
    }

    @Override
    public void stay() {
        energy -= 0.01;
    }

    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> emptySpaces = getNeighborsOfType(neighbors, "empty");
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");

        if (emptySpaces.isEmpty()) {
            return new Action(Action.ActionType.STAY);
        }

        if (!plips.isEmpty()) {
            Direction moveDir = HugLifeUtils.randomEntry(plips);
            return new Action(Action.ActionType.ATTACK, moveDir);
        }

        if (energy >= 1) {
            Direction moveDir = HugLifeUtils.randomEntry(emptySpaces);
            return new Action(Action.ActionType.REPLICATE, moveDir);
        }

        Direction moveDir = HugLifeUtils.randomEntry(emptySpaces);
        return new Action(Action.ActionType.MOVE, moveDir);
    }

    @Override
    public Color color() {
        return color(R, G, B);
    }
}
