import java.util.Random;
import java.util.List;

/**
 * A class representing the characteristics of a Plant
 *
 * @author Anirudh George and Ahmet Bayazitoglu 
 * @version 2022.03.02  
 */
 
public class Plant extends Organism
{
    //The maximum age of the plant
    private static final int MAX_AGE = 10;
    //The rate at which a plant grows in height every step.
    private static final double GROWTH_RATE = 1.25;
    //A shared random number generator to control the growths of plants.
    private static final Random rand = Randomizer.getRandom();
    //The maximum height of a plant.
    private static final int MAX_HEIGHT = 250;
    //The likelihood of a plant growing in an empty space.
    private static final double PLANT_GROWTH_PROBABILITY = 0.06;
    
    //The plant's age.
    private int age;
    //The initial height of the plant.
    private double height = 1;
    //The weather
    private Weather weather = new Weather();
    /**
     * Create a new plant at location in field.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Plant(Field field, Location location) {
        super(field, location);
    }
    
    /**
     * Increase the age.
     * This could result in the plant's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * This is what the plant does most of the time - it grows in height and stays in a fixed position.
     * Sometimes it die of old age or if it reaches the maximum height.
     * @param newPlants A list to return newly born plants.
     */
    protected void act(List<Organism> newPlants) {
        incrementAge();
        if(weather.rainCounter() == true){
            grow(newPlants);
        }    
        if(isAlive()){
            if(height <= MAX_HEIGHT) {
               height *= GROWTH_RATE; 
            }
            else{
                setDead();
            }
        }
    }
    
    /**
     * If the adjacent locations to a plant are not occupied, plants grow there on each step.
     * @param newPlants are being grown.
     */
    protected void grow(List<Organism> newPlants) {
        if (getLocation()!= null) {
            List<Location> locations = field.adjacentLocations(getLocation());
            for (int i = 0; i < locations.size(); i++) {
                if (field.getObjectAt(locations.get(i)) == null && rand.nextDouble() < PLANT_GROWTH_PROBABILITY) {
                    Plant plant = new Plant(field, locations.get(i));
                    newPlants.add(plant);
                }
            }
        }
    }
}