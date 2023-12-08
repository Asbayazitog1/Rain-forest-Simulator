import java.util.List;
/**
 * The general characteristics of an organism in this simulation.
 *
 * @author Anirudh George and Ahmet Bayazitoglu 
 * @version 2022.03.02 
 */
public abstract class Organism
{
    //The field of the organism
    protected Field field;
    //The location of the organism
    protected Location location;
    //Whether the organism is alive or not
    protected boolean alive;
    
    /**
     * Create a new organism at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Organism(Field field, Location location) {
        alive = true;
        this.field = field;
        setLocation(location);
    }
    
    /**
     * Abstract act method overriden in the sublasses
     */
    abstract protected void act(List<Organism> newOrganisms);

    /**
     * Return the organism's location.
     * @return The organism's location.
     */
    protected Location getLocation()
    {
        return location;
    }
    
    /**
     * Place the organism at the new location in the given field.
     * @param newLocation The organism's new location.
     */
    protected void setLocation(Location newLocation)
    {
        if(location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }
    
    /**
     * Return the organism's field.
     * @return The organism's field.
     */
    protected Field getField()
    {
        return field;
    }
    
    /**
     * Check whether the organism is alive or not.
     * @return true if the organism is still alive.
     */
    protected boolean isAlive()
    {
        return alive;
    }
    
    /**
     * Indicate that the organism is no longer alive.
     * It is removed from the field.
     */
    protected void setDead()
    {
        alive = false;
        if(location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }
}