package markers;

import java.util.ArrayList;
import java.util.List;

import Elements.Association;
import Elements.City;
import Elements.Person;

import de.fhpotsdam.unfolding.marker.AbstractMarker;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PConstants;
import processing.core.PGraphics;

public class CityMarker extends AbstractMarker {

	List<Person> lakmembers;
	List<Person> edmmembers;
	String name;
	City city;
	boolean comparebox;
	boolean compareOn;
	List<Association> associations;

	public CityMarker(Association association,List<Person> lakmembers, List<Person> edmmembers) {
		super(association.getCity().getLocation());
		if(lakmembers != null)
			this.lakmembers = lakmembers;
		else
			this.lakmembers = new ArrayList<Person>();
		if(edmmembers != null)
			this.edmmembers = edmmembers;
		else
			this.edmmembers = new ArrayList<Person>();
		this.city = association.getCity();
		this.id = this.city.getName();
		this.comparebox = false;
		this.compareOn = false;
		associations = new ArrayList<Association>();
		associations.add(association);

	}

	public List<Association> getAssociations(){
		return associations;
	}

	public void addAssociations(List<Association> associations){
		this.associations.addAll(associations);
	}

	public void drawCompareBox(boolean comparebox){
		this.comparebox = comparebox;
		System.out.println("X: " + location.x);
		System.out.println("Y: " + location.y);
	}
	
	public boolean getCompareOn(){
		return this.compareOn;
	}

	public void setCompareOn(boolean compareOn){
		this.compareOn = compareOn;
	}
	
	public City getCity(){
		return city;
	}

	public void addLAKMembers(List<Person> lakmembers){
//		for(Person person: lakmembers){
//			this.lakmembers.add(person);
//		}
		this.lakmembers.addAll(lakmembers);
	}

	public void addEDMMembers(List<Person> edmmembers){
		this.edmmembers.addAll(edmmembers);
	}	

	public List<Person> getLAKMembers(){
		return lakmembers;
	}

	public List<Person> getEDMMembers(){
		return edmmembers;
	}

	@Override
	public void draw(PGraphics pg, float x, float y) {
		pg.pushStyle();
		pg.pushMatrix();
		pg.fill(255, 0, 0);
		pg.ellipseMode(PConstants.CENTER);
		pg.stroke(128,138,135);
		pg.strokeWeight(2);
		pg.ellipse(x,y,10,10);
		if(selected){
			pg.fill(0,0,0);
			pg.text(city.getName(),x,y-10);			
		}
		if(comparebox){
			pg.pushMatrix();
			pg.translate(x, y);
			pg.fill(255,255,255);
			pg.stroke(128,138,135);
			pg.strokeWeight(5);
			pg.rectMode(PConstants.CORNERS);
			pg.rect(0, 0, 600, -250);
			pg.pushStyle();
			pg.fill(128,138,135);
			pg.rect(575,-225,600,-250);
			pg.stroke(255,255,255);
			pg.strokeWeight(3);
			pg.line(580, -230, 595, -245);
			pg.line(595, -230, 580, -245);
			pg.popStyle();			
			pg.pushMatrix();
			pg.translate(30, -20);
			pg.line(0, 0, 0, -110);
			pg.line(0, 0, 160, 0);
			pg.pushStyle();
			pg.strokeWeight(20);
			pg.stroke(255,0,0);
			int max = Math.max(Math.max(getLAK2011(), getLAK2012()), Math.max(getEDM2011(), getEDM2012()));
			double factor = 0;
			if(max > 0) factor = 100/max;
//			System.out.println(factor);
			pg.line(20, 0, 20, (float) (-getLAK2011()*factor));
//			System.out.println(getLAK2011());
			pg.line(90, 0, 90, (float) (-getLAK2012()*factor));
//			System.out.println(getLAK2012());
			pg.stroke(0,0,255);
			pg.line(50, 0, 50, (float) (-getEDM2011()*factor));
//			System.out.println(getEDM2011());
			pg.line(120, 0, 120, (float) (-getEDM2012()*factor));
//			System.out.println(getEDM2012());
			pg.fill(0,0,0);
			pg.popMatrix();
			pg.text(max, 15,-120);
			pg.text("#persons", 15, -135);
			pg.textSize(30);
			pg.text(city.getName(),30,-210);
			pg.textSize(22);
			String associationsstr = "";
			int count1 = 0;
			for(Association ass: this.associations){
				count1 ++;
				if(count1 < 5 && !associationsstr.contains(ass.getName())){
					associationsstr += ass.getName();
					associationsstr += ", ";					
				}
			}
			if(associationsstr.length() > 0) associationsstr= associationsstr.substring(0, associationsstr.length() - 2);
			pg.text(associationsstr,30,-180);
			pg.textSize(15);
			String personsstr = "";
			int count2 = 0;
			for(Person lak: lakmembers){
//				System.out.println(lak.getName());
				count2 ++;
				if(count2 < 5 && !personsstr.contains(lak.getName())){
					personsstr += lak.getName();
					personsstr += ", ";					
				}
			}
			for(Person edm: edmmembers){
//				System.out.println(edm.getName());
				count2 ++;
				if(count2 < 5 && !personsstr.contains(edm.getName())){
					personsstr += edm.getName();
					personsstr += ", ";					
				}
			}
			if(personsstr.length() > 0) personsstr= personsstr.substring(0, personsstr.length() - 2);
			pg.text(personsstr,30,-160);
			pg.stroke(128,138,135);
			if(compareOn){
				pg.fill(255,0,0);
			}else{
				pg.fill(128,138,135);				
			}
			pg.strokeWeight(0);
			pg.rect(250, -75, 500, -100);
			pg.fill(255,255,255);
			pg.textSize(17);
			pg.text("Compare this city", 275, -80);
			pg.popStyle();
			pg.popMatrix();
		}
		pg.popMatrix();
		pg.popStyle();
	}

	@Override
	protected boolean isInside(float checkX, float checkY, float x, float y) {
		return checkX > x - 10 && checkX < x + 10 && checkY > y - 10 && checkY < y + 10;
	}

	public int getDistance(float x, float y){
		return (int) Math.sqrt((Math.abs(x-location.x)*Math.abs(x-location.x) + Math.abs(y-location.y)*Math.abs(y-location.y)));
	}

	public void mousePressed(float mouseX, float mouseY, ScreenPosition markerpos){
		if(crossOver(mouseX, mouseY, markerpos.x, markerpos.y)){
			this.comparebox = false;
		}
		if(compareOver(mouseX, mouseY, markerpos.x, markerpos.y)){
			this.compareOn = ! this.compareOn;
		}
	}
	
	public boolean crossOver(float mouseX, float mouseY, float markx, float marky){
		boolean over = false;
		if(mouseX >= markx + 575 && mouseX <= markx + 600){
			if(mouseY <= marky - 225 && mouseY >= marky - 250){
				over = true;				
			}
		}
		return over;
	}
	
	public boolean compareOver(float mouseX, float mouseY, float markx, float marky){
		boolean over = false;
		if(mouseX >= markx + 250 && mouseX <= markx + 500){
			if(mouseY <= marky - 75 && mouseY >= marky - 100){
				over = true;				
			}
		}
		return over;
	}
	
//	@Override
//	public String toString() {
//		return ("Marker at" + association.getCity().getLocation() + " from " + association + " with " + lakmembers.size() + " LAK members and " + edmmembers.size() + "EDM members.");
//	}

	public int getEDM2011(){
		int count = 0;
		for(Person person: edmmembers){
			if(person.isAttendingEDM2011()){
				count ++;
			}
		}
		return count;
	}

	public int getEDM2012(){
		int count = 0;
		for(Person person: edmmembers){
			if(person.isAttendingEDM2012()){
				count ++;
			}
		}
		return count;
	}

	public int getLAK2011(){
		int count = 0;
		for(Person person: lakmembers){
			if(person.isAttendingLAK2011()){
				count ++;
			}
		}
		return count;
	}

	public int getLAK2012(){
		int count = 0;
		for(Person person: lakmembers){
			if(person.isAttendingLAK2012()){
				count ++;
			}
		}
		return count;
	}



}