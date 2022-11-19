package octoveau.sso.admin.web.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import octoveau.sso.admin.dto.ResponseDTO;
import octoveau.sso.admin.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SMSResource
 *
 * @author yifanzheng
 */
@RestController
@RequestMapping("/api/sms")
@Api(tags = "SMS Resource")
public class SMSResource {

    @Autowired
    private SMSService smsService;

    @PostMapping("/send")
    @ApiOperation(value = "发送短信获取验证码")
    public ResponseDTO<Void> sendSMS(@RequestParam("phone") String phone) {
        smsService.sendSMSAndCacheCode(phone);
        return ResponseDTO.ok();
    }
}
