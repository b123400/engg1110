
import java.lang.reflect.Field;
import org.segonds.elevators.model.*;

public class ProjectMain {

    public static void main(String args[]) {
        // Run a single simulation with GUI. See the code in normal() for more info.
        normal();
        
        // Uncomment the following state when you want to check the  performance of your algorithm
        // evaluatePerformance();       
    }
    
    // The code here illustrate how to use run a simulation with a GUI.
    public static void normal() {
        // Setup a building (10 stories, floor height = 4m)
        Building building = new Building(11, 4.0);

        // Create a controller (RefBaselineController is a reference implementation
        // of the Baseline controller)
        BaselineController controller = new BaselineController();

        // We are going to use three elevators 
        Elevator elevators[] = new Elevator[3];

        // Using model-0, model-1, and model-2 elevators 
        elevators[0] = createElevator(1);
        elevators[1] = createElevator(1);
        elevators[2] = createElevator(1);
           
        Class clazz = elevators[0].getClass();
        try{
            Field f = clazz.getDeclaredField("acceleration");
            f.setAccessible(true);
            f.set(elevators[0], new Double(99));
            
            f=clazz.getDeclaredField("maximumSpeed");
            f.setAccessible(true);
            f.set(elevators[0], new Double(99));
        }catch(Exception e){
            //
        }
        
//        elevators[0]

        // Create a stage that model scenario 1
        ScenarioStage stage = ProjectStage.createStage1(6000, 1);
        
        // Run a simulation (with GUI)
        Simulator.run(stage, building, elevators, controller, true);

    }
        
    // The code in this method shows how you can calculate the 
    // value of the objective function directly in the program.
    public static void evaluatePerformance() {
        
        // To store the average and the standard deviation of the total times
        // for each scenario.
        double average[] = new double [5];
        double stddev[] = new double [5];
        
        // Setup a building (10 stories, floor height = 4m)
        Building building = new Building(11, 4.0);

        // Create a controller (RefBaselineController is a reference implementation
        // of the Baseline controller)
        BaselineController controller = new BaselineController();

        // We are going to use three elevators 
        Elevator elevators[] = new Elevator[3];

        // Set the elevators you want to use here 
        elevators[0] = createElevator(0);
        elevators[1] = createElevator(0);
        elevators[2] = createElevator(0);
      
        int duration = 6000;    // Simulation length in clock tick
        int seed = 1;           // Random # seed 
        
        // Run the simulation for 5 scenarios (using the same simulation 
        // length and same rand seed #)
        for (int i = 0; i < 5; i++) {
            ScenarioStage stage;
        
            if (i == 0)
                stage = ProjectStage.createStage1(duration, 1);
            else
            if (i == 1)
                stage = ProjectStage.createStage2(duration, 1);
            else
            if (i == 2)
                stage = ProjectStage.createStage3(duration, 1);
            else
            if (i == 3)
                stage = ProjectStage.createStage4(duration, 1);                        
            else
                stage = ProjectStage.createStage5(duration, 1);
            
            // Setting this property to "true" before a simulation begins will 
            // supress the output generated by Simulator.run().
            Simulator.silent = true;
            
            // When the simulation ends, the object "stat" will hold some 
            // statistical data about the simulation
            // Note: Simulator.run will only returns a stat object if the simulation
            // is running without a GUI.s
            Statistics stat = Simulator.run(stage, building, elevators, controller, true);

            // Record the two pieces of info we need
            average[i] = stat.getAverageTotalTime();
            stddev[i] = stat.getStddevTotalTime();
        }
        
        // Values of the weight function, as specified in the spec
        double weight[] = { 1.1, 1.1, 1.3, 1, 1.2 };
        
        // These values are measured in clock ticks. If you want the values
        // in second, you can divide the results by 10.
        double score = 0;
        for (int i = 0; i < 5; i++) {
            System.out.printf("Scenario %d: Avg=%.4f, Stddev=%.4f\n", i+1, average[i], stddev[i]);
            score += weight[i] * (average[i] + 0.5 * stddev[i]);
        }
        
        System.out.printf("Value of the objective function = %.4f\n", score);
    }
    
    
    
        
    static Elevator createElevator(int model) {
        if (model == 0) {
            return new Elevator(10, 4, 1.5);
        }

        if (model == 1) {
            return new Elevator(10, 3, 1.8);
        }

        if (model == 2) {
            return new Elevator(16, 3, 1.2);
        }

        if (model == 3) {
            return new Elevator(14, 3.25, 1.2);
        }

        if (model == 4) {
            return new Elevator(8, 3, 2);
        }

        // otherwise assume model == 5
        return new Elevator(8, 4.5, 1.5);
    }
}
