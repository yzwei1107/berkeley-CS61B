import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    protected int period;
    protected int state;

    public SawToothGenerator(int period) {
        this.state = 0;
        this.period = period;
    }

    @Override
    public double next() {
        state++;
        state %= period;
        return normalize();
    }

    protected double normalize() {
        return (2 * (double) state / period) - 1;
    }
}
