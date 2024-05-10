package ds.a2.server;

import ds.a2.rmi.RMIClient;

public class User {
    public String name;
    public RMIClient iclient;

    public User(String name, RMIClient client){
		this.name = name;
		this.iclient = client;
	}	
}
