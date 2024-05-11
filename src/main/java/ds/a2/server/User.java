package ds.a2.server;

import ds.a2.rmi.RMIClient;

public class User {
    public String name;
    public RMIClient rmic;

    public User(String name, RMIClient client){
		this.name = name;
		this.rmic = client;
	}	
}
