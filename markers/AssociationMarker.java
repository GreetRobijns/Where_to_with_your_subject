package markers;

import java.util.ArrayList;
import java.util.List;

import Elements.Association;
import Elements.City;
import processing.core.PConstants;
import processing.core.PGraphics;
import de.fhpotsdam.unfolding.geo.Location;
import de.fhpotsdam.unfolding.marker.AbstractMarker;

public class AssociationMarker extends AbstractMarker{
	
	String name;
	City city;
	boolean comparebox;
	List<Association> associations;
	
	public AssociationMarker(Location location, City city){
		super (location);
		this.name = city.getName();
		this.id = name;
		this.city = city;
		this.comparebox = false;
		associations = new ArrayList<Association>();
	}
	
	public City getCity(){
		return city;
	}
	
	public void addAssociation(Association association){
		associations.add(association);
	}

	@Override
	public void draw(PGraphics pg, float x, float y) {
		pg.pushStyle();
		pg.fill(255, 0, 0);
		pg.ellipseMode(PConstants.CENTER);
		pg.ellipse(x,y,10,10);
		if(selected){
			pg.color(100);
			pg.text(name,x,y-10);
			
			pg.fill(255,255,255);
			pg.stroke(128,138,135);
			pg.strokeWeight(5);
			pg.rectMode(PConstants.CORNERS);
			float rightCornerX = x+400; 
			float rightCornerY = y-250; 
			pg.rect(x, y, rightCornerX, rightCornerY);
			pg.line(x+50, y-20, x+50, y-140);
			pg.line(x+50, y-20, x+180, y-20);
		}
		
		
//		if(comparebox){
//			pg.fill(highlightColor);
//			pg.stroke(highlightStrokeColor);
//			pg.rectMode(PConstants.CORNER);
//			pg.rect(x, y, x + 100, y - 100);
//		}
		pg.popStyle();
	}

	@Override
	protected boolean isInside(float checkX, float checkY, float x, float y) {
		return checkX > x - 15 && checkX < x + 15 && checkY > y - 15 && checkY < y + 15;
	}
	
	public int getDistance(float x, float y){
		return (int) Math.sqrt((Math.abs(x-location.x)*Math.abs(x-location.x) + Math.abs(y-location.y)*Math.abs(y-location.y)));
	}
	
	public void drawCompareBox(boolean comparebox){
		this.comparebox = comparebox;
	}

}
