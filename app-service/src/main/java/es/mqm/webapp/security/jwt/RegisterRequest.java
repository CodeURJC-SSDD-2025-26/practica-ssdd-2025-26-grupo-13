package es.mqm.webapp.security.jwt;

import es.mqm.webapp.model.Location;

public class RegisterRequest {
    private String name;
    private String surnames;
    private String username; //email
	private String password;
    private Location location;

    public RegisterRequest(){

    }
    public RegisterRequest(String name, String surnames, String username, String password, Location location) {
		this.name = name;
        this.surnames = surnames;
        this.username = username;
		this.password = password;
        this.location = location;
	}

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getSurnames(){
        return surnames;
    }

    public void setSurnames(String surnames){
        this.surnames = surnames;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    public Location getLocation(){
        return location;
    }

    public void setLocation(Location location){
        this.location = location;
    }

	@Override
	public String toString() {
		return "RegisterRequest [name=" + name + ", surnames=" + surnames +", username=" + username + ", password=" + password + ", location=" + location +"]";
	}
}


