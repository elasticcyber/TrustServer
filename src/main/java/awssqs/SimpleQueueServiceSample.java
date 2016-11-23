package awssqs;

import java.util.Map.Entry;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClient;
import com.amazonaws.services.sqs.buffered.AmazonSQSBufferedAsyncClient;
import com.amazonaws.services.sqs.model.GetQueueUrlRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;

public class SimpleQueueServiceSample {
	
	public static void main(String[] args) throws Exception {
		
		AWSCredentials credentials = null;
        try {
        	credentials = new BasicAWSCredentials("AKIAI4EBEC6FVC6YHROQ", "232eYRVzt/OZBdJX9h8Vw0oM8ui83TqgEGKsJj0a");
        } catch (Exception e) {
        	System.out.println("Exception has occured: " + e.getMessage());
        }
        
        AmazonSQSAsync  sqs = new AmazonSQSAsyncClient (credentials);
        sqs = new AmazonSQSBufferedAsyncClient(sqs);
        Region usEast2 = Region.getRegion(Regions.US_WEST_2);
        sqs.setRegion(usEast2);

        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon SQS");
        System.out.println("===========================================\n");

        try {

        	
        	// Get queue URL by queue name
        	String queueName = "AIMSimpleQ";
        	GetQueueUrlRequest getQueueUrlRequest = new GetQueueUrlRequest(queueName);
        	String myQueueUrl = sqs.getQueueUrl(getQueueUrlRequest).getQueueUrl();
	
        	
            // List queues
            System.out.println("Listing all queues in your account.\n");
            for (String queueUrl : sqs.listQueues().getQueueUrls()) {
                System.out.println("  QueueUrl: " + queueUrl);
            }
            System.out.println();
            
          //Send a message
//            System.out.println("Sending a message to " + queueName + ".\n");
//            sqs.sendMessage(new SendMessageRequest(myQueueUrl, "Eli new message"));
            
            
            
         // Receive messages
            while (true){
	            System.out.println("Receiving messages from " + queueName + ".\n");
	            ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(myQueueUrl).withMaxNumberOfMessages(100).withWaitTimeSeconds(10);  
	            sqs.receiveMessageAsync(receiveMessageRequest,new AsyncHandler<ReceiveMessageRequest, ReceiveMessageResult>() {
					
					@Override
					public void onSuccess(ReceiveMessageRequest arg0, ReceiveMessageResult arg1) {
						 for (Message message : arg1.getMessages()) {
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
						 
						//Delete a message
//				            System.out.println("Deleting a message.\n");
//				            String messageReceiptHandle = message.getReceiptHandle();
//				            sqs.deleteMessage(new DeleteMessageRequest(myQueueUrl, messageReceiptHandle));
					}
					
					@Override
					public void onError(Exception arg0) {
						System.out.println("  An Error has occured: " + arg0.getMessage());
						
					}
				});
	            			            
	            System.out.println();
	            Thread.sleep(1000);
            }

            

            
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
