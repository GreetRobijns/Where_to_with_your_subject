package map;
import processing.core.PApplet;
import processing.core.PFont;
import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoJSONReader;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.utils.MapUtils;
import markers.CityMarker;
import org.geonames.WebService;

import Elements.Association;
import Elements.City;
import Elements.Paper;
import Elements.Person;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.DC;

import java.awt.Font;
import java.io.*;

import java.util.*;


public class LAK extends PApplet {

	private static final long serialVersionUID = -325652256485957908L;

	InputStream lak2011 = FileManager.get().open("2011_fulltext_.rdf");
	InputStream lak2012 = FileManager.get().open("2012_fulltext_.rdf");
	InputStream edm2011 = FileManager.get().open("edm2011.rdf");
	InputStream edm2012 = FileManager.get().open("edm2012.rdf");
	
	List<Association> associations; //List of all associations
	List<City> cities;
	List<Paper> papers;
	
	ArrayList<CityMarker> attendingCityMarkers; //List of county markers (for the attending countries)
	List<Marker> countryMarkers; //List of country markers
	List<Marker> nbPapersMarkers;
	List<Marker> associationMarkers; //List of association markers
	List<Marker> activeCountryMarkers; //List of active country markers
	List<Marker> activeCityMarkers; //List of the active city markers
	UnfoldingMap map; //The map
	UnfoldingMap mapOverview; // The overviewMap
	boolean compare; //Whether of not the compare-view is drawn
	List<CityMarker> compareMarkers;


	
	private enum dataType {LAK_2011, LAK_2012, EDM_2011, EDM_2012}; //enum of all possible data types.
	
	/**
	 * Setup all the data and map
	 */
	public void setup() {
		//INITIALIZATION
		size(800, 600);
		associations = new ArrayList<Association>();
		cities = new ArrayList<City>();
		papers = new ArrayList<Paper>();
		attendingCityMarkers = new ArrayList<CityMarker>();
		compareMarkers = new ArrayList<CityMarker>();
		map = new UnfoldingMap(this);
		mapOverview = new UnfoldingMap(this, "overview", 0, 400, 250, 250);
		this.compare = false;
		//Connect to geoNames
		WebService.setUserName("wardcapsel");

		MapUtils.createDefaultEventDispatcher(this, map);
		//Read in the data from the txt file
		processCities();
		//Read in the data from the rdf file
		System.out.println("processing " + dataType.LAK_2011.toString() + "...");
		processRDF(lak2011, dataType.LAK_2011);
		System.out.println("processing " + dataType.LAK_2012.toString() + "...");
		processRDF(lak2012, dataType.LAK_2012);
		System.out.println("processing " + dataType.EDM_2011.toString() + "...");
		processRDF(edm2011, dataType.EDM_2011);
		System.out.println("processing " + dataType.EDM_2012.toString() + "...");
		processRDF(edm2012, dataType.EDM_2012);
		//Create the country markers
		//This is the coloring of the countries according to LAK and EDM data
		attendingCityMarkers = setAttendingCityMarkers(associations);
		countryMarkers = setCountryMarkers(attendingCityMarkers);
		activeCountryMarkers = countryMarkers;
		activeCityMarkers = setCityMarkers(attendingCityMarkers);
		associationMarkers = setCityMarkers(attendingCityMarkers);
	}

	/**
	 * Method to draw the map.
	 */
	public void draw() {
		map.getMarkers().clear();
		mapOverview.getMarkers().clear();
		map.addMarkers(activeCountryMarkers);
		mapOverview.addMarkers(activeCountryMarkers);
		background(0);
		if(map.getZoomLevel()>=4) {
			map.addMarkers(activeCityMarkers);
		}
		map.draw();
		mapOverview.draw();
		if(this.compare){
			CityMarker comp1 = compareMarkers.get(0);
			CityMarker comp2 = compareMarkers.get(1);
			pushStyle();
			fill(255,255,255);
			stroke(128,138,135);
			strokeWeight(5);
			rectMode(CORNERS);
			rect(50, 50, 750, 550);
			pushStyle();
			fill(128,138,135);
			rect(725,75,750,50);
			stroke(255,255,255);
			strokeWeight(3);
			line(730, 55, 745, 70);
			line(745, 55, 730, 70);
			popStyle();
			line(125,300,350,300);
			line(450,300,675,300);
			line(125,300,125,125);
			line(450,300,450,125);
			fill(128,138,135);
			textFont(new PFont(new Font("Champagne & Limousines", Font.BOLD, 30), true));
			text(comp1.getCity().getName(), 150,100);
			text(comp2.getCity().getName(), 475,100);
			pushStyle();
			strokeWeight(20);
			int max1 = Math.max(Math.max(comp1.getLAK2011(), comp1.getLAK2012()), Math.max(comp1.getEDM2011(), comp1.getEDM2012()));
			int max2 = Math.max(Math.max(comp2.getLAK2011(), comp2.getLAK2012()), Math.max(comp2.getEDM2011(), comp2.getEDM2012()));
			int max = Math.max(max1, max2);
			double factor = 0;
			if(max > 0) 
				factor = 150/max;
			if(comp1.getLAK2011()>0) {
				stroke(255,0,0);        
				fill(255,0,0);
				rect(155, 293, 155, (float) (293-comp1.getLAK2011()*factor),1,1,0,0);
			}
			if(comp1.getEDM2011()>0) {
				stroke(255,255,0);        
				fill(255, 255, 0);
				rect(195, 293, 195, (float) (293-comp1.getEDM2011()*factor),1,1,0,0);
			}
			if(comp1.getLAK2012()>0) {
				stroke(255,0,0);        
				fill(255,0,0);
				rect(255, 293, 255, (float) (293-comp1.getLAK2012()*factor),1,1,0,0);
			}	
			if(comp1.getEDM2012()>0) {
				stroke(255,255,0);        
				fill(255, 255, 0);
				rect(295, 293, 295, (float) (293-comp1.getEDM2012()*factor),1,1,0,0);
			}
			if(comp2.getLAK2011()>0) {
				stroke(255,0,0);        
				fill(255,0,0);
				rect(480, 293, 480, (float) (293-comp2.getLAK2011()*factor),1,1,0,0);
			}
			if(comp2.getEDM2011()>0) {
				stroke(255,255,0);        
				fill(255, 255, 0);
				rect(520, 293, 520, (float) (293-comp2.getEDM2011()*factor),1,1,0,0);
			}
			if(comp2.getLAK2012() >0) {
				stroke(255,0,0);        
				fill(255,0,0);
				rect(580, 293, 580, (float) (293-comp2.getLAK2012()*factor),1,1,0,0);
			}
			if(comp2.getEDM2012() > 0) {
				stroke(255,255,0);        
				fill(255, 255, 0);
				rect(620, 293, 620, (float) (293-comp2.getEDM2012()*factor),1,1,0,0);
			}
			popStyle();
			textFont(new PFont(new Font("Champagne & Limousines", Font.BOLD, 15), true));
			text(max, 105,135);
			text("#papers" , 105, 120);
			text(max, 430,135);
			text("#papers" , 430, 120);
			
			textFont(new PFont(new Font("Champagne & Limousines", Font.BOLD, 13), true));
			text("2011", (float) 175,325);
			text("2012", (float) 275,325);
			text("2011", (float) 500,325);
			text("2012", (float) 600,325);
			popStyle();
		}
	}
	
	public void showCompareView(){
		compareMarkers.clear();
		for(Marker marker: activeCityMarkers){
			if(((CityMarker) marker).getCompareOn()){
				compareMarkers.add((CityMarker) marker);
			}
		}
		if(compareMarkers.size() > 1){
			this.compare = true;
		}
	}
	
	public void clearCompareView(){
		this.compare = false;
	}
	
	/**
	 * Method to redraw the map to the given subject
	 * @param subject
	 */
	public void colorCitiesToSubject(String subject)
	{
		List<Marker> subjectMarkers = new ArrayList<Marker>();
		ArrayList<CityMarker> attendingSubjectCityMarkers = new ArrayList<CityMarker>();
		List<Association> tmpAssociations = new ArrayList<Association>();
		HashMap<Association, List<Paper>> associationEDM = new HashMap<Association, List<Paper>>();
		HashMap<Association, List<Paper>> associationLAK = new HashMap<Association, List<Paper>>();
		for(Paper paper: papers)
		{	
			for(String topic: paper.getSubjects()) 
			{
				if(topic.equalsIgnoreCase(subject) || topic.contains((subject.toLowerCase()))) 
				{
					if(paper.wasForEDM2011() || paper.wasForEDM2012()) 
					{
						if(paper.getMakers().size() > 0)
						{
							Association association = paper.getMakers().get(0).getAssociation();
							if(!associationEDM.containsKey(association)) 
							{
								ArrayList<Paper> tmp = new ArrayList<Paper>();
								tmp.add(paper);
								associationEDM.put(association, (tmp));
							}
							else
							{
								associationEDM.get(association).add(paper);
							}
						}
					}
					else if(paper.wasForLAK2011()||paper.wasForLAK2012()) 
					{
						if(paper.getMakers().size() > 0)
						{
							Association association = paper.getMakers().get(0).getAssociation();
							if(!associationLAK.containsKey(association)) 
							{
								ArrayList<Paper> tmp = new ArrayList<Paper>();
								tmp.add(paper);
								associationLAK.put(association, (tmp));
							}
							else
							{
								associationLAK.get(association).add(paper);
							}
						}
					}
				}
			}
		}
		for(Association association: associationEDM.keySet()) {
			CityMarker marker = new CityMarker(association, associationLAK.get(association), associationEDM.get(association));
			attendingSubjectCityMarkers.add(marker);
			tmpAssociations.add(association);
			associationLAK.remove(association);
		}
		for(Association association: associationLAK.keySet()) {
			CityMarker marker = new CityMarker(association, associationLAK.get(association), associationEDM.get(association));
			attendingSubjectCityMarkers.add(marker);
			tmpAssociations.add(association);
		}		    
		activeCityMarkers = setCityMarkers(attendingSubjectCityMarkers);
		subjectMarkers = setCountryMarkers(attendingSubjectCityMarkers);
		activeCountryMarkers = subjectMarkers;
	}

	/**
	 * Method to read in all data from the txt file.
	 * This consists the connection creating a City object and Association object for each of the cities and associations in the file and linking them.
	 */
	private void processCities() {
		System.out.println("Reading in cities...");
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("universities.txt"));
			String text = null;
			// repeat until all lines is read
			while ((text = reader.readLine()) != null) {
				String[] parts = text.split("&");
				String association = parts[0];
				String city = parts[1];
				String state = parts[2];
				String country = parts[3];
				City createdCity = new City(city, state, country);
				Association createdAssociation = new Association(association, createdCity); 
				boolean addCity = true;
				for(City addedCity : cities) {
					if(addedCity.getLocation().equals(createdCity.getLocation()))
						addCity = false;
				}
				boolean addAssociation = true;
				for(Association addedAssociation : associations) {
					if(addedAssociation.getName().equals(createdAssociation.getName())) {
						addAssociation = false;
					}
				}
				if(addCity)
					cities.add(createdCity);
				if(addAssociation)
					associations.add(createdAssociation);
				
			}
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("OK!");
	}
	
	/**
	 * Method to process an RDF file.
	 * @param file - The Inputstream of the RDF file
	 * @param type - The type of data. 
	 */
	private void processRDF(InputStream file, dataType type) {
		//Create a model separately for each conference
		Model model = ModelFactory.createDefaultModel();
		model.read(file, null);
		//Get the associations and attendees 
		getAssociationsAndAttendees(model, type);
		getPapers(model,type);
	}
	
	/**
	 * Method to retrieve all associations and link the attendees to their association.
	 * @param m - The model.
	 * @param type - The type of data. 
	 */
	private void getAssociationsAndAttendees(Model m, dataType type) {
		System.out.println("Reading in associations...");
		ResIterator iter2 = m.listResourcesWithProperty(FOAF.member); //Iterator over members
		if (iter2.hasNext()) {
			while (iter2.hasNext()) {
				Resource thisElement = iter2.nextResource();
				String associationName = thisElement.getRequiredProperty(FOAF.name).getString();
				Association association = null;
				for(Association addedAssociation : associations) {
					if(addedAssociation.getName().equals(associationName)) {
						association = addedAssociation;
					}
				}
				StmtIterator members = thisElement.listProperties(FOAF.member);
				List<Person> names = new ArrayList<Person>();
				while (members.hasNext()) {
					String urlName = members.nextStatement().getResource().toString();
					String name = parse(urlName);
					Person person = new Person(name,association);
					person.setAsAttending(type.name());
					names.add(person);
				} 
				if(association!=null) {
					association.addPeople(names);
				}
			}
		} 
		else 
			System.out.println("No associations were found in the database");
		System.out.println("OK!");
	}
	
	/**
	 * Method to retrieve all subjects and link the subjects to their association.
	 * @param m - The model.
	 * @param type - The type of data. 
	 */
	private void getPapers(Model m, dataType type) {
		System.out.println("Reading in papers...");
		ResIterator iter2 = m.listResourcesWithProperty(DC.title); //Iterator over papers
		if (iter2.hasNext()) {
			while (iter2.hasNext()) {
				Resource thisElement = iter2.nextResource();
				String title = thisElement.getProperty(DC.title).getString();
				System.out.println("title: " + title);
				StmtIterator makers = thisElement.listProperties(FOAF.maker);
				List<Person> makernames = new ArrayList<Person>();
				while (makers.hasNext()) {
					String maker = makers.nextStatement().toString();
					String[] parts = maker.split(",");
					String author = parts[2];
					parts = author.split("/");
					parts=  (parts[6].split("]")[0]).split("-");
					String name = parts[0];
					for(int i=1; i<parts.length;i++)
						name = name + " " + parts[i];
					for(City city: cities) {
						for(Person person: city.getAssociatedPeople()) {
							if(person.getName().equalsIgnoreCase(name))
								makernames.add(person);		
						}
					}
					System.out.println("maker: " + name);
				}
				StmtIterator subjects = thisElement.listProperties(DC.subject);
				List<String> names = new ArrayList<String>();
				while (subjects.hasNext()) {
					String name = subjects.nextStatement().getString();
					names.add(name);
					System.out.println("subject: " + name);
				}
				Paper paper = new Paper(title,makernames,names);	
				paper.setAsForConference(type.name());
				papers.add(paper);
			}
		} 
		else 
			System.out.println("No subjects were found in the database");
		System.out.println("OK!");
	}
	
	/**
	 * Method to add a marker at a given location (name) and for a given association.
	 * @param location - The name of the location.
	 * @param affiliation - The name of the association (located at the given location).
	 * @return 
	 */
	private List<Marker> setCountryMarkers(ArrayList<CityMarker> attendingCityMarkers) {
		int maximimum = 0;
		List<Feature> countries = GeoJSONReader.loadData(this, "countries.geo.json");
		List<Marker> countryMarkers = MapUtils.createSimpleMarkers(countries);
		nbPapersMarkers = MapUtils.createSimpleMarkers(countries);
		List<Integer> nbPapers = new ArrayList<Integer>();
		for (Marker markerCountry : countryMarkers) {
			String countryId = markerCountry.getId();
			boolean hit = false;
			int lak = 0;
			int edm = 0;
			for (CityMarker marker : attendingCityMarkers) {
				String country = marker.getCity().getCountry();
				if (countryId.equals(country)) {
					lak += marker.getLAKPapers().size();
					edm += marker.getEDMPapers().size();
					hit = true;
				}
			}
			if (hit) {
				int transparancy = (lak+edm);
				if(transparancy > maximimum)
					maximimum = transparancy;
				nbPapers.add(transparancy);
				if(edm==0 && lak!=0){
					markerCountry.setColor(color(255,0,0/*, transparancy*/));
					
				}
				else if(edm!=0 && lak==0){
					markerCountry.setColor(color(255,255,0/*, transparancy*/));
				}
				else if(lak!=0 && edm!=0){
					double lakD = new Double(lak);
					double edmD = new Double(edm);
					double ratio = (lakD/edmD)/2;
					if(ratio>1)
					{
						ratio = ratio/10;
					}
					int red = 255;
					int green = (int) Math.round(ratio*255);
					green = Math.max(green, 0);
					green = Math.min(green, 255);
					int blue = 0;
					markerCountry.setColor(color(red, green, blue/*, transparancy*/));
				}
				else {
					markerCountry.setColor(color(255, 255, 255, 0));
				}
				System.out.println("LAK: " + lak);
				System.out.println("EDM: " + edm);
				System.out.println("*******" + countryId);
			} else {
				markerCountry.setColor(color(255, 255, 255, 0));
				nbPapers.add(0);
				
			}
		}
		for(int i = 0; i <nbPapers.size(); i++)
		{
			if(nbPapers.get(i)==maximimum)
				nbPapersMarkers.get(i).setColor(color(73, 157, 72));
			else if(nbPapers.get(i)==0)
				nbPapersMarkers.get(i).setColor(color(255, 255, 255,0));
			else
			{
					Double ratio = (double) (((double) nbPapers.get(i)/(double) maximimum)*700);
					Integer transparancy = (int) Math.round(ratio);
					nbPapersMarkers.get(i).setColor(color(73, 157, 72,transparancy));
			}
		}
		
		
		
		return countryMarkers;
	}
	
	/**
	 * 
	 * @param associations
	 * @return
	 */
	private ArrayList<CityMarker> setAttendingCityMarkers(List<Association> associations) {
		ArrayList<CityMarker> attendingCityMarkers = new ArrayList<CityMarker>();
		for(Paper paper : papers) {
			if(paper.getMakers().size() > 0){
			Association association = paper.getMakers().get(0).getAssociation();
			CityMarker marker;
			if(paper.wasForEDM2011() || paper.wasForEDM2012()){
				ArrayList<Paper> edmpapers = new ArrayList<Paper>();
				edmpapers.add(paper);
				marker = new CityMarker(association, null, edmpapers);
			}
			else{
				ArrayList<Paper> lakpapers = new ArrayList<Paper>();
				lakpapers.add(paper);
				marker = new CityMarker(association, lakpapers, null);
			}
			attendingCityMarkers.add(marker);	
			}
		}
		return attendingCityMarkers;
	}
	
	public List<Marker> setCityMarkers(List<CityMarker> markers){
		List<Marker> cityMarkers = new ArrayList<Marker>();
		for (CityMarker marker: markers){
			boolean present = false;
			for(Marker mark: cityMarkers){
				if(marker.getCity().getName().equals(((CityMarker) mark).getCity().getName())){
					present = true;
					((CityMarker) mark).addAssociations(marker.getAssociations());
					((CityMarker) mark).addLAKPapers(marker.getLAKPapers());
					((CityMarker) mark).addEDMPapers(marker.getEDMPapers());
				}
			}
			if(!present){
				cityMarkers.add(marker);
			}
		}
		return cityMarkers;
	}

	public void mouseMoved() {
		List<Marker> hitMarkers = map.getHitMarker(mouseX, mouseY);
		boolean hit = false;
		for(Marker hitMarker: hitMarkers){
			if(hitMarker.getClass() == CityMarker.class){
				hitMarker.setSelected(true);
				hit = true;
				for (Marker marker : map.getMarkers()) {
					if (marker != hitMarker)
						marker.setSelected(false);
				}
				break;
			}
		}
		if(!hit){
			// Deselect all other markers
			for (Marker marker : map.getMarkers()) {
				marker.setSelected(false);
			}
		}
	}
	
	public void mouseClicked(){
		if(crossOver()){
			this.compare = false;
			for(Marker marker: activeCityMarkers){
				((CityMarker) marker).setCompareOn(false);
			}
		}
		for(Marker marker: activeCityMarkers){
			((CityMarker) marker).mousePressed(mouseX,mouseY,((CityMarker) marker).getScreenPosition(map));
			if(marker.isSelected()){
				((CityMarker) marker).drawCompareBox(true);
			}
		}
	}
	
	public boolean crossOver(){
		boolean over = false;
		if(mouseX >= 725 && mouseX <= 750){
			if(mouseY >= 50 && mouseY <= 75){
				over = true;				
			}
		}
		return over;
	}

	/**
	 * Method to parse a given URL string.
	 * @param url - The string to be parsed.
	 * @return A parsed string. A '-' is replaced with a space.
	 */
	private String parse(String url) {
		String[] splitURL = url.split("/");
		String result = splitURL[splitURL.length - 1];
		if (result.contains("-")) {
			result = result.replaceAll("-", " ");
		}
		return result;
	}

	/**
	 * Method to quickly reset the map to the original setting of the countrymarkers
	 */
	public void reset() {
		activeCountryMarkers = countryMarkers;
		activeCityMarkers =  associationMarkers;
		
	}

	public void changeMarkers() {
		activeCountryMarkers = nbPapersMarkers;
		activeCityMarkers =  associationMarkers;
		
	}
}
