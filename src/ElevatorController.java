/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author b123400
 */
import java.util.Arrays;
import org.segonds.elevators.model.*;
public class ElevatorController {
    ControlledElevator elevator;
    int targetFloors[]=new int[99];
    public ElevatorController(ControlledElevator _elevator){
        elevator=_elevator;
    }
    public void addTargetFloor(int floor){
        int i=0;
        for(i=0;i<targetFloors.length;i++){
            if(targetFloors[i]==floor) {
                break;
            }
        }
        if(i!=targetFloors.length){
            targetFloors[targetFloors.length-1]=floor;
        }
        Arrays.sort(targetFloors);
        if(this.elevator.getDirection()==Direction.UP){
            this.elevator.setTarget(targetFloors[0]);
        }else{
            this.elevator.setTarget(targetFloors[targetFloors.length-1]);
        }
    }
    public ControlledElevator getElevator(){
        return elevator;
    }
    public int timeNeededUntilArrivingFloor(int floor, Direction direction){
        if(direction!=this.elevator.getDirection())return targetFloors.length*2;
        return targetFloors.length;
    }
}
