import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * A simple model of a bear.
 * Bears sleep, breed, age, move, eat deer, and die.
 * 
 * @author Anirudh George and Ahmet Bayazitoglu 
 * @version 2022.03.02 
 */
public class Bear extends Predator
{
    // The age at which a bear can start to breed.
    private static final int BREEDING_AGE = 10;
    // The age to which a bear can live.
    private static final int MAX_AGE = 320;
    // The likelihood of a bear breeding.
    private static final double BREEDING_PROBABILITY = 0.20;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 7;
    // The food value of a single deer. In effect, this is the
    // number of steps a bear can go before it has to eat again.
    private static final int DEER_FOOD_VALUE = 9;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // The maximum amount of food a bear can have.
    private static final int MAX_FOOD_LEVEL = 180;
    // The bear's age.
    private int age;
    // The bear's food level, which is increased by eating deer.
    private int foodLevel;
     //The weather
    private Weather weather = new Weather();
    /**
     * Create a bear. A bear can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the bear will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param gender The gender of the tiger
     */
    public Bear(boolean randomAge, Field field, Location location, Gender gender)
    {
        super(field, location, gender);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(DEER_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = DEER_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the bear does most of the time: it hunts for
     * deer. In the process, it might meet and breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newBears A list to return newly born bears.
     */
    protected void act(List<Organism> newBears)
    {
        incrementAge();
        incrementHunger();
        if(weather.dayCounter == 1 ){
            sleep();
        }
        else if(isAlive()) {
            meet(newBears);
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Increase the age. This could result in the bear's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this bear more hungry. This could result in the bear's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
    
    /**
     * Look for deer adjacent to the current location.
     * Only the first live deer is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentNLocations(getLocation(), 2);
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext() ) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Deer) {
                Deer deer = (Deer) animal;
                if(deer.isAlive()) { 
                    deer.setDead();
                    foodLevel = DEER_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * Check whether or not this bear is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newBears A list to return newly born bears.
     */
    protected void giveBirth(List<Organism> newBears)
    {
        // New bears are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Bear cub = new Bear(false, field, loc, randomGender());
            newBears.add(cub);
        }
    } 
    
    /**
     * Check whether or not this bear is to give birth at this step.
     * New bears will be made into free adjacent locations.
     * @param newBears A list to return newly born bears.
     * @param steps vary the radius between two bears in which they can give birth.
     */
    protected void giveBirth(List<Organism> newBears, int steps)
    {
        // New bears are born into adjacent locations.
        // Get a list of further apart free locations.
        Field field = getField();
        List<Location> free = field.getFreeNAdjacentLocations(getLocation(), steps);
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Bear cub = new Bear(false, field, loc, randomGender());
            newBears.add(cub);
        }
    } 
    
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }
    
    /**
     * Checks whether a pair of bear objects can both breed if they both have reached the breeding age.
     * @return A value determining whether the bears can breed or not.
     */
    protected boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}