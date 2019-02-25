package oracle.SpringBootTest.entitie;

import java.util.HashMap;

public class User {
	private int SessionId;
	private HashMap<String,String> values;
	
	public User(int sessionId) {
		SessionId = sessionId;
		this.values = new HashMap<>();
	}
	
	public int getSessionId() {
		return SessionId;
	}
	public void setSessionId(int sessionId) {
		SessionId = sessionId;
	}
	public String getValues(String attibut_name) {
		return this.values.get(attibut_name);
	}
	public void setValues(HashMap<String, String> values) {
		this.values = values;
	}
	
	public void addvalue(String attribut_name,String attribut_value)
	{
		this.values.put(attribut_name, attribut_value);
	}

	
	

}
