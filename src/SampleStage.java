
import java.util.List;
import org.segonds.elevators.model.*;
import java.util.Random;

/**
 *
 * @author cjyuan
 * 
 * This class implement a simulation stage in which the passengers are generated
 * randomly.
 */
public class SampleStage extends ScheduledStage {

    int seed;

    public SampleStage(int length, int seed) {
        super(length);
        this.seed = seed;
    }

    void generatePassengers(Building building) {
        Random rand = new Random(seed);
        int length = (int) getLength().getTicks();
        int passengers = length / 30;
        int nbFloors = building.getNbFloors();

        for (int i = 0; i < passengers; i++) {
            int clock, src, dst;
            clock = rand.nextInt(length) + 1;
            src = rand.nextInt(nbFloors);
            dst = rand.nextInt(nbFloors);

            // Make sure src != dst
            while (dst == src) {
                dst = rand.nextInt(nbFloors);
            }

            addPassenger(clock, src, dst);
        }
    }
    boolean generated = false;

    public void reset(Clock clock, int lastPassengerId) {
        generated = false;
        super.reset(clock, lastPassengerId);
    }

    public List<Passenger> generate(Clock clock, Building building) {
        if (generated == false) {
            generatePassengers(building);
            generated = true;
        }

        return super.generate(clock, building);
    }
}
