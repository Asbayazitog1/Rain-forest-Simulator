import java.util.List;
import java.util.Random;
/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes and Michael Kölling
 * @author Anirudh George  and Ahmet Bayazitoglu 
 * @version 2022.03.02 
 */
public abstract class Animal extends Organism
{
    //The gender of the animal.
    public Gender gender;
    //A shared random number generator to control disease.
    private Random rand = new Random();
    //Whether the animal has the disease or not.
    private boolean hasDisease;
    //The chance an animal gets infected
    private double INFECTION_PROBABILITY = 0.00005;
    public enum Gender {
        MALE,
        FEMALE
    }
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     * @param gender The gender of the animal
     */
    public Animal(Field field, Location location,Gender gender)
    {
        super(field, location);
        this.gender = gender;
    }
    
    /**
     * Abstract act() method overriden in the individual animal subclasses
     */
    abstract protected void act(List<Organism> newAnimals);
    
    /**
     * Abstract canBreed() method overriden in the individual animal subclasses
     */
    abstract protected boolean canBreed();
    
    /**
     * Abstract giveBirth() method overriden in the individual animal subclasses.
     */
    abstract protected void giveBirth(List<Organism> newAnimals);
    
    /**
     * Abstract giveBirth() method overriden in the individual animal subclasses
     */
    abstract protected void giveBirth(List<Organism> newAnimals, int steps);
    
    /**
     * The animals can meet within a distance of each other(set by the number of steps in the adjacentNlocations method)
     * 
     * Checks whether the animal objects in the other locations or the current animal object have the disease after calling the getsInfected() method 
     * which could result in both animals getting the disease.
     * The disease therefore spreads and stops the animals giving birth.
     * 
     * @param newOrganisms have been given birth by two animals of the same class with opposite genders ensuring they can both breed.
     */
    protected void meet(List<Organism> newOrganisms) {
        int steps = 10;
        List<Location> locations = field.adjacentNLocations(getLocation(), steps);
        this.getsInfected();
        
        for (int i = 0; i < locations.size(); i++) {
            if (field.getObjectAt(locations.get(i)) != null && field.getObjectAt(locations.get(i)).getClass() != Plant.class) {
                Animal animalObject =(Animal) field.getObjectAt(locations.get(i));
                if (animalObject != null) {
                    animalObject.getsInfected();
                    if(animalObject.hasDisease == true || this.hasDisease == true){
                        animalObject.hasDisease = true;
                        this.hasDisease = true;
                        break;
                    }
                    else{                           
                        if (animalObject.getClass()==this.getClass()&& animalObject.getGender()!= this.getGender()&& (animalObject.canBreed() && this.canBreed())){
                                giveBirth(newOrganisms, steps);
                        } 
                    }
                }
            }     
        }
    }
      
    /**
     * Randomly sets the gender of an animal when it's being created.
     */
    protected Gender randomGender() {
        int x = rand.nextInt(Gender.values().length);
        return Gender.values()[x];
    }
     
    /**
     * @return gender to get the gender of an animal object. 
     */
    public Gender getGender(){
        return gender;
    }
    
    /**
     * This method will tell us whether an animal becomes infected or not.
     * If the animal doesn't have the disease, the animal becomes infected or not based on its random infection rate.
     */
    protected void getsInfected() {
        if (hasDisease == false) {
            Random infectedRate = Randomizer.getRandom();
            if (infectedRate.nextDouble() < INFECTION_PROBABILITY) {
                   hasDisease = true;
            }
        }
    }
    
    /**
     * This method is to keep animals at the same space when they need to sleep.
     */
    public void sleep(){
        location = location;
        field = field;
    }
}