public class AcceleratingSawToothGenerator extends SawToothGenerator {
    private double resetFactor;

    public AcceleratingSawToothGenerator(int period, double resetFactor) {
        super(period);
        this.resetFactor = resetFactor;
    }

    @Override
    public double next() {
        state++;
        if (state == period) {
            state = 0;
            period *= resetFactor;
        } else {
            state %= period;
        }
        return normalize();
    }

}
