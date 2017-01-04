package next.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class PerformanceAspect {
	private static final Logger log = LoggerFactory.getLogger(PerformanceAspect.class);
	
	@Around("within(next.service..*) || within(next.dao..*)")
	public Object performance(ProceedingJoinPoint pjp) throws Throwable {
		StopWatch watch = new StopWatch();
		watch.start();
		Object returnValue = pjp.proceed();
		watch.stop();
		log.debug("method : {}, execution time : {}", pjp.toShortString(), watch.getTotalTimeMillis());
		return returnValue;
	}
}
