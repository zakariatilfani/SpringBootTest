package oracle.SpringBootTest.controller;

import oracle.SpringBootTest.*;
import oracle.SpringBootTest.service.UserService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@RestController
public class executecontroller {
	UserService liste_users = new UserService();
	Logger logger = LogManager.getLogger(executecontroller.class);

	@RequestMapping(value = "/execute", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String execute(@RequestBody String json, @RequestParam("sessionId") int id) {
		logger.info("entring execute endpoint");
		String dataObject = null;
		try {
			dataObject = JsonPath.parse(json).read("code");
		} catch (Exception e) {
			logger.error("error in json object " + e);
		}
		if (dataObject != null) {
			if (liste_users.user_index(id) == -1)
				liste_users.add_user(id);
			return "{\"result\":\"" + code_parser(dataObject, id) + "\"}";
		} else
			return "{\"result\":\"json object error\"}";
	}

	/**
	 * @param temp
	 *            is the code get from json object
	 * @param session
	 *            id user
	 * @return the result that will be produce
	 */
	String code_parser(String temp, int id) {
		logger.info("entring code parser");
		StringBuffer temp_buffer = new StringBuffer(temp.replaceAll("\\s", ""));
		if (temp.isEmpty())
			return "Empty code";
		else if (temp_buffer.toString().startsWith("%python")) {
			temp_buffer.delete(0, "%python".length());
			if (temp_buffer.toString().startsWith("print")) {
				temp_buffer.delete(0, "print".length());
				if (temp_buffer.toString().matches("^.*+.*$")) {
					String attribute_value1 = temp_buffer.substring(0, temp_buffer.indexOf("+"));
					String attribute_value2 = temp_buffer.substring(temp_buffer.indexOf("+") + 1, temp_buffer.length());
					if (attribute_value1.matches("^[a-zA-Z]*$") && attribute_value2.matches("^[0-9]*$")
							&& this.liste_users.check_user_value_existance(id, attribute_value1)) {
						attribute_value1 = this.liste_users.get_user(id).getValues(attribute_value1);
						return python_subprocess(attribute_value1, attribute_value2);
					} else if (attribute_value1.matches("^[0-9]*$") && attribute_value2.matches("^[0-9]*$")) {
						return python_subprocess(attribute_value1, attribute_value2);
					}
					return "value not found";
				}
				return "error in code syntaxe";
			} else if (temp_buffer.toString().matches("^.*=.*$")) {

				String attribute_name = temp_buffer.substring(0, temp_buffer.indexOf("="));
				String attribute_value = temp_buffer.substring(temp_buffer.indexOf("=") + 1, temp_buffer.length());
				if (attribute_name.matches("^[a-zA-Z]*$") && attribute_value.matches("^[0-9]*$")) {
					this.liste_users.add_value_to_user(id, attribute_name, attribute_value);
					return "";
				} else
					return "error in code syntaxe";
			} else
				return "error in code syntaxe";
		} else
			return "can't recognize interpretor";

	}

	/**
	 * @param att1
	 * @param att2
	 * @return result from the execution of the python subprocess
	 */
	String python_subprocess(String att1, String att2) {
		logger.info("entring to python subprocess methode");
		String result_value = "";
		try {
			String prg = "import sys\nprint(" + att1 + "+" + att2 + ")\n";
			String workingDir = System.getProperty("user.dir");
			workingDir += "\\src\\main\\resources\\";
			BufferedWriter out = new BufferedWriter(new FileWriter(workingDir + "py_sub.py"));
			out.write(prg);
			out.close();

			ProcessBuilder pb = new ProcessBuilder(workingDir + "Python\\Python37-32\\python.exe",
					workingDir + "py_sub.py");
			Process p = pb.start();
		    
			Thread.sleep(2000); 
            p.destroy(); 

			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			result_value = in.readLine();
			in.close();
		} catch (Exception e) {
			logger.error("error while execution python subprocess "+e);
		}

		if (result_value == null || result_value.length() == 0)
			result_value = "error in code";
		logger.info("value from python subprocess execution "+result_value);
		return result_value;

	}

}