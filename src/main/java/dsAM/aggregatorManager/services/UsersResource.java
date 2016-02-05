 package dsAM.aggregatorManager.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dsAM.aggregatorManager.db.UserDAO;

@Path("/users")
public class UsersResource {
	
	private static final int SUCCESSFUL_LOGIN = 0;
	private static final int SECOND_TIME_LOGIN = 1;
	
	@PUT
	@Consumes(MediaType.TEXT_PLAIN)
	public Response MobileRegister(String request) {
		System.out.println(request);
		String[] details=request.split(",");
		String username=details[0];
		String password=details[1];
		UserDAO dao = new UserDAO();
		dao.addUser(username, password); 
		return Response.ok(MediaType.TEXT_PLAIN).build();
		//return Response.status(400).build();
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