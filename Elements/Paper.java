package Elements;

import java.util.List;

public class Paper 
{

	private String title;
	private List<Person> makers;
	private List<String> subjects;
	private boolean atLAK2011;
	private boolean atLAK2012;
	private boolean atEDM2011;
	private boolean atEDM2012;

	public Paper(String title, List<Person> makers, List<String> subjects) 
	{
		this.title = title;
		this.makers = makers;
		this.subjects = subjects;
		atLAK2012 = false;
		atEDM2011 = false;
		atEDM2012 = false;
	}
	
	public void setAsForConference(String conference) {
		if(conference.equals("LAK_2011"))
			atLAK2011=true;
		else if(conference.equals("LAK_2012"))
			atLAK2012=true;
		else if(conference.equals("EDM_2011"))
			atEDM2011=true;
		else if(conference.equals("EDM_2012"))
			atEDM2012=true;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public List<Person> getMakers()
	{
		return makers;
	}
	
	public List<String> getSubjects()
	{
		return subjects;
	}
	
	public boolean wasForLAK2011() {
		return atLAK2011;
	}
	
	public boolean wasForLAK2012() {
		return atLAK2012;
	}
	
	public boolean wasForEDM2011() {
		return atEDM2011;
	}
	
	public boolean wasForEDM2012() {
		return atEDM2012;
	}

}
