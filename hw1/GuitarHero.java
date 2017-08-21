import stdlib.StdAudio;
import stdlib.StdDraw;
import synthesizer.GuitarString;

/**
 * A client that uses the synthesizer package to create a a 37 key synthesizer
 * @author moboa
 */
public class GuitarHero {
    public static void main(String[] args) {
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        GuitarString[] strings = new GuitarString[keyboard.length()];

        for (int i = 0; i < keyboard.length(); i++) {
            double frequency = 440 * Math.pow(2, (i - 24) / 12);
            strings[i] = new GuitarString(frequency);
        }

        while (true) {
            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int indexOfGuitarString = keyboard.indexOf(key);
                if (indexOfGuitarString != -1) {
                    strings[indexOfGuitarString].pluck();
                }
            }

        /* compute the superposition of samples */
            double sample = 0;
            for (GuitarString string : strings) {
                sample += string.sample();
            }

        /* play the sample on standard audio */
            StdAudio.play(sample);

        /* advance the simulation of each guitar string by one step */
            for (GuitarString string : strings) {
                string.tic();
            }
        }
    }
}
