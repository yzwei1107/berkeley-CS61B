// TODO: Make sure to make this class a part of the synthesizer package
//package <package name>;
package synthesizer;

import java.lang.management.BufferPoolMXBean;
import java.util.HashSet;
import java.util.Set;

/**
 * Simulation of a guitar string using the Karplus-Strong algorithm.
 * @author moboa
 *
 * The algorithm is simply the following three steps:
 * 1. Replace every item in a BoundedQueue with random noise (double values between -0.5 and 0.5).
 * 2. Remove the front double in the BoundedQueue and average it with the next double in the BQ.
 * 3. Play the double that you dequeued in step 2. Go back to step 2 (repeating forever).
 *
 */
public class GuitarString {
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int bufferCapacity = (int) Math.round(SR / frequency);
        buffer = new ArrayRingBuffer<>(bufferCapacity);

        while (!buffer.isFull()) {
            buffer.enqueue(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise
     * (step 1 of the algorithm).
     */
    public void pluck() {
        while(!buffer.isEmpty()) {
            buffer.dequeue();
        }

        /* The doubles in the buffer must be unique. */
        Set<Double> randomDoubleSet = new HashSet<>();

        while(randomDoubleSet.size() < buffer.capacity()) {
            randomDoubleSet.add(Math.random() - 0.5);
        }

        for (Double r : randomDoubleSet) {
            buffer.enqueue(r);
        }

    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm (step 2).
     */
    public void tic() {
        double newAveragedDouble = DECAY * (buffer.dequeue() + buffer.peek()) / 2;
        buffer.enqueue(newAveragedDouble);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.peek();
    }
}
