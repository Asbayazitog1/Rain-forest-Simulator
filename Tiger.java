import java.util.Random;
import java.util.List;
import java.util.Iterator;

/**
 * A simple model of a tiger.
 * Tigers sleep, breed, age, move, eat tortoises, deers, bears and jaguars, and die.
 *
 * @author Anirudh George and Ahmet Bayazitoglu 
 * @version 2022.03.02 
 */
public class Tiger extends Predator
{
    private static final int BREEDING_AGE = 50;
    // The age to which a tiger can live.
    private static final int MAX_AGE = 350;
    // The likelihood of a tiger breeding.
    private static final double BREEDING_PROBABILITY = 0.0201;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 2;
    // The food value of a single tortoise. In effect, this is the
    // number of steps a tiger can go before it has to eat again.
    private static final int TORTOISE_FOOD_VALUE = 5;
    //The food value of a single jaguar. 
    private static final int JAGUAR_FOOD_VALUE = 9;
    //The food value of a single bear.
    private static final int BEAR_FOOD_VALUE = 10;
    //The food value of a single deer.
    private static final int DEER_FOOD_VALUE = 6;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    //The maximum amount of food a tiger can have.
    private static final int MAX_FOOD_LEVEL = 240;
    // The tiger's age.
    private int age;
    // The tiger's food level, which is increased by eating any animal.
    private int foodLevel;
    //The weather
    private Weather weather = new Weather();
    /**
     * Create a new tiger. A tiger may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the tiger will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param gender The gender of the tiger
     */
    public Tiger(boolean randomAge, Field field, Location location, Gender gender)
    {
        super(field, location, gender);
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(TORTOISE_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = TORTOISE_FOOD_VALUE + BEAR_FOOD_VALUE + DEER_FOOD_VALUE + JAGUAR_FOOD_VALUE;
        }
    }
    
    /**
     * This is what the tiger does most of the time: it hunts for
     * any animal. In the process, it might meet and breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newTigers A list to return newly born tigers.
     */
    protected void act(List<Organism> newTigers)
    {
        incrementAge();
        incrementHunger();
        if(weather.dayCounter == 3 ){
            sleep();
        }
        else if(isAlive()) {
           
            meet(newTigers);
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
     * Increase the age. This could result in the tiger's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Make this tiger more hungry. This could result in the tiger's death.
     */
    private void incrementHunger()
    {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * Look for animals adjacent to or a distance from the current location.
     * Only the first live animal is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    protected Location findFood()
    {
        Field field = getField();
        List<Location> adjacent = field.adjacentNLocations(getLocation(), 2);
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Tortoise) {
                Tortoise tortoise = (Tortoise) animal;
                if(tortoise.isAlive()) { 
                    tortoise.setDead();
                    foodLevel = TORTOISE_FOOD_VALUE;
                    return where;
                }
            }
            
            if(animal instanceof Jaguar) {
                Jaguar jaguar = (Jaguar) animal;
                if(jaguar.isAlive()) { 
                    jaguar.setDead();
                    foodLevel = JAGUAR_FOOD_VALUE;
                    return where;
                }
            }
            
            if(animal instanceof Bear) {
                Bear bear = (Bear) animal;
                if(bear.isAlive()) { 
                    bear.setDead();
                    foodLevel = BEAR_FOOD_VALUE;
                    return where;
                }
            }
            
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
     * Check whether or not this tiger is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newTigers A list to return newly born tigers.
     */
    protected void giveBirth(List<Organism> newTigers)
    {
        // New tigers are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Tiger cub = new Tiger(false, field, loc, randomGender());
            newTigers.add(cub);
        }
    }
    
    /**
     * Check whether or not this tiger is to give birth at this step.
     * New births will be made into free locations at a larger distance .
     * @param newTigers A list to return newly born tigers.
     * @param steps vary the radius between two tigers in which they can give birth.
     */
    protected void giveBirth(List<Organism> newTigers, int steps)
    {
        // New tigers are born into adjacent locations.
        // Get a list of further apart free locations.
        Field field = getField();
        List<Location> free = field.getFreeNAdjacentLocations(getLocation(), steps);
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Tiger cub = new Tiger(false, field, loc, randomGender());
            newTigers.add(cub);
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
     * Checks whether a pair of tiger objects can both breed if they both have reached the breeding age.
     * @return A value determining whether the tigers can breed or not.
     */
    protected boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}