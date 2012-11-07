
import org.segonds.elevators.model.*;

public class Project_phase2_3a {

    public static void main(String args[]) {

        // Setup two elevators
        // Capacity = 12, max speed = 6m/s, acceleration = 3m/s^2
        Elevator elevators[] = {
            new Elevator(12, 6, 3.0), new Elevator(12, 6, 3.0)
        };

        // Setup a building (10 stories, floor height = 4m)
        Building building = new Building(10, 4.0);

        // Create a simulation stage
        // Duration = 3000 clock ticks, seed # = 10
        SampleStage stage = new SampleStage(3000, 10);

        // Create a controller
        BasicController controller = new BasicController();

        // Run a simulation with or without GUI
        // Last param: true => with GUI; false => without GUI
        Simulator.run(stage, building, elevators, controller, true);
    }
}
