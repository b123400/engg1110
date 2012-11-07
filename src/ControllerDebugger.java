
import java.io.*;
import org.segonds.elevators.model.*;

public class ControllerDebugger implements Controller {

    Controller C;
    ControlledBuilding building;
    ControlledElevator E[];
    int startTick, endTick;
    String outFilename;
    PrintStream out;

    // Show output to the console
    public ControllerDebugger(Controller c) {
        this(c, -1, -1, null);
    }

    // Show output to the specified file or to the console (if _outFilename is null)
    public ControllerDebugger(Controller c, String _outFilename) {
        this(c, -1, -1, _outFilename);
    }

    // Only generate output between "_startTick" and "_endTick".
    // Output can be send to the specifiled file or to the console (if _outFilename is null)
    public ControllerDebugger(
            Controller c, int _startTick, int _endTick,
            String _outFilename) {
        C = c;
        startTick = _startTick;
        endTick = _endTick;
        outFilename = _outFilename;
    }

    @Override
    public String getName() {
        return C.getName();
    }

    @Override
    public void reset() {
        try {
            if (outFilename == null) {
                out = System.out;   // Send the output to the console
            } else {
                // Send the output to the specified file
                out = new PrintStream(new FileOutputStream(new File(outFilename)));
            }
        } catch (IOException exp) {
            out = null;
            System.err.println("Error (" + outFilename + "): " + exp.getMessage());
        }

        C.reset();

        // Output header fields


    }

    @Override
    public void setup(ControlledBuilding b) {
        building = b;
        E = b.getElevators();
        C.setup(building);
    }

    @Override
    public void tick(Clock clock) {

        C.tick(clock);        
        
        boolean showOutput =
                (startTick == -1 && endTick == -1)
                || (clock.getTicks() >= startTick && clock.getTicks() <= endTick);
        
        if (showOutput) {
            printDebuggingInfo(out, clock, building);
        }
    }
    
    public static void printDebuggingInfo(Clock clock, ControlledBuilding b) {
        printDebuggingInfo(System.out, clock, b);
    }

    public static void printDebuggingInfo(PrintStream out, Clock clock, ControlledBuilding b) {
        String upButtons = "";
        String downButtons = "";
        for (int i = 0; i < b.getNbFloors(); i++) {
            if (b.isFloorRequested(i, Direction.UP)) {
                upButtons += i + " ";
            }
            if (b.isFloorRequested(i, Direction.DOWN)) {
                downButtons += i + " ";
            }
        }

        out.println(clock.getTicks()
                + ", UP [" + upButtons + "], DOWN [" + downButtons + "]");

        ControlledElevator E[] = b.getElevators();

        // Output each elevator's states
        for (int i = 0; i < E.length; i++) {

            String door = "UNKNOWN";
            if (E[i].isClosed() && !E[i].isAboutToOpen()) {
                door = "CLOSED";
            } else if (E[i].isOpen()) {
                door = "OPEN";
            } else if (E[i].isClosing()) {
                door = "CLOSING";
            } else if (E[i].isOpening()) {
                door = "OPENING";
            } else if (E[i].isAboutToOpen()) {
                door = "CLOSED (About to open)";
            }

            String elevatorButtons = "";
            for (int f = 0; f < b.getNbFloors(); f++) {
                if (E[i].isFloorRequested(f)) {
                    elevatorButtons += f + " ";
                }
            }

            // Elevator #, speed, floor, door status, target, committed direction
            out.printf("%d, %.2f, %d, %s, %d, %s, [%s]\n",
                    i, E[i].getSpeed(), E[i].getFloor(), door,
                    E[i].getTarget(), E[i].getDirection().toString(), elevatorButtons);

        }
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();
        if (outFilename != null && out != null) {
            out.close();
            out = null;
        }
    }
}
