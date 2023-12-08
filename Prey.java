import java.util.List;
/**
 * The general characteristics of Prey
 *
 * @author Anirudh George and Ahmet Bayazitoglu
 * @version 2022.03.02
 */
public abstract class Prey extends Animal
{
    /**
     * Create a new prey at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param gender The gender of the prey.
     */
    public Prey(Field field, Location location, Gender gender)
    {
        super(field, location, gender);
    }
    
    /**
     * Abstract method which is overriden by each of the prey to show what they do at each step.
     */
    abstract protected void act(List<Organism> newPrey);
    
    /**
     * Abstract method which is overriden by each of the prey to find their plant food.
     */
    abstract protected void giveBirth(List<Organism> newPrey);
}