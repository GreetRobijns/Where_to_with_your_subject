package Elements;

public class Person {
	
	private String name;
	private Association association;
	private boolean attendingLAK2011;
	private boolean attendingLAK2012;
	private boolean attendingEDM2011;
	private boolean attendingEDM2012;

	public Person(String name, Association association) {
		this.name = name;
		attendingLAK2011 = false;
		attendingLAK2012 = false;
		attendingEDM2011 = false;
		attendingEDM2012 = false;
		this.association = association; //TODO GREET ZIEN OF HET NIET ANDERS KAN
	}

	public String getName() {
		return name;
	}
	
	public Association getAssociation()
	{
		return association;
	}
	
	public void setAsAttending(String conference) {
		if(conference.equals("LAK_2011"))
			attendingLAK2011=true;
		else if(conference.equals("LAK_2012"))
			attendingLAK2012=true;
		else if(conference.equals("EDM_2011"))
			attendingEDM2011=true;
		else if(conference.equals("EDM_2012"))
			attendingEDM2012=true;
	}
	
	public boolean isAttendingLAK2011() {
		return attendingLAK2011;
	}
	
	public boolean isAttendingLAK2012() {
		return attendingLAK2012;
	}
	
	public boolean isAttendingEDM2011() {
		return attendingEDM2011;
	}
	
	public boolean isAttendingEDM2012() {
		return attendingEDM2012;
	}
	
	public boolean isAttendingAConference() {
		if(isAttendingLAK2011() || isAttendingLAK2012())
			return true;
		if(isAttendingEDM2011() || isAttendingEDM2012())
			return true;
		return false;
	}
}
