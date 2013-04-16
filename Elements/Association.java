package Elements;

import java.util.ArrayList;
import java.util.List;

public class Association {
	
	private String name;
	private City city;
	private List<Person> associatedPeople;
	
	public Association(String name, City city) {
		this.name = name;
		this.city = city;
		associatedPeople = new ArrayList<Person>();
	}
	
	public void addPerson(Person name) {
		if(!associatedPeople.contains(name))
			this.associatedPeople.add(name);
		this.city.addPerson(name);
	}
	
	public void addPeople(List<Person> names) {
		for(Person name : names) {
			if(!associatedPeople.contains(name)) {
				this.associatedPeople.add(name);
			}
		}
		this.city.addPeople(names);
	}

	public boolean isAttendingAConference() {
		for(Person associatedPerson : associatedPeople)
			if(associatedPerson.isAttendingAConference())
				return true;
		return false;
	}
	
	public boolean isAttendingLAK2011() {
		for(Person associatedPerson : associatedPeople) {
			if(associatedPerson.isAttendingLAK2011())
				return true;
		}
		return false;
	}
	
	public boolean isAttendingLAK2012() {
		for(Person associatedPerson : associatedPeople) {
			if(associatedPerson.isAttendingLAK2012())
				return true;
		}
		return false;
	}
	
	public boolean isAttendingEDM2011() {
		for(Person associatedPerson : associatedPeople) {
			if(associatedPerson.isAttendingEDM2011())
				return true;
		}
		return false;
	}
	
	public boolean isAttendingEDM2012() {
		for(Person associatedPerson : associatedPeople) {
			if(associatedPerson.isAttendingEDM2012())
				return true;
		}
		return false;
	}
	
	public String getName() {
		return name;
	}
	
	public City getCity() {
		return city;
	}
}
