
import org.segonds.elevators.model.*;

/**
 *
 * @author cjyuan
 *
 * This controller makes an elevator stop at every floor on its way down/up.
 */
public class BasicController implements Controller {

    protected ControlledBuilding building;
    protected ControlledElevator elevators[];
    private int buildingFloors, topFloor;
    public final Direction UP = Direction.UP;
    public final Direction DOWN = Direction.DOWN;
    public final Direction UNCOMMITTED = Direction.UNCOMMITTED;

    @Override
    public void setup(ControlledBuilding building) {
        this.building = building;
        elevators = building.getElevators();
        buildingFloors = building.getNbFloors();
        topFloor = buildingFloors - 1;
    }

    @Override
    public void reset() {
        // nothing to do
    }

    @Override
    public String getName() {
        return "Basic Sweep Controller";
    }

    @Override
    public void tick(Clock clock) {

        // For each elevator
        for (int i = 0; i < elevators.length; i++) {
            ControlledElevator elevator = elevators[i];

            double speed = elevator.getSpeed();
            int currentFloor = elevator.getFloor();

            // If the elevator is currently stopped at a floor and if its
            // doors are opening.
            if (speed == 0.0 && elevator.isOpening()) {

                // If the elevator was moving up
                if (elevator.getDirection() == UP) {

                    // If the elevator is at the top floor, reverse its direction
                    if (currentFloor == topFloor) {
                        elevator.setDirection(DOWN);
                        elevator.setTarget(topFloor - 1);
                    } else // Otherwise, continue to move up to the next floor
                    {
                        elevator.setTarget(currentFloor + 1);
                    }
                } else if (elevator.getDirection() == DOWN) {

                    // If the elevator is at the ground floor, reverse its direction
                    if (currentFloor == 0) {
                        elevator.setDirection(UP);
                        elevator.setTarget(1);
                    } // Otherwise, continue to move down to the next floor.
                    else {
                        elevator.setTarget(currentFloor - 1);
                    }
                }
            }

            if (elevator.getDirection() == UNCOMMITTED) {
                // If the elevator is idle. (initially)
                elevator.setDirection(UP);
                elevator.setTarget(0);
            }
        } // end for each elevator
    }
}
