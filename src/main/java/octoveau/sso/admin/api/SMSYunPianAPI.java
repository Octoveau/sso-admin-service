package octoveau.sso.admin.api;

import octoveau.sso.admin.util.ThreadUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * SMSYunPianAPI
 * <p>
 * 云片短信第三方服务
 *
 * @author yifanzheng
 */
@Service
public class SMSYunPianAPI {

    private static final Logger log = LoggerFactory.getLogger(SMSYunPianAPI.class);

    @Value("${yunpian.sms.url}")
    private String smsUrl;

    @Autowired
    private RestTemplate restTemplate;

    public void sendShortMessage(SMSRequestDTO smsRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 构建消息参数
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("apikey", smsRequest.getApiKey());
        params.add("mobile", smsRequest.getMobile());
        params.add("text", smsRequest.getText());
        // 构建HttpEntity
        HttpEntity<MultiValueMap> httpEntity = new HttpEntity<>(params, httpHeaders);
        for (int times = 1; ; times++) {
            try {
                restTemplate.exchange(smsUrl, HttpMethod.POST, httpEntity, String.class);
            } catch (RestClientException e) {
                if (times <= 3) {
                    log.warn("Call rest api error, and attempt={}/{} after 1 seconds, message: {}", times, 3, e.getMessage());
                    ThreadUtil.sleep(1000);
                    continue;
                }
                throw e;
            }
        }
    }
}
