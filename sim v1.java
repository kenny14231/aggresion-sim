import java.util.*;

import org.knowm.xchart.*;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.XYSeries.XYSeriesRenderStyle;
import org.knowm.xchart.style.Styler.LegendPosition;

import java.io.IOException;
import java.lang.Math;

public class AggressionSim {

	static int INIT_POPULATION_SIZE = 100;
	static int NUM_FOOD_PAIRS = 50;
	static double DOVE_PROPORTION = 0.9;
	static int NUM_DAYS = 50;
	
	public static ArrayList<Blob> initPop() {
		ArrayList<Blob> population = new ArrayList<Blob>();
		String type = "";
		
		for(int i=0; i < INIT_POPULATION_SIZE; i++) {
			double p = Math.random();
			if(p < DOVE_PROPORTION) {
				type = "dove";
			}
			else {
				type = "hawk";
			}
			
			population.add(new Blob(type));
		}
		return(population);
	}
	
	public static ArrayList<FoodPair> initBoard() {
		ArrayList<FoodPair> foodBoard = new ArrayList<FoodPair>();
		
		for(int i = 0; i < NUM_FOOD_PAIRS; i++) {
			foodBoard.add(new FoodPair());
		}
		
		return(foodBoard);
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Random rand = new Random();
		
		ArrayList<Blob> population = initPop();
		ArrayList<FoodPair> foodBoard = initBoard();
		double[] populationSizeOverTime = new double[NUM_DAYS];
		double[] aggregateDays = new double[NUM_DAYS];
		double[] numHawksOverTime = new double[NUM_DAYS];
		double[] numDovesOverTime = new double[NUM_DAYS];
		int toomany = 0;
		
		for(int days=0; days < NUM_DAYS; days++) {
			int populationSize = population.size();
			populationSizeOverTime[days] = populationSize;
			aggregateDays[days] = days;
			int numDoves = 0;
			int numHawks = 0;
			
			foodBoard = initBoard();
			
			//foraging stage
			for(int i = 0; i < populationSize; i++) {
				Blob blob = population.get(i);
				blob.setFullness(0);
				
				String blobType = blob.getType();
				if(blobType == "dove") {
					numDoves++;
				}
				else {
					numHawks++;
				}
				
				int counter = 0;
				
				int attackedFoodPair = rand.nextInt(NUM_FOOD_PAIRS);
				System.out.println(attackedFoodPair);
				FoodPair foodPair = foodBoard.get(attackedFoodPair);
				if(foodPair.getNumBlobs() < 2) {
					foodPair.addBlob(blob);
				}
				else {
					while(foodPair.getNumBlobs() >= 2) {
						attackedFoodPair = (attackedFoodPair + 1) % NUM_FOOD_PAIRS;
						foodPair = foodBoard.get(attackedFoodPair);
						counter++;
						if(counter > 100) {
							System.out.println("false effective");
							attackedFoodPair = rand.nextInt(NUM_FOOD_PAIRS);
							foodPair = foodBoard.get(attackedFoodPair);
							break;
						}
					}
					System.out.println("effective solution found");
					foodPair.addBlob(blob);
				}
				
				
			}
			
			numDovesOverTime[days] = numDoves;
			numHawksOverTime[days] = numHawks;
			
			//interaction stage
			for(int pair = 0; pair < NUM_FOOD_PAIRS; pair++) {
				FoodPair foodPair = foodBoard.get(pair);
				ArrayList<Blob> blobs = foodPair.getBlobs();
				int numBlobs = foodPair.getNumBlobs();
						
				if(numBlobs == 1) {
					blobs.get(0).setFullness(2);
					//System.out.println("lucky!");
				}
				else if(numBlobs >= 2) {
					//System.out.println("share");
					Blob blob1 = blobs.get(0);
					Blob blob2 = blobs.get(1);
					String blob1Type = blob1.getType();
					String blob2Type = blob2.getType();
					if(blob1Type.equals(blob2Type)) {
						if(blob1Type == "dove") {
							blob1.setFullness(1);
							blob2.setFullness(1);
						}
						else if(blob1Type == "hawk") {
							blob1.setFullness(0);
							blob2.setFullness(0);
						}
					} else {
						if(blob1Type == "dove") {
							blob1.setFullness(0.5);
							blob2.setFullness(1.5);
						}
						else if (blob1Type == "hawk"){
							blob1.setFullness(1.5);
							blob2.setFullness(0.5);
						}
					}
					if(numBlobs > 2) {
						System.out.println("too many at a food");
						toomany++;
					}
				}
			}
			
			//killing/reproducing stage
			/*Iterator<Blob> itr = population.iterator();
			while(itr.hasNext()) {
				Blob blob = itr.next();
				
				int blobHealth = blob.getFullness();
				
				if(blobHealth == 0) {
					itr.remove();
				}
				else if(blobHealth == 2) {
					String blobType = blob.getType();
					population.add(new Blob(blobType));
				}
			}*/
			/*for(int i=0; i<populationSize; i++) {
				try {
					Blob blob = population.get(i);
					
					double blobHealth = blob.getFullness();
					//System.out.println(blobHealth);
					if(blobHealth == 0) {
						population.remove(blob);
						//System.out.println("death");
					}
					else if(blobHealth == 0.5) {
						double p = Math.random();
						if(p < 0.5) {
							population.remove(blob);
						}
					}
					else if(blobHealth == 1.5) {
						double p = Math.random();
						if(p < 0.5) {
							String blobType = blob.getType();
							population.add(new Blob(blobType));
						}
					}
					else if(blobHealth == 2) {
						//System.out.println("reproduce");
						String blobType = blob.getType();
						population.add(new Blob(blobType));
					}
				}
				catch(Exception e) {
					
				}*/
			
			@SuppressWarnings("unchecked")
			ArrayList<Blob> tempPopulation = (ArrayList<Blob>)population.clone();
			
			for(int i=0; i<populationSize; i++) {
				Blob blob = population.get(i);
				
				double blobHealth = blob.getFullness();
				//System.out.println(blobHealth);
				if(blobHealth == 0) {
					tempPopulation.remove(blob);
					//System.out.println("death");
				}
				else if(blobHealth == 0.5) {
					double p = Math.random();
					if(p < 0.5) {
						tempPopulation.remove(blob);
					}
				}
				else if(blobHealth == 1.5) {
					double p = Math.random();
					if(p < 0.5) {
						String blobType = blob.getType();
						tempPopulation.add(new Blob(blobType));
					}
				}
				else if(blobHealth == 2) {
					//System.out.println("reproduce");
					String blobType = blob.getType();
					tempPopulation.add(new Blob(blobType));
				}
			}
			
			population = tempPopulation;
		}
		
		System.out.println(toomany);
		
		XYChart chart = new XYChartBuilder().width(600).height(500).
				title("Dove VS Hawk in population").xAxisTitle("Days").yAxisTitle("Number of Individuals").build();
		
		chart.getStyler().setDefaultSeriesRenderStyle(XYSeriesRenderStyle.Line);
		chart.getStyler().setChartTitleVisible(true);
		chart.getStyler().setLegendPosition(LegendPosition.OutsideS);
		chart.getStyler().setMarkerSize(0);
		
		chart.addSeries("Doves", aggregateDays, numDovesOverTime);
		chart.addSeries("Hawks", aggregateDays, numHawksOverTime);
		chart.addSeries("Total Population", populationSizeOverTime);
		
		new SwingWrapper<XYChart>(chart).displayChart();
		BitmapEncoder.saveBitmap(chart, "./Sample_Chart", BitmapFormat.PNG);
	}

}
