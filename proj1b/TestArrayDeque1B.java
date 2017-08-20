/**
 * Created by moboa on 18/08/2017.
 */

import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDeque1B {

    /* Test the add and remove functionality of the ArrayDeque */
    @Test
    public void addRemoveTest() {
        StudentArrayDeque<Integer> studentDeque = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> solutionDeque = new ArrayDequeSolution<>();

        Integer expected = null;
        Integer actual = null;
        DequeOperation currentMethod = null;
        OperationSequence methodsCalled = new OperationSequence();

        while (true) {
            int randomIntFromZeroToThree = StdRandom.uniform(4);

            switch (randomIntFromZeroToThree) {
                case 0 :
                    studentDeque.addFirst(randomIntFromZeroToThree);
                    solutionDeque.addFirst(randomIntFromZeroToThree);

                    currentMethod = new DequeOperation("addFirst", randomIntFromZeroToThree);
                    expected = solutionDeque.get(0);
                    actual = studentDeque.get(0);

                    break;

                case 1 :
                    studentDeque.addLast(randomIntFromZeroToThree);
                    solutionDeque.addLast(randomIntFromZeroToThree);

                    currentMethod = new DequeOperation("addLast", randomIntFromZeroToThree);
                    expected = solutionDeque.get(solutionDeque.size() - 1);
                    actual = studentDeque.get(studentDeque.size() - 1);

                    break;

                case 2 :
                    /* If one of these arrays is empty, we don't want to call a remove function
                       as a NullPointerException would be thrown*/
                    if (studentDeque.isEmpty() || solutionDeque.isEmpty()) {
                        continue;
                    }

                    currentMethod = new DequeOperation("removeFirst");
                    expected = solutionDeque.removeFirst();
                    actual = studentDeque.removeFirst();

                    break;

                case 3 :
                    if (studentDeque.isEmpty() || solutionDeque.isEmpty()) {
                        continue;
                    }

                    currentMethod = new DequeOperation("removeLast");
                    expected = solutionDeque.removeLast();
                    actual = studentDeque.removeLast();

                    break;

                default :
                    System.out.println("No option for " + randomIntFromZeroToThree);
                    fail();
            }

            methodsCalled.addOperation(currentMethod);

            assertEquals(methodsCalled.toString(), expected, actual);
        }

    }
}
