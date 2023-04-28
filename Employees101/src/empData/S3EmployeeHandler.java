package empData;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;



public class S3EmployeeHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final String BUCKET_NAME = "javaawsbuckets101";
    private static final String OBJECT_KEY = "emp.txt";
    
    private final AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
    
    private String putObject()
    {
    	String objectContent = "This is my object content";
    	byte[] contentBytes = objectContent.getBytes(StandardCharsets.UTF_8);
    	ObjectMetadata metadata = new ObjectMetadata();
    	metadata.setContentLength(contentBytes.length);
    	
    	InputStream inputStream = new ByteArrayInputStream(contentBytes);
    	s3Client.putObject(new PutObjectRequest(BUCKET_NAME , OBJECT_KEY, inputStream, metadata));
    	
    	
    	return "Object added to s3 Bucket Successfully!!!: " + BUCKET_NAME +"/" + OBJECT_KEY;
    }
   
    
    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) 
    {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        
        
  
        String operation = input.getHttpMethod();
        String getMsg="==>>Getting Employee Data Successfully!!!";
        switch (operation) {
            case "GET":
                GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKET_NAME, OBJECT_KEY);
                response.setBody(s3Client.getObject(getObjectRequest).toString().concat(getMsg));
                response.setStatusCode(200);
                break;
            case "PUT":
                String requestBody = input.getBody();

               return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(putObject());
	
            case "DELETE":
                DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(BUCKET_NAME, OBJECT_KEY);
                s3Client.deleteObject(deleteObjectRequest);
                response.setBody("Object Deleted From Buckets Successfully!!!");
                response.setStatusCode(200);
                break;
            default:
                response.setBody("Invalid operation");
                response.setStatusCode(400);
        }
        return response;
    }
        
}














































//PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, OBJECT_KEY, requestBody);
//s3Client.putObject(putObjectRequest);
//response.setStatusCode(200);
//break;


//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//public class S3EmployeeHandler implements RequestHandler<Object, String> {
//	String processId = "";
//
//	@Override
//	public String handleRequest(Object input, Context context) {
//		ObjectMapper mapper = new ObjectMapper();
//		JSONObject inputJson;
//		try {
//			inputJson = new JSONObject(mapper.writeValueAsString(input));
//
//			processId = inputJson.getString("processId");
//
//		} catch (JSONException | JsonProcessingException e) {
//			e.printStackTrace();
//		}
//
//		return processId;
//
//	}
//
//}


//I AM ROLE:  crudRestAPIRole
