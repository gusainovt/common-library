package gusainov.library.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import gusainov.library.annotation.LogUserRequest;
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


    String method = logUserRequest.value();

    log.info(
        """
        
        ==================== REQUEST ====================
        
        Method: {}
        Body: {}
       
        =================================================
        """, method, prettyJson(serviceJoinPoint.getArgs()));

    Object response;

    try {
      response = serviceJoinPoint.proceed();
    } catch (Exception e) {

      log.warn(
          """
          
          ==================== ERROR ====================
          
          Method: {}
          Message: {}
         
          =================================================
          """, method, e.getMessage(), e);

      throw e;
    }

    log.info(
        """
        
        ==================== RESPONSE ===================
        
        Method: {}
        Body: {}
        
        =================================================
        """, method, prettyJson(response));

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
