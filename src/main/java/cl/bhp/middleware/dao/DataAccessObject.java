package cl.bhp.middleware.dao;

import cl.bhp.middleware.exception.ServiceException;
import cl.bhp.middleware.util.PropertiesUtil;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Clase que permite crear un reclamo en EspoCRM por la API Expuesta
 * @author Luis Oliveros
 */

public class DataAccessObject {
	static PropertiesUtil prop = new PropertiesUtil();
	
	private static final Logger LOGGER = Logger.getLogger(DataAccessObject.class);
	
	/**
	 * Genera reclamo de un empleado
	 * recibe los datos por parametro
	 * @param datos
	 * @return  
	 * @throws ServiceException
	 */
	
	public JSONObject generarReclamo (JSONObject datos) throws ServiceException {
		long init = System.currentTimeMillis();
		JSONObject Json = new JSONObject();
		String URI = prop.getLocalProperties().getProperty("api.espocrm.uri");
		String auth = prop.getLocalProperties().getProperty("api.espocrm.auth");
		
		try {
			
			HttpResponse<String> responseUser = Unirest.post(URI)
					  .header("Authorization", "Basic "+auth)
					  .header("cache-control", "no-cache")
					  .header("Content-Type", "application/json")
					  .body(datos)
					  .asString();
					 		
			LOGGER.info("Response EspoCRM Status:"+responseUser.getStatus()+" Message: "+responseUser.getStatusText());
			Json = new JSONObject(responseUser.getBody());

			} catch (UnirestException e) {
				e.printStackTrace();
				throw new ServiceException("456");
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServiceException("456");
			}
		
		LOGGER.info("Tiempo en consulta EspoCRM "+(System.currentTimeMillis() - init)+" ms.");
		return Json;	
	}
}
