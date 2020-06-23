package l2l.contextsharing.Controller;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.SubscribeRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController//处理http请求，返回json格式
@RequestMapping//配置url，让该类下的所有接口url都映射在/users下
public class subscribe {
    @RequestMapping(value = "/subscribe",method = RequestMethod.GET)
    public String subscribe(){
        // Subscribe an email endpoint to an Amazon SNS topic.
        AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();
        final SubscribeRequest subscribeRequest = new SubscribeRequest("arn:aws-cn:sns:cn-northwest-1:148543509440:context", "http", "http://127.0.0.1:8080/receive");
        snsClient.subscribe(subscribeRequest);

// Print the request ID for the SubscribeRequest action.
        System.out.println("SubscribeRequest: " + snsClient.getCachedResponseMetadata(subscribeRequest));
        System.out.println("To confirm the subscription, check your endpoint.");
        return snsClient.getCachedResponseMetadata(subscribeRequest).toString();
    }
}
