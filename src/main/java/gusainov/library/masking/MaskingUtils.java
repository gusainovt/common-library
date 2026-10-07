package gusainov.library.masking;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import lombok.experimental.UtilityClass;

/**
 * Маскирует значения по регулярному выражению.
 *
 * <p>
 * Правило маскирования: все capturing groups регулярного выражения
 * {@code (...)} заменяются символами {@code *}. Само совпадение
 * целиком не маскируется.
 * </p>
 *
 * <p>
 * Пример:
 *
 * <pre>
 * body:
 * {"login":"timur","password":"123456"}
 *
 * pattern:
 * "password"\\s*:\\s*"([^"]*)"
 *
 * result:
 * {"login":"timur","password":"******"}
 * </pre>
 */
@UtilityClass
public final class MaskingUtils {

  /**
   * Маскирует значения, найденные по переданному регулярному выражению.
   *
   * <p>Маскированию подвергаются все capturing groups
   * регулярного выражения, начиная с первой группы.
   * Само совпадение регулярного выражения целиком не маскируется.</p>
   *
   * <p>Например, для JSON:
   * {@code "password": "123456"}
   *
   * <p>И паттерна:
   * {@code "password"\\s*:\\s*"([^"]*)"}
   *
   * <p>Результат будет:
   * {@code "password": "******"}
   * </p>
   *
   * @param body текст, в котором необходимо выполнить маскирование
   * @param patterns массив регулярных выражений с группами маскируемых значений
   * @return текст с замаскированными значениями
   */
  public static String maskByPattern(String body, String[] patterns) {
    if (patterns == null || patterns.length == 0) {
      return body;
    }

    StringBuilder sb = new StringBuilder(body);

    for (String pattern : patterns) {
      Matcher matcher = Pattern.compile(pattern).matcher(sb);
      matcherFind(matcher, sb);
    }

    return sb.toString();
  }

  /**
   * Обходит все совпадения регулярного выражения и заменяет содержимое
   * всех найденных capturing groups символами {@code *}.
   *
   * @param matcher matcher, содержащий найденные совпадения регулярного выражения
   * @param sb буфер строки, в котором выполняется маскирование
   */
  private static void matcherFind(Matcher matcher, StringBuilder sb) {
    while (matcher.find()) {
      IntStream.rangeClosed(1, matcher.groupCount())
          .forEach(group -> {
            if (matcher.group(group) != null) {
              IntStream.range(
                  matcher.start(group),
                  matcher.end(group)
              ).forEach(i -> sb.setCharAt(i, '*'));
            }
          });
    }
  }
}
