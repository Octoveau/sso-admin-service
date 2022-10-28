package octoveau.sso.admin.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.NativeWebRequest;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * <p>
 * 将服务器端异常处理为客户端友好的 JSON 格式。
 */
@ControllerAdvice
public class GlobalExceptionHandler implements ProblemHandling {

    @Override
    public ResponseEntity<Problem> process(@Nullable ResponseEntity<Problem> entity, NativeWebRequest request) {
        if (entity == null || entity.getBody() == null) {
            return entity;
        }
        Problem problem = entity.getBody();

        ProblemBuilder builder = Problem.builder()
                .with("data", "")
                .with("code", Objects.isNull(problem.getStatus()) ? "" : problem.getStatus().getStatusCode())
                .with("message", StringUtils.isEmpty(problem.getDetail()) ? "" : problem.getDetail());
        return new ResponseEntity<>(builder.build(), entity.getHeaders(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Problem> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nonnull NativeWebRequest request) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors().stream()
                .map(f -> new FieldError(f.getObjectName(), f.getField(), f.getCode()))
                .collect(Collectors.toList());

        Problem problem = Problem.builder()
                .withStatus(Status.BAD_REQUEST)
                .withDetail(fieldErrors.toString())
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Problem> handleUsernameNotFoundException(UsernameNotFoundException ex, NativeWebRequest request) {
        Problem problem = Problem.builder()
                .withStatus(Status.NOT_FOUND)
                .withDetail(ex.getMessage())
                .build();
        return create(ex, problem, request);
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    public ResponseEntity<Problem> restTemplateException(
            HttpStatusCodeException ex, NativeWebRequest request) {
        // 尝试获取原始异常信息，通常这只对本项目的其它组件有效
        String responseBody = ex.getResponseBodyAsString();
        Problem problem = Problem.builder()
                .withStatus(Status.valueOf(ex.getStatusCode().name()))
                .withDetail(responseBody)
                .build();
        return create(ex, problem, request);
    }
}