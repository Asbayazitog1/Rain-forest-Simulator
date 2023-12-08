import java.util.List;
import java.util.Random;
import java.util.Iterator;

/**
 * A simple model of a deer.
 * Deer sleep, breed age, move, eat plants and die.
 * 
 * @author Anirudh George and Ahmet Bayazitoglu 
 * @version 2022.03.02 
 */
public class Deer extends Prey
{
    // The age at which a deer can start to breed.
    private static final int BREEDING_AGE = 9;
    // The age to which a deer can live.
    private static final int MAX_AGE = 200;
    // The likelihood of a deer breeding.
    private static final double BREEDING_PROBABILITY = 0.3;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 8;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    // The food value of a single plant. In effect, this is the
    // number of steps a deer can go before it has to eat again.
    private static final int PLANT_FOOD_VALUE = 16;
    //The maximum amount of food a deer can have
    private static final int MAX_FOOD_LEVEL = 160;
    
    // The deer's age.
    private int age;
    //The deer's food level, which is increased by eating plants.
    private int foodLevel;
     //The weather
    private Weather weather = new Weather();
    /**
     * Create a new deer. A deer may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the deer will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param gender The gender of the deer
     */
    public Deer(boolean randomAge, Field field, Location location, Gender gender)
    {
        super(field, location, gender);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(PLANT_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = PLANT_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the deer does most of the time - it runs 
     * around. Sometimes it will meet and breed or die of old age.
     * @param newDeer A list to return newly born deer.
     */
    protected void act(List<Organism> newDeer)
    {
        incrementAge();
        incrementHunger();
        if(weather.dayCounter==3 ){
            sleep();
        }
        else if(isAlive()) {
            meet(newDeer);
            // Try to move into a free location.
            Location newLocation = findFood();
            if (newLocation == null) {
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            
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
     * Increase the age.
     * This could result in the deer's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this deer is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newDeer A list to return newly born deer.
     */
    protected void giveBirth(List<Organism> newDeer)
    {
        // New deer are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Deer fawn = new Deer(false, field, loc, randomGender());
            newDeer.add(fawn);
        }
    }
    
    /**
     * Check whether or not this deer is to give birth at this step.
     * New births will be made into free locations at a larger distance .
     * @param newDeer A list to return newly born deer.
     * @param steps vary the radius between two deer in which they can give birth.
     */
    protected void giveBirth(List<Organism> newDeer, int steps)
    {
        // New deer are born into adjacent locations.
        // Get a list of further apart free locations.
        Field field = getField();
        List<Location> free = field.getFreeNAdjacentLocations(getLocation(), steps);
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Deer fawn = new Deer(false, field, loc, randomGender());
            newDeer.add(fawn);
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
     *  
     * Checks whether a pair of deer objects can both breed if they both have reached the breeding age.
     * @return A value determining whether the deer can breed or not.
     */
    protected boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
    
    /**
     * Look for plants adjacent to or a distance from the current location.
     * Only the first live plant is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentNLocations(getLocation(), 2);
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext() ) {
            Location where = it.next();
            Object organism = field.getObjectAt(where);
            if(organism instanceof Plant) {
                Plant plant = (Plant) organism;
                if(plant.isAlive()) { 
                    plant.setDead();
                    foodLevel = PLANT_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
    
    /**
     * * Make this deer more hungry. This could result in the deer's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }
}