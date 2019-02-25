package oracle.SpringBootTest.controller;

import static org.junit.Assert.*;

import org.junit.Test;

public class executecontrollerTest {
    
    executecontroller executecontroller=new executecontroller();	
	@Test
	public void test_subprocess() {
		assertNotNull(executecontroller.python_subprocess("", ""));
		assertEquals("10",executecontroller.python_subprocess("5", "5"));
	}

}
