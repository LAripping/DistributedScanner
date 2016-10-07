 package dsAM.aggregatorManager.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dsAM.aggregatorManager.Main;
import dsAM.aggregatorManager.db.UserDAO;
import dsAM.aggregatorManager.gui.androidRegistrationRequest;

@Path("/users")
public class UsersResource {
	
	private static final int SUCCESSFUL_LOGIN = 0;
	private static final int SECOND_TIME_LOGIN = 1;
	private static final boolean USER_DOES_NOT_EXIST = false;
    private static final boolean USER_ALREADY_EXISTS = true;

@PUT
@Consumes(MediaType.TEXT_PLAIN)
public Response MobileRegister(String request) {
	System.out.println(request);
	String[] details=request.split(",");
	String username=details[0];
	String password=details[1];
	UserDAO dao = new UserDAO();
	androidRegistrationRequest req=null;
            
        //first check if the registrant already exists in the database
	boolean answer = dao.userExists(username);
        
	if(answer == USER_ALREADY_EXISTS)
    {
		return Response.status(406).build(); 
    }
    else if(answer == USER_DOES_NOT_EXIST)
    {
    	//Now let the manager decide whether he wants to accept the android client or not
	    req = new androidRegistrationRequest(username , password , Thread.currentThread());
	    synchronized(Thread.currentThread()){
			try {                                                   //pause the current flow until the manager decides
				req.show_gui();
				req.setVisible(true);
				Thread.currentThread().wait();
			} catch (InterruptedException e) {
		        System.err.println("Thread exception while showing android registration gui");
				e.printStackTrace();
			}
	    }
	    
    }
	if(req.isAccepted())                                        //get manager's decision
    {
    	 if(Main.v){
    		 System.out.println("Android client with username : "+ username + " was succesfully registered");
    	 }
    	 return Response.ok(MediaType.TEXT_PLAIN).build();
    }
    else
    {									// If registration is rejected 
    	return Response.status(406).build();                            // return a "406-Not Acceptable" status
    }
}
	
	
	@POST
	@Path("/login")
	@Consumes(MediaType.TEXT_PLAIN)
	public String UserLogin(String data){
		System.out.println(data);
		String[] details=data.split(",");
		String username=details[0];
		String password=details[1];
		UserDAO dao= new UserDAO();
		dao.CheckAdmin(username, password);
		int loginStatus = dao.getStatus();
		if(loginStatus == SUCCESSFUL_LOGIN)
    	{
			return "ok";
    	}
    	else if(loginStatus == SECOND_TIME_LOGIN)
    	{
    		return "second time";
    	}
    	else{
    		return "error";
    	}
		
	}
	
	@POST
	@Path("/logout")
	@Consumes(MediaType.TEXT_PLAIN)
	public void UserLogout(String data){
		System.out.println(data);
		String[] details=data.split(",");
		String username=details[0];
		String password=details[1];
		UserDAO dao= new UserDAO();
		dao.userLogout(username, password);
	}
	
}