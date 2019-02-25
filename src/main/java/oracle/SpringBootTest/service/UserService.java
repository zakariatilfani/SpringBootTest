package oracle.SpringBootTest.service;
import oracle.SpringBootTest.*;
import oracle.SpringBootTest.entitie.User;

import java.util.ArrayList;
import java.util.List;

public class UserService {
List<User> list_users;

public UserService() {
	list_users=new ArrayList<>();
	}

/**
 * @param id
 * @return the index of a user id
 */
public int user_index(int id)
{
	if(list_users.size()==0)
		return -1;
	else {
	int i=0;
	while(i<this.list_users.size())
	{
		if(list_users.get(i).getSessionId()==id)
			return i;
		i++;
	}
	return -1;
	}
}

public void add_user(int id)
{
	User temp=new User(id);
	this.list_users.add(temp);
}

public User get_user(int id) {
	int user_loc;
	user_loc=this.user_index(id);
	if(user_loc!=-1)
		return this.list_users.get(user_loc);
	    return null;
}

public void setList_users(List<User> list_users) {
	this.list_users = list_users;
}

public boolean check_user_value_existance(int id,String attribut_name) {
	int user_loc;
	user_loc=this.user_index(id);
	if(user_loc!=-1 && this.list_users.get(user_loc).getValues(attribut_name)!=null)
	return true;
	return false;
	
}

public void add_value_to_user(int id,String attribut_name,String attribut_value)
{
	int user_loc;
	user_loc=this.user_index(id);
	if(user_loc!=-1)
	this.list_users.get(user_loc).addvalue(attribut_name,attribut_value );
	else System.out.println("this user doesn't exist");
}



}

