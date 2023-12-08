import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * A class showing the effects of time and weather
 *
 * @author Anirudh George  and Ahmet Bayazitoglu 
 * @version 2022.03.02 
 */
public class Weather
{
    public int noRainCounter;
    public int dayCounter;
    public String weather;
    private String dayTime;
    private Simulator simulator;
    private List<String> weatherList = new ArrayList<>(List.of("Sunny","rainy","cloudy"));
    private Random rand = new Random();
    /**
     * Constructor for objects of class Weather
     */
    public Weather()
    {
        weather = new String();
    }
    
    /**
     * This method calculates the time of the day 
     */
    public String dayTimeUpdate(){
        if (dayCounter == 1){
            dayTime = "Morning";
        }
        else if (dayCounter == 2){
            dayTime = "Mid-day";
        }
        else if (dayCounter == 3){
            dayTime= "Night";
        }
        else if (dayCounter == 4) {
            dayCounter = 1;
            dayTimeUpdate();
        }
        return dayTime;
    }
    
    /**
     * @returns weather
     */
    public String getWeather(){
        return this.weather;
    }
    
    /**
     * @return daytime
     */
    public String getTime(){
        return this.dayTime;
    }
    
    /**
     * @returns daycounter
     */
    public int getCounter(){
        return dayCounter;
    }
    
    /**
     * get random weather in each step 
     */
    public String randomWeather(){
        int i  = rand.nextInt(weatherList.size());
        weather = weatherList.get(i);
        return weather;
    }
    
    /**
     * counts the weather without rain 
     */
    public boolean rainCounter(){
        if (noRainCounter >= 5){
            return false;
        }
        return true;
    }
    
    /**
     * when rains sets no rain counter to 0
     */
    public void setRainCounter(){
        if (weather.equals("rainy")){
            noRainCounter = 0;
        }
    }
}