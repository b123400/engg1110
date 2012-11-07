import org.segonds.elevators.model.*;

/**
 *
 * @author cjyuan
 * 
 * You should not modify this class.
 */
public class ProjectStage {

  /*
   Usage of RandomStage2:
   Constructor:
   RandomStage2(long length, int seed, int nbFloors,
   double AR[], double TP[][]);

   AR is an array of size "nbFloors" that stores the arrival rate on each floor.
   AR[i] > 0 ==>  Average arrival interval between two passengers arriving
   at floor i (measured in clock ticks)
   AR[i] == 0 => No passenger is going to arrive at floor i.



   TP is a 2D array of size nbFloors * nbFloors. Each row, TP[x], stores the
   percentages of passengers (among all passengers arriving at floor x)
   going to floor y.

   TP[x][y] > 0 =>
   Percentage of passengers (among all passengers arriving at floor x)
   going to floor y.

   TP[x][y] or TP[y][x] == -1 ==>
   No elevator service or no passenger is goign to travel between x and y

   TP[x][y] == 0 ==>
   Want to share the remaining percentage equally among all TP[x][z] == 0.

   Note: One of the following two conditions must be satisfied
   1) If one of the elements in TP[x] (i.e., row x) is zero, then sum of all
   non-negative elements in TP[x] must be less than or equal to 100.
   2) If none of the elements in TP[x] is zero, then sum of all the
   non-negative elements must be equal to 100.

   TP[x][x] will be automatically set to -1 for all x's.
   */
    
    
  static final int nbFloors = 11;

   
  /*
   * Scenario #1 (Around 9pm when most passengers are travelling from floor #0
   * and #5 to the other floors)
   * Characteristics:
   *    A lot of people enter through floors #0 and #5.
   *    The ratio of people going to floor #5, #0 and any of the other floors
   *    is roughly 2:2:1.
   *    Passengers have the same likelihood to go to any floor except floor #0,
   *    #5, and adjacent floors.
   *    People are less likely to take an elevator to adjacent floors.
   *
   * Arrival Rate (AR):
   *  Floor #0: 20 sec
   *  Floor #5: 15 sec
   *  Other floors: 60 sec
   *
   * Travelling pattern (TP):
   * If not specified, assume the entry is 0 (i.e., share the remaining percentage).
   *
   *      |  0|  1|  2|  3|  4|  5|  6|  7|  8|  9| 10|
   *  --------------------------------------------------------------------
   *    0 |---|  5|   |   |   | 30|   |   |   |   |   |
   *    1 | 10|---|  5|   |   | 20|   |   |   |   |   |
   *    2 | 20|  5|---|  5|   | 20|   |   |   |   |   |
   *    3 | 20|   |  5|---|  5| 20|   |   |   |   |   |
   *    4 | 20|   |   |  5|---| 10|   |   |   |   |   |
   *    5 | 20|   |   |   |  5|---|  5|   |   |   |   |
   *    6 | 20|   |   |   |   | 10|---|  5|   |   |   |
   *    7 | 20|   |   |   |   | 20|  5|---|  5|   |   |
   *    8 | 20|   |   |   |   | 20|   |  5|---|  5|   |
   *    9 | 20|   |   |   |   | 20|   |   |  5|---|  5|
   *   10 | 20|   |   |   |   | 20|   |   |   |  5|---|
   *
   *
   *   From any floor (that is not #0 and #5) to:
   *     #0 or #5: 20%
   *     Adjacent floors (one floor above and one floor below): 5% respectively



   */
  public static ScenarioStage createStage1(long length, int seed) {
    double AR[] = {200, 600, 600, 600, 600, 150, 600, 600, 600, 600, 600};
    double TP[][] = {
      {-1, 5, 0, 0, 0, 30, 0, 0, 0, 0, 0},
      {10, -1, 5, 0, 0, 20, 0, 0, 0, 0, 0},
      {20, 5, -1, 5, 0, 25, 0, 0, 0, 0, 0},
      {20, 0, 5, -1, 5, 25, 0, 0, 0, 0, 0},
      {20, 0, 0, 5, -1, 10, 0, 0, 0, 0, 0},
      {20, 0, 0, 0, 5, -1, 5, 0, 0, 0, 0},
      {20, 0, 0, 0, 0, 10, -1, 5, 0, 0, 0},
      {20, 0, 0, 0, 0, 20, 5, -1, 5, 0, 0},
      {20, 0, 0, 0, 0, 20, 0, 5, -1, 5, 0},
      {20, 0, 0, 0, 0, 20, 0, 0, 5, -1, 5},
      {20, 0, 0, 0, 0, 20, 0, 0, 0, 5, -1}
    };

    return new RandomStage2(length, seed, nbFloors, AR, TP);
  }
  /*
   * Scenario #2 (Lunch time at around 1pm)
   * Characteristics:
   *    A lot of people enter and exit through floors #0 and #5.
   *    The ratio of people going to floor #5, #0 and any of the other floors
   *    is roughly 4:2:1.
   *    Passengers have the same likelihood to go to any floor except floor #0,
   *    #5, and adjacent floors.
   *    People are less likely to take an elevator to adjacent floors.
   *
   * Arrival Rate (AR):
   *  Floor #0: 15 sec
   *  Floor #5: 10 sec
   *  Other floors: 25 sec
   *
   * Travelling pattern (TP):
   * If not specified, assume the entry is 0 (i.e., share the remaining percentage).
   *
   *      |  0|  1|  2|  3|  4|  5|  6|  7|  8|  9| 10|
   *  --------------------------------------------------------------------
   *    0 |---|  5|   |   |   | 20|   |   |   |   |   |
   *    1 | 13|---|  3|   |   | 50|   |   |   |   |   |
   *    2 | 25|  2|---|  2|   | 45|   |   |   |   |   |
   *    3 | 25|   |  2|---|  2| 45|   |   |   |   |   |
   *    4 | 40|   |   |  2|---| 23|   |   |   |   |   |
   *    5 | 30|   |   |   |  5|---|  5|   |   |   |   |
   *    6 | 40|   |   |   |   | 23|---|  2|   |   |   |
   *    7 | 25|   |   |   |   | 45|  2|---|  2|   |   |
   *    8 | 25|   |   |   |   | 45|   |  2|---|  2|   |
   *    9 | 25|   |   |   |   | 45|   |   |  2|---|  2|
   *   10 | 25|   |   |   |   | 45|   |   |   |  2|---|
   */
  static double TP2[][] = {
    {-1, 5, 0, 0, 0, 20, 0, 0, 0, 0, 0},
    {13, -1, 3, 0, 0, 50, 0, 0, 0, 0, 0},
    {25, 2, -1, 2, 0, 45, 0, 0, 0, 0, 0},
    {25, 0, 2, -1, 2, 45, 0, 0, 0, 0, 0},
    {40, 0, 0, 2, -1, 23, 0, 0, 0, 0, 0},
    {30, 0, 0, 0, 5, -1, 5, 0, 0, 0, 0},
    {40, 0, 0, 0, 0, 23, -1, 2, 0, 0, 0},
    {25, 0, 0, 0, 0, 45, 2, -1, 2, 0, 0},
    {25, 0, 0, 0, 0, 45, 0, 2, -1, 2, 0},
    {25, 0, 0, 0, 0, 45, 0, 0, 2, -1, 2},
    {25, 0, 0, 0, 0, 45, 0, 0, 0, 2, -1}
  };

  public static ScenarioStage createStage2(long length, int seed) {

    double AR[] = {150, 250, 250, 250, 250, 100, 250, 250, 250, 250, 250};
    return new RandomStage2(length, seed, nbFloors, AR, TP2);
  }

  /*
   * Scenario #3 (Around 5:30pm when most passengers are leaving the building )
   * Characteristics:
   *    Most passengers exit through floor #0 and #5.
   *    Passengers have the same likelihood to go to any floor except floor #0,
   *    #5, and adjacent floors.
   *    People are less likely to take an elevator to adjacent floors.
   *
   * Arrival Rate (AR):
   *  Floor #0: 120 sec
   *  Floor #5: 15 sec
   *  Floor #1, #4, #6: 30 sec
   *  Other floors: 25 sec
   *
   * Travelling pattern (TP):
   * If not specified, assume the entry is 0 (i.e., share the remaining percentage).
   *  
   *      |  0|  1|  2|  3|  4|  5|  6|  7|  8|  9| 10|
   *  --------------------------------------------------------------------
   *    0 |---|  4|   |   |   | 50|   |   |   |   |   |
   *    1 | 30|---|  2|   |   | 40|   |   |   |   |   |
   *    2 | 50|  1|---|  1|   | 35|   |   |   |   |   |
   *    3 | 50|   |  1|---|  1| 35|   |   |   |   |   |
   *    4 | 60|   |   |  2|---| 15|   |   |   |   |   |
   *    5 | 70|   |   |   |  2|---|  2|   |   |   |   |
   *    6 | 60|   |   |   |   | 15|---|  2|   |   |   |
   *    7 | 50|   |   |   |   | 35|  1|---|  1|   |   |
   *    8 | 50|   |   |   |   | 35|   |  1|---|  1|   |
   *    9 | 50|   |   |   |   | 35|   |   |  1|---|  1|
   *   10 | 50|   |   |   |   | 35|   |   |   |  1|---|
   */
  public static ScenarioStage createStage3(long length, int seed) {

    double AR[] = {1200, 300, 250, 250, 300, 150, 300, 250, 250, 250, 250};
    double TP[][] = {
      {-1, 4, 0, 0, 0, 50, 0, 0, 0, 0, 0},
      {30, -1, 2, 0, 0, 40, 0, 0, 0, 0, 0},
      {50, 1, -1, 1, 0, 35, 0, 0, 0, 0, 0},
      {50, 0, 1, -1, 1, 35, 0, 0, 0, 0, 0},
      {60, 0, 0, 2, -1, 15, 0, 0, 0, 0, 0},
      {70, 0, 0, 0, 2, -1, 2, 0, 0, 0, 0},
      {60, 0, 0, 0, 0, 15, -1, 2, 0, 0, 0},
      {50, 0, 0, 0, 0, 35, 1, -1, 1, 0, 0},
      {50, 0, 0, 0, 0, 35, 0, 1, -1, 1, 0},
      {50, 0, 0, 0, 0, 35, 0, 0, 1, -1, 1},
      {50, 0, 0, 0, 0, 35, 0, 0, 0, 1, -1}
    };

    return new RandomStage2(length, seed, nbFloors, AR, TP);
  }
  /*
   * Scenario #4: Other periods (Light traffic)
   *    The traffic pattern is similar to that found in Scenario 2 except that
   *    the arrival interval between two passengers on the same floor is much
   *    larger.
   *
   * Arrival Rate (AR):
   *  Floor #0: 150 sec
   *  Floor #5: 120 sec
   *  Other floors: 300 sec
   *
   *
   * Travelling pattern (TP):
   * If not specified, assume the entry is 0 (i.e., share the remaining percentage).
   *
   */

  public static ScenarioStage createStage4(long length, int seed) {

    double AR[] = {1500, 3000, 3000, 3000, 3000, 1200, 3000, 3000, 3000, 3000, 3000};
    return new RandomStage2(length, seed, nbFloors, AR, TP2);
  }


  /*
   * Scenario #5 (In between classes)
   * Characteristics:
   *    The traffic pattern is similar to that found in Scenario 2 except that
   *    passengers appear in group of 1 to 4 (average group size is 2.5), and
   *    passengers in the same group will go to the same floor.
   *
   *    The arrival rates between two groups on the same floor are 2.5 times 
   *    more than those found in Scenario 2.
   *
   * Travelling pattern (TP):
   * The travelling pattern is the same as that found in Scenario 2.
   */
  public static ScenarioStage createStage5(long length, int seed) {

    double AR[] = {150, 250, 250, 250, 250, 100, 250, 250, 250, 250, 250};
    // Since the average group size is 2.5. The arrival interval is also
    // multiplied by 2.5 to keep the same number of passengers
    for (int i = 0; i < nbFloors; i++) {
      AR[i] = AR[i] * 2.5;
    }

    double groupProbability[] = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    int maxAdditionalPassengers[] = {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};

    return new RandomStage2(length, seed, nbFloors, AR, TP2,
            groupProbability, maxAdditionalPassengers);
  }
}
// Template
/*

 double TP[][] = {
 { -1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
 {  0, -1,  0,  0,  0,  0,  0,  0,  0,  0,  0},
 {  0,  0, -1,  0,  0,  0,  0,  0,  0,  0,  0},
 {  0,  0,  0, -1,  0,  0,  0,  0,  0,  0,  0},
 {  0,  0,  0,  0, -1,  0,  0,  0,  0,  0,  0},
 {  0,  0,  0,  0,  0, -1,  0,  0,  0,  0,  0},
 {  0,  0,  0,  0,  0,  0, -1,  0,  0,  0,  0},
 {  0,  0,  0,  0,  0,  0,  0, -1,  0,  0,  0},
 {  0,  0,  0,  0,  0,  0,  0,  0, -1,  0,  0},
 {  0,  0,  0,  0,  0,  0,  0,  0,  0, -1,  0},
 {  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, -1}
 };

 */
