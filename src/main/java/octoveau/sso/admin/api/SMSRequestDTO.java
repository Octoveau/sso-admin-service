package octoveau.sso.admin.api;

import lombok.Data;

/**
 * SMSRequestDTO
 *
 * @author yifanzheng
 */
@Data
public class SMSRequestDTO {

    private String apiKey;
    private String mobile;
    private String text;

    private static final String message = "【octoveau】您的验证码是${code}。请在1分钟之内输入，如非本人操作，请忽略本短信";

    public static SMSRequestDTO build(String mobile, String code) {
        SMSRequestDTO smsRequestDTO = new SMSRequestDTO();
        smsRequestDTO.setApiKey("e279a911806779698c61ffbe6129a86e");
        smsRequestDTO.setMobile(mobile);
        smsRequestDTO.setText(message.replace("${code}", code));

        return smsRequestDTO;
    }
}
