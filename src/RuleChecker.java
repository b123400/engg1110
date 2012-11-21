import org.segonds.elevators.model.*;

class RuleChecker {
	ControlledBuilding building;
	ControlledElevator E[];

	// Set up shorter aliases
	final Direction UP = Direction.UP;
	final Direction DOWN = Direction.DOWN;
	final Direction UNCOMMITTED = Direction.UNCOMMITTED;
    
    /**
     * To construct a RuleChecker object.
     * @param E The array of Elevators.
     * @param building  The building object.
     */
    public RuleChecker(ControlledElevator E[], ControlledBuilding building) {
        this.E = E;
        this.building = building;
    }

	//// helper function of constraint checking #1 ////

	private int elevatorModel(int i) {
		double maxSpeed = E[i].getMaximumSpeed();
		double acceleration = E[i].getAcceleration();
		int capacity = E[i].getCapacity();

		double models[][] = {
			{4, 1.5, 10},
			{3, 1.8, 10},
			{3, 1.2, 16},
			{3.25, 1.2, 14},
			{3, 2, 8},
			{4.5, 1.5, 8}
		};

		for(int j = 0; j < 6; j++) {
			if(models[j][0] == maxSpeed &&
			   models[j][1] == acceleration &&
			   models[j][2] == capacity)
				return j;
		}

		return -1;
	}

	//// helper function of constraint checking #2 ////

	private boolean isReverse(int i)
	{
		if(E[i].getDirection() == UP && E[i].getFloor() > E[i].getTarget())
			return true;
		if(E[i].getDirection() == DOWN && E[i].getFloor() < E[i].getTarget())
			return true;
		return false;
	}
    
    /**
     * This function checks Rules 4, 5, 6, 7, and 11.
     * 
     * @return true means the setup() function fits all constraints. Else, at least one rule is violated.
     */

	public boolean setupChecking() {
		String report = "Inside setup()\n";
		boolean result = true;

		//// rule 4 ////

		if( building.getNbElevators() != 3) {
			report += "\tViolate Rule 4: number of elevators = " + building.getNbElevators() + "\n";
			result = false;
		}
        
        //// rule 5 ////

		for(int i = 0; i < building.getNbElevators(); i++) {
			if( elevatorModel(i) == -1 ) {
				report += "\tViolate Rule 5: Elevator " + i + " using an invalid model\n";
				result = false;
			}
		}

		//// rule 6 ////

		int freightCount = 0;
		for(int j = 0; j < building.getNbElevators(); j++) {
			if(elevatorModel(j) == 2 || elevatorModel(j) == 3) {
				freightCount++;
			}
		}

		if(freightCount == 0) {
			report += "\tViolate Rule 6: number of freight elevators = " + freightCount + "\n";
			result = false;
		}
        
		//// rule 7 ////

		for(int i = 0; i < building.getNbElevators(); i++) {
			for(int f = 0; f < building.getNbFloors(); f++) {
				if(E[i].isServiced(f) == false) {
					report += "\tViolate Rule 7\n";
					report += "\tE[i].isServiced(" + f + ") == false\n";
					result = false;
				}
			}
		}

		//// rule 11 ////

		if(building.getFloorHeight() != 4) {
			report += "\tViolate Rule 11: floor height = " + building.getFloorHeight() + "\n";
			result = false;
		}

		//// End of checking ////
		if(result == false)
			System.out.println(report);
        
		return result;
	}
    
    /**
     * This checks Rules 2 and 3
     * @param i which elevator.
     * @param tickNum   the clock tick you want to print.
     * @param previousDir   the previous direction of the elevator.
     * @return 
     */

    public boolean tickChecking(int i, int tickNum, Direction previousDir)
    {
        String report = "Tick = " + tickNum
                + "; i = " + i + "\n";
        boolean result = true;
        
        //// rule 2 ////
        
        if(isReverse(i) && E[i].isEmpty() == false && E[i].getSpeed() != 0) {
            report += "\tViolate Rule 2\n";
            report += "\tis moving reverse == true and E[i].isEmpty == false\n";
            result = false;
        }
        
        //// rule 2, too ////
        
        int searchStart;
        if(E[i].getSpeed() == 0)
            searchStart = E[i].getFloor();
        else
            searchStart = E[i].nextStoppableFloor();
        
        if(E[i].getDirection() == UP) {
            for(int j = searchStart - 1; j >= 0; j--) {
                if(E[i].isFloorRequested(j)) {
                    report += "\tViolate Rule 2\n";
                    report +=
                            "\tDirection == UP && current/approaching floor = " + searchStart
                            + " && E[i].isFloorRequested(" + j + ") == true\n";
                    result = false;
                }
            }
        }
        
        if(E[i].getDirection() == DOWN) {
            for(int j = searchStart + 1; j < building.getNbFloors(); j++) {
                if(E[i].isFloorRequested(j)) {
                    report += "\tViolate Rule 2\n";
                    report +=
                            "\tDirection == DOWN && current/approaching floor = " + searchStart
                            + " && E[i].isFloorRequested(" + j + ") == true\n";
                    result = false;
                }
            }
        }
        
        //// rule 3 ////
        
        if(E[i].getDirection() != previousDir && E[i].isEmpty() == false) {
            report += "\tViolate Rule 2: ";
            report += "previous dir = " + previousDir + " && ";
            report += "current  dir = " + E[i].getDirection() + " && ";
            report += "Elevator is not empty\n";
            
            result = false;
        }
        
        //// End of checking ////
        if(result == false)
            System.out.println(report);
        
        return result;
    }
}
