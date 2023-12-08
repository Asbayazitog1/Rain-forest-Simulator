import java.util.List;
/**
 * The general characteristics of Predator
 *
 * @author Anirudh George and Ahmet Bayazitoglu
 * @version 2022.03.02
 */
public abstract class Predator extends Animal
{
    /**
     * Create a new predator at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param gender The gender of the predator.
     */
    public Predator(Field field, Location location, Gender gender)
    {
        super(field, location, gender);
    }

    /**
     * Abstract method which is overriden by each of the predators to show what they do at each step.

     */
    abstract protected void act(List<Organism> newPredators);

    /**
     * Abstract method which is overriden by each of the predators to find their respective food.
     */
    abstract protected Location findFood();
    
    /**
     * Abstract method which is overriden by each of the predators to give birth to their specific species.
     */
    abstract protected void giveBirth(List<Organism> newPredators);
}