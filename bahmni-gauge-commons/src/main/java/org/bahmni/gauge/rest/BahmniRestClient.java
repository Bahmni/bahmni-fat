package org.bahmni.gauge.rest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.bahmni.gauge.common.program.domain.Program;
import org.bahmni.gauge.common.registration.domain.Patient;
import org.json.JSONObject;

import javax.net.ssl.SSLContext;
import java.io.StringWriter;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

public class BahmniRestClient {

	private static BahmniRestClient bahmniRestClient;

	private static final String PATIENT_PROFILE_URL = "/openmrs/ws/rest/v1/bahmnicore/patientprofile";

    private static final String PATIENT_URL = "/openmrs/ws/rest/v1/patient/";

	private static final String PROGRAM_ENROLLMENT_URL = "/openmrs/ws/rest/v1/bahmniprogramenrollment";

	private Configuration freemarkerConfiguration;

	private String url;

	private String username;

	private String password;

	private BahmniRestClient(){
		setFreemarkerConfig();
		setupUnirest();
	}

	private void setupUnirest() {
		try {
			SSLContext  sslcontext = SSLContexts.custom()
					.loadTrustMaterial(null, new TrustStrategy() {
						public boolean isTrusted(X509Certificate[] certificate, String authType) {
							return true;
						}
					})
					.build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext);
			CloseableHttpClient httpclient = HttpClients.custom()
					.setSSLSocketFactory(sslsf)
					.build();
			Unirest.setHttpClient(httpclient);

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static BahmniRestClient get(){
		if(bahmniRestClient == null){
			bahmniRestClient = new BahmniRestClient();
			bahmniRestClient.url = System.getenv("BAHMNI_URL");
			bahmniRestClient.username = System.getenv("BAHMNI_USER");
			bahmniRestClient.password = System.getenv("BAHMNI_PASSWORD");
		}

		return bahmniRestClient;
	}

	private void setFreemarkerConfig() {
		try{
			freemarkerConfiguration = new Configuration(Configuration.VERSION_2_3_22);
			freemarkerConfiguration.setTemplateLoader(new ClassTemplateLoader(BahmniRestClient.class,"/org/bahmni/gauge/rest"));
			freemarkerConfiguration.setDefaultEncoding("UTF-8");
			freemarkerConfiguration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		}catch(Exception ex){
			throw new IllegalArgumentException("The freemarker directory location configured is not ");
		}
	}

	public void createPatient(Patient patient){
		try {
			Template freemarkerTemplate = freemarkerConfiguration.getTemplate("patient_create.ftl");
			Map<String,Object> patientData = new HashMap<>();
			patientData.put("patient",patient);

			StringWriter stringWriter = new StringWriter();
			freemarkerTemplate.process(patientData,stringWriter);
			String requestJson = stringWriter.toString();

            HttpResponse<JsonNode> response = Unirest.post(url + PATIENT_PROFILE_URL)
					.basicAuth(username,password)
					.header("content-type", "application/json")
					.body(requestJson)
					.asJson();

			if(response.getBody() != null && response.getBody().getObject() != null &&
					response.getBody().getObject().get("patient") != null &&
					((JSONObject)response.getBody().getObject().get("patient")).get("uuid")!=null){
				patient.setUuid((String)((JSONObject)(response.getBody().getObject().get("patient"))).get("uuid"));
			}else{
				System.err.println("Response from the server for patient creation:");
				System.err.println(response.getBody().toString());
				throw new BahmniAPIException("Patient creation failed!!");
			}
		}
		catch (Exception e) {
			throw new BahmniAPIException(e);
		}
	}

	public void retirePatient(String uuid) {
		try {
			HttpResponse<String> retirePatient = Unirest.delete(url + PATIENT_URL + uuid)
					.basicAuth(username, password)
					.header("content-type", "application/json")
					.asString();
		}
		catch (Exception e) {
			throw new BahmniAPIException(e);
		}
	}

	public void enrollToProgram(Patient patient, Program program){
		try {
			Template freemarkerTemplate = freemarkerConfiguration.getTemplate("program_enrollment.ftl");
			Map<String,Object> programData = new HashMap<>();
			//program.setPatientUuid(patient.getUuid());
			programData.put("patient",patient);
			programData.put("program",program);

			StringWriter stringWriter = new StringWriter();
			freemarkerTemplate.process(programData,stringWriter);
			String requestJson = stringWriter.toString();

			HttpResponse<JsonNode> response = Unirest.post(url + PROGRAM_ENROLLMENT_URL)
					.basicAuth(username,password)
					.header("content-type", "application/json")
					.body(requestJson)
					.asJson();

			if(response.getBody() != null && response.getBody().getObject() != null &&
					response.getBody().getObject().get("patient") != null &&
					((JSONObject)response.getBody().getObject().get("patient")).get("uuid")!=null){
				program.setUuid((String)((JSONObject)(response.getBody().getObject().get("patient"))).get("uuid"));
			}else{
				System.err.println("Response from the server for program enrollment:");
				System.err.println(response.getBody().toString());
				throw new BahmniAPIException("Program enrollment failed!!");
			}
		}
		catch (Exception e) {
			throw new BahmniAPIException(e);
		}
	}
}
