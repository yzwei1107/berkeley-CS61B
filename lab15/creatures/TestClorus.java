package creatures;

import huglife.Empty;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.Impassible;
import org.junit.Test;

import java.awt.Color;
import java.util.HashMap;

import static org.junit.Assert.*;

public class TestClorus {

    @Test
    public void testBasics() {
        Clorus c = new Clorus(1);
        c.move();
        assertEquals(0.97, c.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), c.color());
        c.stay();
        assertEquals(0.96, c.energy(), 0.01);
        Plip p = new Plip(1.04);
        c.attack(p);
        assertEquals(2, c.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Clorus c = new Clorus(2);
        Clorus offspring = c.replicate();

        assertEquals(1, c.energy(), 0.01);
        assertEquals(1, offspring.energy(), 0.01);
        assertNotSame(c, offspring);
    }

    @Test
    public void testChoose() {
        Clorus c = new Clorus(5);

        HashMap<Direction, Occupant> noEmptySpaces = new HashMap<Direction, Occupant>();
        noEmptySpaces.put(Direction.TOP, new Plip());
        noEmptySpaces.put(Direction.BOTTOM, new Impassible());
        noEmptySpaces.put(Direction.LEFT, new Plip());
        noEmptySpaces.put(Direction.RIGHT, new Plip());
        Action actual = c.chooseAction(noEmptySpaces);
        Action expected = new Action(Action.ActionType.STAY);
        assertEquals(expected, actual);

        HashMap<Direction, Occupant> plipsInSight = new HashMap<Direction, Occupant>();
        plipsInSight.put(Direction.TOP, new Plip());
        plipsInSight.put(Direction.BOTTOM, new Empty());
        plipsInSight.put(Direction.LEFT, new Plip());
        plipsInSight.put(Direction.RIGHT, new Plip());
        assertEquals(Action.ActionType.ATTACK, c.chooseAction(plipsInSight).type);

        HashMap<Direction, Occupant> oneClorus = new HashMap<Direction, Occupant>();
        oneClorus.put(Direction.TOP, new Clorus(1));
        oneClorus.put(Direction.BOTTOM, new Empty());
        oneClorus.put(Direction.LEFT, new Empty());
        oneClorus.put(Direction.RIGHT, new Empty());
        assertEquals(Action.ActionType.REPLICATE, c.chooseAction(oneClorus).type);

        c = new Clorus(0.5);
        assertEquals(Action.ActionType.MOVE, c.chooseAction(oneClorus).type);
    }
}
