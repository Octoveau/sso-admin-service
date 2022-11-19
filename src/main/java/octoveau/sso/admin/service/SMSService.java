package octoveau.sso.admin.service;

import octoveau.sso.admin.api.SMSRequestDTO;
import octoveau.sso.admin.api.SMSYunPianAPI;
import octoveau.sso.admin.storage.SMSCodeStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 短信服务
 *
 * @author yifanzheng
 */
@Service
public class SMSService {

    private static final SMSCodeStorage smsCodeStorage = new SMSCodeStorage();

    @Autowired
    private SMSYunPianAPI smsYunPianAPI;

    /**
     * 发送短信
     *
     * @param phone 手机号
     */
    public void sendSMSAndCacheCode(String phone) {
        int codeNum = (int) Math.floor(Math.random() * (9999 - 1000)) + 1000;
        SMSRequestDTO smsRequestDTO = SMSRequestDTO.build(phone, String.valueOf(codeNum));
        smsYunPianAPI.sendShortMessage(smsRequestDTO);
        // 缓存code
        smsCodeStorage.cacheCode(phone, String.valueOf(codeNum));
    }

    public String getCodeCache(String phone) {
        return smsCodeStorage.getCache(phone);
    }
}
