package gusainov.library.aspect;

import static gusainov.library.masking.MaskingUtils.maskByPattern;
import com.fasterxml.jackson.databind.ObjectMapper;
import gusainov.library.annotation.LogUserRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 *  Аспект, отвечающий за логирование входящих запросов.
 *  Применяется к методам с аннотацией {@link gusainov.library.annotation.LogUserRequest}
 */
@Aspect
@Slf4j
@RequiredArgsConstructor
public class LogUserRequestAspect {

  private final ObjectMapper objectMapper;

  /**
   * Перехватывает выполнение методов, помеченных аннотацией {@link LogUserRequest},
   * и выполняет логирование входящего запроса, полученного ответа или возникшей ошибки.
   *
   * <p>Аспект работает следующим образом:
   * <ul>
   *   <li>Логирует информацию о запросе (имя метода и тело запроса) до вызова целевого метода.</li>
   *   <li>Выполняет целевой метод.</li>
   *   <li>В случае успешного выполнения логирует полученный ответ.</li>
   *   <li>В случае возникновения исключения логирует сообщение об ошибке и пробрасывает исключение дальше.</li>
   * </ul>
   */
  @Around("@annotation(logUserRequest)")
  public Object logUserRequest(ProceedingJoinPoint serviceJoinPoint,
      LogUserRequest logUserRequest) throws Throwable{

    String requestId = UUID.randomUUID().toString();

    String method = logUserRequest.method();

    String[] maskPatterns = logUserRequest.maskPatterns();

    String bodyRequest = maskByPattern(prettyJson(serviceJoinPoint.getArgs()), maskPatterns);

    log.info(
        """
        
        ==================== REQUEST ====================
        
        requestId: {}
        Method: {}
        Body: {}
       
        =================================================
        """,
        requestId,
        method,
        bodyRequest);

    Object response;

    try {
      response = serviceJoinPoint.proceed();
    } catch (Exception e) {

      log.warn(
          """
          
          ==================== ERROR ====================
          
          requestId: {}
          Method: {}
          Message: {}
         
          ===============================================
          """,
          requestId,
          method,
          e.getMessage(), e);

      throw e;
    }

    String bodyResponse = maskByPattern(prettyJson(response), maskPatterns);


    log.info(
        """
        
        ==================== RESPONSE ===================
        
        requestId: {}
        Method: {}
        Body: {}
        
        =================================================
        """,
        requestId,
        method,
        bodyResponse);

    return response;
  }

  private String prettyJson(Object o) {
    try {
      return objectMapper.writerWithDefaultPrettyPrinter()
          .writeValueAsString(o);
    } catch (Exception e) {
      return String.valueOf(o);
    }
  }
}
