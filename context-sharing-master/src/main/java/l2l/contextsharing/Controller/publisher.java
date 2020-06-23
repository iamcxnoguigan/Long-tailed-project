package l2l.contextsharing.Controller;


import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController//处理http请求，返回json格式
@RequestMapping(value = "/publisher")//配置url，让该类下的所有接口url都映射在/users下
public class publisher {
    @RequestMapping(value = "/publish", method = RequestMethod.GET)
    public String say() {
        // Publish a message to an Amazon SNS topic.

        String msg = "If you receive this message, publishing a message to an Amazon SNS topic works.";
        PublishRequest publishRequest = new PublishRequest("arn:aws-cn:sns:cn-northwest-1:148543509440:context", msg);
        AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();
        PublishResult publishResponse = snsClient.publish(publishRequest);

// Print the MessageId of the message.
        System.out.println("MessageId: " + publishResponse.getMessageId());
        return "publish";
    }
}
