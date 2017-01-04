package next.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAsepct {
	private static final Logger log = LoggerFactory.getLogger(LoggingAsepct.class);

	@Before("within(next.dao..*)")
	public void logMethodParameters(JoinPoint jp) {
		Object[] args = jp.getArgs();
		for (Object arg : args) {
			log.debug("method : {}, argument : {}", jp.toShortString(), arg);
		}
	}
}
