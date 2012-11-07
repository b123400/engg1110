

import java.util.Arrays;
import org.segonds.elevators.model.*;

// This class is incomplete. You need to complete its implementation according
// to the Baseline algorithm described in "Simulator Simulator.pptx".
public class CustomController implements Controller {

    /* Declaration of instance variables */
    // The building object
    ControlledBuilding building;
    // An array of all elevator objects
    ControlledElevator E[];
    ElevatorController controllers[]=new ElevatorController[9];
    
    boolean upPressedFloor[]=new boolean[99];
    boolean downPressedFloor[]=new boolean[99];
    

    // Number of floors
    int nbFloors;
    // Floor number of the top floor
    int topFloor;
    
    // Set up shorter aliases
    final Direction UP = Direction.UP;
    final Direction DOWN = Direction.DOWN;
    final Direction UNCOMMITTED = Direction.UNCOMMITTED;

    public void setup(ControlledBuilding b) {
        // Save a reference to the building object for later use
        building = b;

        // Obtain a reference to the array of elevators
        E = building.getElevators();
        for(int i=0;i<E.length;i++){
            controllers[i]=new ElevatorController(E[i]);
        }

        // Save these constants for easier access later
        nbFloors = building.getNbFloors();
        topFloor = nbFloors - 1;
        
        for(int i=0;i<=topFloor;i++){
            upPressedFloor[i]=downPressedFloor[i]=false;
        }
    }

    // Nothing to reset
    public void reset() {}

    public String getName() {
        return "Custom Controller";
    }

    public void tick(Clock clock) {
        int newRequestedFloors[]=new int[99];
        boolean newRequestedDirectionIsUp[]=new boolean[99];
        for(int i=0;i<=topFloor;i++){
            if(building.isFloorRequested(i, UP)&&!upPressedFloor[i]){
                //new user pressed up at floor i
                newRequestedFloors[newRequestedFloors.length]=i;
                newRequestedDirectionIsUp[i]=true;
                upPressedFloor[i]=true;
            }else if(!building.isFloorRequested(i, UP)&&upPressedFloor[i]){
                //pressed state to uppressed state
                upPressedFloor[i]=false;
            }
            if(building.isFloorRequested(i, DOWN)&&!downPressedFloor[i]){
                //someone pressed down now
                int j=0;
                while(j<newRequestedFloors.length){
                    if(newRequestedFloors[j]==i){
                        break;
                    }
                    j++;
                }
                if(j!=newRequestedFloors.length){
                    newRequestedFloors[newRequestedFloors.length]=i;
                    newRequestedDirectionIsUp[i]=false;
                }
                downPressedFloor[i]=true;
            }else if(!building.isFloorRequested(i, DOWN)&&downPressedFloor[i]){
                //pressed to uppressed
                downPressedFloor[i]=false;
            }
        }
        for(int i=0;i<newRequestedFloors.length;i++){
            Direction direction=newRequestedDirectionIsUp[i]?UP:DOWN;
            ElevatorController fastestController=controllers[0];
            int fastestTime=999999999;
            for(int j=0;j<controllers.length;j++){
                int thisTime=controllers[i].timeNeededUntilArrivingFloor(newRequestedFloors[i],direction);
                if(thisTime<fastestTime){
                    fastestTime=thisTime;
                    fastestController=controllers[i];
                }
            }
            fastestController.addTargetFloor(i);
        }
    }
}