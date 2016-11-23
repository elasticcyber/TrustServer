package awssqs;

import java.util.List;
import java.util.Map.Entry;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class SimpleQueueServiceSample {
	
	public static void main(String[] args) throws Exception {
		
		AWSCredentials credentials = null;
        try {
        	credentials = new BasicAWSCredentials("AKIAI4EBEC6FVC6YHROQ", "232eYRVzt/OZBdJX9h8Vw0oM8ui83TqgEGKsJj0a");
            //credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                    "Please make sure that your credentials file is at the correct " +
                    "location (~/.aws/credentials), and is in valid format.",
                    e);
        }
        
        AmazonSQS sqs = new AmazonSQSClient(credentials);
        Region usEast2 = Region.getRegion(Regions.EU_WEST_1);
        sqs.setRegion(usEast2);

        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon SQS");
        System.out.println("===========================================\n");
        
        try {
            // Create a queue
//            System.out.println("Creating a new SQS queue called MyQueue.\n");
//            CreateQueueRequest createQueueRequest = new CreateQueueRequest("MyQueue");
//            String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
        	
        	// Get queue URL by queue name
        	String queueName = "EliTesting";
        	GetQueueUrlRequest getQueueUrlRequest = new GetQueueUrlRequest(queueName);
        	String myQueueUrl = sqs.getQueueUrl(getQueueUrlRequest).getQueueUrl();
        	        	
            // List queues
            System.out.println("Listing all queues in your account.\n");
            for (String queueUrl : sqs.listQueues().getQueueUrls()) {
                System.out.println("  QueueUrl: " + queueUrl);
            }
            System.out.println();
            
         // Send a message
//            System.out.println("Sending a message to " + queueName + ".\n");
//            sqs.sendMessage(new SendMessageRequest(myQueueUrl, "5eaae023-8d31-4449-bb56-2bae34544ad2"));
            
         // Receive messages
            System.out.println("Receiving messages from " + queueName + ".\n");
            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl);
            List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
            for (Message message : messages) {
                System.out.println("  Message");
                System.out.println("    MessageId:     " + message.getMessageId());
                System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
                System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
                System.out.println("    Body:          " + message.getBody());
                for (Entry<String, String> entry : message.getAttributes().entrySet()) {
                    System.out.println("  Attribute");
                    System.out.println("    Name:  " + entry.getKey());
                    System.out.println("    Value: " + entry.getValue());
                }
            }
            
            System.out.println();

            // Delete a message
//            System.out.println("Deleting a message.\n");
//            String messageReceiptHandle = messages.get(0).getReceiptHandle();
//            sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageReceiptHandle));

            // Delete a queue
//            System.out.println("Deleting the test queue.\n");
//            sqs.deleteQueue(new DeleteQueueRequest(myQueueUrl));
            
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it " +
                    "to Amazon SQS, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered " +
                    "a serious internal problem while trying to communicate with SQS, such as not " +
                    "being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }


		
	}

}
