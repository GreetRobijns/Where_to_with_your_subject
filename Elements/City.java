package Elements;

import java.util.ArrayList;
import java.util.List;

import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;

import de.fhpotsdam.unfolding.geo.Location;

public class City {

	private String name;
	private String state;
	private String country;
	private Location location;
	private List<Person> associatedPeople;
	
	
	public City(String name, String state, String country) {
		this.name = name;
		this.state = state;
		this.country = country;
		associatedPeople = new ArrayList<Person>();
		ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
		if(country.equals("USA")) {
			name = name + "," + country;
		}
		searchCriteria.setQ(name);
		try {
			ToponymSearchResult searchResult = WebService.search(searchCriteria);
			this.location = new Location(searchResult.getToponyms().get(0).getLatitude(), searchResult.getToponyms().get(0).getLongitude());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		if(this.location == null){
			this.location = new Location(0, 0);
		}
	}
	
	public Location getLocation() {
		return location;
	}
	
	public String getName() {
		return name;
	}
	
	public String getState() {
		return state;
	}
	
	public String getCountry() {
		return country;
	}
	
	public List<Person> getAssociatedPeople() {
		return associatedPeople;
	}
	
	public void addPerson(Person name) {
		if(!associatedPeople.contains(name))
			this.associatedPeople.add(name);
	}
	
	public void addPeople(List<Person> names) {
		for(Person name : names) {
			if(!associatedPeople.contains(name))
				this.associatedPeople.add(name);
		}
	}
	
	public List<Person> getLAKmembers() {
		List<Person> result = new ArrayList<Person>();
		for(Person attendee : associatedPeople) {
			if(attendee.isAttendingLAK2011() || attendee.isAttendingLAK2012())
				result.add(attendee);
		}
		return result;
	}
	
	public List<Person> getEDMmembers() {
		List<Person> result = new ArrayList<Person>();
		for(Person attendee : associatedPeople) {
			if(attendee.isAttendingEDM2011() || attendee.isAttendingEDM2012())
				result.add(attendee);
		}
		return result;
	}
	
}
