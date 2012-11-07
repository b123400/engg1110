

import org.segonds.elevators.model.*;

// Reference implementation of the Baseline Controller (as described in the
// lecture note)
public class RefBaselineController implements Controller {
    /* Declaration of instance variables */
    // The building object
    ControlledBuilding building;
    // An array of all elevator objects
    ControlledElevator E[];

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

       // Save these constants for easier access later
        nbFloors = building.getNbFloors();
        topFloor = nbFloors - 1;
    }
    
    // Nothing to reset
    public void reset() {}

    public String getName() {
        return "Baseline Controller";
    }


    public void tick(Clock clock) {
        // For each elevator
        for (int i = 0; i < E.length; i++) {
            // Elevator's velocity. speed == 0 => Stopped;
            // speed > 0 => Moving up; speed < 0 => Moving down
            double speed = E[i].getSpeed();

            // Committed Direction
            Direction CD = E[i].getDirection();

            if (CD == UP && speed > 0)
                handleCase1(i);
            else
            if (CD == UP && speed == 0)
                handleCase2(i);
            else
            if (CD == DOWN && speed < 0)
                handleCase3(i);
            else
            if (CD == DOWN && speed == 0)
                handleCase4(i);
            else
            if (CD == UNCOMMITTED)
                handleCase5(i);
        }
    }

    public void handleCase1(int i) {
        int nextFloor = E[i].nextStoppableFloor();

        // If floor x is a candidate, candidates[x] will be set to true
        // Note: All elements are initialized to false by default.
        boolean candidates[] = new boolean [nbFloors];

        // Locate candidates
        for (int f = nextFloor; f <= topFloor; f++)
            if (building.isFloorRequested(f, UP) || E[i].isFloorRequested(f))
                candidates[f] = true;

        // At this point, candidates[] has recorded all the
        // candidate floors. Next step is to find the target among
        // the candidate floors.

        // Initially use -1 to indicate "target not found"
        int target = -1;

        // Find the lowest candidate floor as the target
        for (int f = 0; f <= topFloor; f++)
            if (candidates[f] == true) {
              target = f;
              break;
            }

        // If we can find a target
        if (target != -1)
            E[i].setTarget(target);
        // Otherwise, treat the case as an idle case
        else
            handleCase5(i);
    }
      
    public void handleCase2(int i) {

        int currentFloor = E[i].getFloor();

        if (currentFloor == topFloor) {
            handleCase5(i);
            return;
        }
        
        // Nothing to do if the elevator doors are NOT in closed or 
        // closing states
        if (! E[i].isClosing() )
            return;       
   
        int nextFloor = currentFloor + 1;

        boolean candidates[] = new boolean [nbFloors];

        for (int f = nextFloor; f <= topFloor; f++)
            if (building.isFloorRequested(f, UP) || E[i].isFloorRequested(f))
                candidates[f] = true;

        int target = -1;

        // Find the lowest candidate floor as the target
        for (int f = 0; f <= topFloor; f++)
            if (candidates[f] == true) {
                target = f;
                break;
            }
        
        // If we can find a target
        if (target != -1)
            E[i].setTarget(target);
        // Otherwise, treat the case as an idle case
        else
            handleCase5(i);
    }

    public void handleCase3(int i) {
        int nextFloor = E[i].nextStoppableFloor();

        boolean candidates[] = new boolean [nbFloors];

        // Locate candidates
        for (int f = nextFloor; f >= 0; f--)
            if (building.isFloorRequested(f, DOWN) || E[i].isFloorRequested(f))
                candidates[f] = true;

        // Initially use -1 to indicate "target not found"
        int target = -1;

        // Find the highest candidate floor as the target
        for (int f = topFloor; f >= 0; f--)
            if (candidates[f] == true) {
                target = f;
                break;
              }

        // If we can find a target
        if (target != -1)
            E[i].setTarget(target);
        // Otherwise, treat the case as an idle case
        else
            handleCase5(i);
    }

    public void handleCase4(int i) {
        int currentFloor = E[i].getFloor();
        if (currentFloor == 0) {
            handleCase5(i);
            return;
        }

        // Nothing to do if the elevator doors are NOT in closed or
        // closing states
        if (! E[i].isClosing() )
             return;

        int nextFloor = currentFloor - 1;

        boolean candidates[] = new boolean [nbFloors];

        for (int f = nextFloor; f >= 0; f--)
            if (building.isFloorRequested(f, DOWN) || E[i].isFloorRequested(f))
                candidates[f] = true;

        int target = -1;

        // Find the highest candidate floor as the target
        for (int f = topFloor; f >= 0; f--)
            if (candidates[f] == true) {
                target = f;
                break;
            }

        // If we can find a target
        if (target != -1)
            E[i].setTarget(target);
        // Otherwise, treat the case as an idle case
        else
            handleCase5(i);
    }

    // Handle uncomitted case
    void handleCase5(int i) {

        boolean candidates[] = new boolean [nbFloors];

        for (int f = 0; f <= topFloor; f++)
            if (building.isFloorRequested(f, UP) || E[i].isFloorRequested(f))
                candidates[f] = true;

        int target = -1;

        // Find the lowest candidate floor as the target
        for (int f = 0; f <= topFloor; f++)
            if (candidates[f] == true) {
                target = f;
                break;
            }

        if (target != -1) {
            E[i].setTarget(target);
            E[i].setDirection(UP);
            return;
        }

         
        // Note: candidates[] and target remain unchanged at this point
        // So no need to reinitialize.

        for (int f = 0; f <= topFloor; f++)
            if (building.isFloorRequested(f, DOWN) || E[i].isFloorRequested(f))
                candidates[f] = true;

        // Find the highest candidate floor as the target
        for (int f = topFloor; f >= 0; f--)
            if (candidates[f] == true) {
                target = f;
                break;
            }

        if (target != -1) {
            E[i].setTarget(target);
            E[i].setDirection(DOWN);
            return;
        }

        // Otherwise stays idle
        E[i].setDirection(UNCOMMITTED);
    }

}
