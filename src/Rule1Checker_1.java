import org.segonds.elevators.model.*;

public class Rule1Checker_1
{
    public static void main(String args[])
    {
        Building building = new Building(11, 4.0);
        
        RefBaselineController controller = new RefBaselineController();
        
        Elevator elevators[] = new Elevator[3];
        
        elevators[0] = createElevator(0);
        elevators[1] = createElevator(1);
        elevators[2] = createElevator(2);
        
        //// Rule 1 checking ////
        
        ScheduledStage stage = new ScheduledStage(36000);
        int time = 10, step = 500;
        for(int i = 0; i < building.getNbFloors()-1; i++) {
            stage.addPassenger(time, i, i+1);
            time += step;
        }

        for(int i = building.getNbFloors()-1; i > 0; i--) {
            stage.addPassenger(time, i, i-1);
            time += step;
        }
       
        Simulator.run(stage, building, elevators, controller, true);
    }
  
    static Elevator createElevator(int model)
    {
        if(model == 0)
        {
            return new Elevator(10, 4, 1.5);
        }
        
        if(model == 1)
        {
            return new Elevator(10, 3, 1.8);
        }
        
        if(model == 2)
        {
            return new Elevator(16, 3, 1.2);
        }
        
        if(model == 3)
        {
            return new Elevator(14, 3.25, 1.2);
        }
        
        if(model == 4)
        {
            return new Elevator(8, 3, 2);
        }
        
        // otherwise assume model == 5
        return new Elevator(8, 4.5, 1.5);
    }
}
