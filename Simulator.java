import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A predator-prey simulator, based on a rectangular field in a rainforest
 * containing animals and plants with other stimuli affecting these.
 * 
 * @author David J. Barnes and Michael Kölling
 * @author Anirudh George and Ahmet Bayazitoglu
 * @version 2022.03.02 
 */
public class Simulator
{
    // Constants representing configuration information for the simulation.
    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 120;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 80;
    // The probability that a jaguar will be created in any given grid position.
    private static final double JAGUAR_CREATION_PROBABILITY = 0.02;
    // The probability that a tortoise will be created in any given grid position.
    private static final double TORTOISE_CREATION_PROBABILITY = 0.12;  
    // The probability that a bear will be created in any given grid position.
    private static final double BEAR_CREATION_PROBABILITY = 0.01;
    // The probability that a deer will be created in any given grid position.
    private static final double DEER_CREATION_PROBABILITY = 0.12;
    // The probability that a tiger will be created in any given grid position.
    private static final double TIGER_CREATION_PROBABILITY = 0.01;
    // The probability that a plant seed will be created in any given grid position.
    private static final double SEED_GERMINATION_RATE = 0.10;

    // List of animals in the field.
    private List<Organism> organisms;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;
    //The animal's gender.
    private Animal.Gender gender;
    //The weather
    private Weather weather = new Weather();
    /**
     * Construct a simulation field with default size.
     */
    public Simulator()
    {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width)
    {
        if(width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }
        
        organisms = new ArrayList<>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Tortoise.class, Color.ORANGE);
        view.setColor(Jaguar.class, Color.BLUE);
        view.setColor(Tiger.class, Color.RED);
        view.setColor(Deer.class, Color.YELLOW);
        view.setColor(Bear.class, Color.BLACK);
        view.setColor(Plant.class, Color.GREEN);
        // Setup a valid starting point.
        reset();
    }
    
    /**
     * Run the simulation from its current state for a reasonably long period,
     * (4000 steps).
     */
    public void runLongSimulation()
    {
        simulate(4000);
    }
    
    /**
     * Run the simulation from its current state for the given number of steps.
     * Stop before the given number of steps if it ceases to be viable.
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps)
    {
        for(int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            //delay(60);   // uncomment this to run more slowly
        }
    }
    
     /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * organism.
     */
    public void simulateOneStep()
    {
        step++;
        weather.dayCounter++;
        weather.dayTimeUpdate();
        weather.noRainCounter++;
        weather.rainCounter();
        // Provide space for newborn animals.
        List<Organism> newOrganisms = new ArrayList<>();
        
        // Let all organisms act.
        for(Iterator<Organism> it = organisms.iterator(); it.hasNext(); ) {
            Organism organism = it.next();
            organism.act(newOrganisms);
            if(! organism.isAlive()) {
                it.remove();
            }
        }
        // Add the newly born organisms to the main lists.
        organisms.addAll(newOrganisms);
        view.showStatus(step, field);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset()
    {
        step = 0;
        organisms.clear();
        populate();
        
        // Show the starting state in the view.
        view.showStatus(step, field);
    }
    
    /**
     * Randomly populate the field with organisms.
     */
    private void populate()
    {
        Random rand = Randomizer.getRandom();
        field.clear();
        Random genderDecider = Randomizer.getRandom();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= JAGUAR_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    if (genderDecider.nextDouble() > 0.5) {
                       Jaguar jaguar = new Jaguar(true, field, location, gender.MALE);
                       organisms.add(jaguar);
                    }
                    else{
                        Jaguar jaguar = new Jaguar(true, field, location, gender.FEMALE);
                        organisms.add(jaguar); 
                    }
                }
                
                else if(rand.nextDouble() <= TORTOISE_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    if (genderDecider.nextDouble() > 0.5) {
                        Tortoise tortoise = new Tortoise(true, field, location, gender.MALE);
                        organisms.add(tortoise);
                    }
                    else{
                        Tortoise tortoise = new Tortoise(true, field, location, gender.FEMALE);
                        organisms.add(tortoise); 
                    }
                }
                else if(rand.nextDouble() <= BEAR_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    if (genderDecider.nextDouble() > 0.5) {
                        Bear bear = new Bear(true, field, location, gender.MALE);
                        organisms.add(bear);
                    }
                    else{
                        Bear bear = new Bear(true, field, location, gender.FEMALE);
                        organisms.add(bear); 
                    }
                }
                else if(rand.nextDouble() <= DEER_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    if (genderDecider.nextDouble() > 0.5) {
                        Deer deer = new Deer(true, field, location, gender.MALE);
                        organisms.add(deer);
                    }
                    else{
                        Deer deer = new Deer(true, field, location, gender.FEMALE);
                        organisms.add(deer); 
                    }
                }
                else if(rand.nextDouble() <= TIGER_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    if (genderDecider.nextDouble() > 0.5) {
                       Tiger tiger = new Tiger(true, field, location, gender.MALE);
                       organisms.add(tiger);
                    }
                    else{
                        Tiger tiger = new Tiger(true, field, location, gender.FEMALE);
                        organisms.add(tiger); 
                    }
                    // else leave the location empty.
                }
                else if (rand.nextDouble() <= SEED_GERMINATION_RATE) {
                    Location location = new Location(row, col);
                    Plant plant = new Plant(field, location);
                    organisms.add(plant);
                }
            }
        }
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    private void delay(int millisec)
    {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
}