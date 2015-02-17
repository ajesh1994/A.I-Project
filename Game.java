
public class Game {
	public static final int TOTAL_HOUSES = 12;
    
    public Player player1;
    public Player player2;
      
    public int store_player1;
    public int store_player2;
      
    public int[] houses;
      
    public Game(){
          
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
      
    public int convertHouseToGlobalHouse(Player player, int house){
        if(player == player1){
            return house;
        } else {
            return house + 6;
        }
    }
      
      
      
    public void sowerSeeds(Player player, int house){
        if(house < 0 || house > 5){
            return;
        }
          
        int housePosition = convertHouseToGlobalHouse(player, house);
        
        
        
        
        
        int totalSeeds = houses[housePosition];
          
          
        for(int seedIndex = 0; seedIndex < totalSeeds; seedIndex++){

        	
              
              
        }
          
        while(true){    
        }
    }
      
    public void setupNewGame(){
        store_player1 = 0;
        store_player2 = 0;
        houses = new int[12];
        for(int index = 0; index < TOTAL_HOUSES; index++){
            houses[index] = 4;
        }
    }
    
    public void move(int house){
    	
    	int totalSeeds = houses[house];
    	
    	houses[house] = 0;
    	
    	
    	int currentHouse = house+1;
    	
    	 for(int seedIndex = 0; seedIndex < totalSeeds; seedIndex++){
    		 
    		 if(currentHouse > 11){
    			 
    			 currentHouse = 0;
    			 store_player2++;
    			 seedIndex++;
    		 }
    		 
    		 houses[currentHouse] += 1;
    		 currentHouse++;
    	 }
    	
    }
    
    
    public void output(){
    	System.out.println(" ");
    	move(10);
    	for(int i = 5; i>= 0; i--){
    		System.out.print("  ["+houses[i]+"]  ");
    	}
    	System.out.println("");//new line
    	System.out.println("["+store_player1+"]                                     ["+store_player2+"]");
    	
    	for(int i = 6; i < 12; i++){
    		System.out.print("  ["+houses[i]+"]  ");
    	}
    }
    
    
}
