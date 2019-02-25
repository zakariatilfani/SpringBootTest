package oracle.SpringBootTest.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import junit.framework.TestCase;
import oracle.SpringBootTest.entitie.User;

public class UserServiceTest extends TestCase {
	
	UserService userservice=new UserService();
	
	@Test
	public void test_user_index()throws Exception
	{
		userservice.add_user(1);
		userservice.add_user(3);
		userservice.add_user(88);
		
		assertNotNull(userservice.user_index(4));
		assertEquals(1, userservice.user_index(3));
	}
	
	

}
