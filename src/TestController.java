
import org.segonds.elevators.model.*;

/**
 *
 * @author cjyuan
 */
public class TestController {

    public static void main(String args[]) {
        BaselineController c = new BaselineController();
        test1(c);
    }

    // For different test cases, feel free to make multiple copies of this method
    // and customize the states accordingly.
    public static void test1(Controller c) {
        /****************** Set up Building ********************/
        // Create a building (# of floors = 11, floor height = 4m)
        UnitTestBuilding b = new UnitTestBuilding(11, 4);

        // By default all floor buttons are off

        // Press the UP button on floor 4 and 10
        b.setUpButton(10, true);
        b.setUpButton(4, true);

        // Press the DOWN button on floors 1 and 10
        b.setDownButton(1, true);
        b.setDownButton(10, true);

        /****************** Set up an Elevator ********************/
        // Create one elevator for testing
        UnitTestElevator e[] = new UnitTestElevator[1];

        // Create an elevator object
        // Parameters are: The building object, capacity, maximum speed, acceleration rate
        e[0] = new UnitTestElevator(b, 12, 4, 1.6);

        // Press the elevator buttons for floor 2 and 5 (i.e., to simulate some
        // passengers going to floor 2 and some going to floor 5)
        e[0].setFloorRequested(2, true);
        e[0].setFloorRequested(5, true);

        // Set the current committed direction to UP
        e[0].setDirection(Direction.UP);

        // Place the elevator at floor 4
        e[0].setFloor(0);

        // Note: To set the position of an elevator, you can use either setFloor() or
        // setPosition(). setFloor(x) is the same as setPosition(x * floorHeight);

        e[0].setSpeed(0); // Make the elevator stopped

        // Possible values of "door status" are as follow.
        // 0 => CLOSED, 1 => OPEN, 2 => CLOSING, 3 => OPENING, 4 => ABOUT_TO_OPEN
        e[0].setDoorStatus(2);   // Set the door status as "closing"

        // Test the controller (the result will be output to the console)
        UnitTester.test(b, e, c);


        // For this particular test case, the baseline algo should have
        // set the target to 2 and committed direction to UP.
    }
}
