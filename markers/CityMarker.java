package markers;

import java.util.ArrayList;
import java.util.List;

import Elements.Association;
import Elements.City;
import Elements.Paper;
import Elements.Person;

import de.fhpotsdam.unfolding.marker.AbstractMarker;
import de.fhpotsdam.unfolding.utils.ScreenPosition;
import processing.core.PConstants;
import processing.core.PGraphics;

public class CityMarker extends AbstractMarker {

	List<Person> lakmembers;
	List<Person> edmmembers;
	List<Paper> lakpapers;
	List<Paper> edmpapers;
	String name;
	City city;
	boolean comparebox;
	boolean compareOn;
	List<Association> associations;

	public CityMarker(Association association, List<Paper> lakpapers, List<Paper> edmpapers) {
		super(association.getCity().getLocation());
		this.city = association.getCity();
		this.id = this.city.getName();
		this.comparebox = false;
		this.compareOn = false;
		if(lakpapers != null)
			this.lakpapers = lakpapers;
		else
			this.lakpapers = new ArrayList<Paper>();
		if(edmpapers != null)
			this.edmpapers = edmpapers;
		else
			this.edmpapers = new ArrayList<Paper>();
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
		this.lakmembers.addAll(lakmembers);
	}

	public void addEDMMembers(List<Person> edmmembers){
		this.edmmembers.addAll(edmmembers);
	}	

	public void addLAKPapers(List<Paper> lakpapers){
		this.lakpapers.addAll(lakpapers);
	}

	public void addEDMPapers(List<Paper> edmpapers){
		this.edmpapers.addAll(edmpapers);
	}

	public List<Person> getLAKMembers(){
		return lakmembers;
	}

	public List<Person> getEDMMembers(){
		return edmmembers;
	}
	
	public List<Paper> getLAKPapers(){
		return lakpapers;
	}
	
	public List<Paper> getEDMPapers(){
		return edmpapers;
	}

	@Override
	public void draw(PGraphics pg, float x, float y) {
		pg.pushStyle();
		pg.pushMatrix();
		pg.fill(0, 255, 0);
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
			if(getLAK2011()>0) 
				pg.rect(20,-11,20, (float) (-11-getLAK2011()*factor), 1,1,0,0);
			if(getLAK2012()>0)
				pg.rect(90, -11, 90, (float) (-11-getLAK2012()*factor),1,1,0,0);
			pg.stroke(255, 255, 0);
			if(getEDM2011()>0)
				pg.rect(50, -11, 50, (float) (-11-getEDM2011()*factor),1,1,0,0);
			if(getEDM2012()>0)
				pg.rect(120, -11, 120, (float) (-11-getEDM2012()*factor),1,1,0,0);
			pg.fill(0,0,0);
			pg.popMatrix();
			pg.text(max, 15,-120);
			pg.text("#papers", 15, -135);
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
			for(Paper lakpap: lakpapers){
				for(Person lak: lakpap.getMakers()){
					count2 ++;
					if(count2 < 5 && !personsstr.contains(lak.getName())){
						personsstr += lak.getName();
						personsstr += ", ";					
					}
				}
			}
			for(Paper edmpap: edmpapers){
				for(Person edm: edmpap.getMakers()){
					count2 ++;
					if(count2 < 5 && !personsstr.contains(edm.getName())){
						personsstr += edm.getName();
						personsstr += ", ";					
					}	
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
		if(comparebox){
			if(compareOver(mouseX, mouseY, markerpos.x, markerpos.y)){
				this.compareOn = ! this.compareOn;
			}
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
	
	public int getEDM2011(){
		int count = 0;
		for(Paper paper: edmpapers){
			if(paper.wasForEDM2011()){
				count ++;
			}
		}
		return count;
	}

	public int getEDM2012(){
		int count = 0;
		for(Paper paper: edmpapers){
			if(paper.wasForEDM2012()){
				count ++;
			}
		}
		return count;
	}

	public int getLAK2011(){
		int count = 0;
		for(Paper paper: lakpapers){
			if(paper.wasForLAK2011()){
				count ++;
			}
		}
		return count;
	}

	public int getLAK2012(){
		int count = 0;
		for(Paper paper: lakpapers){
			if(paper.wasForLAK2012()){
				count ++;
			}
		}
		return count;
	}
}