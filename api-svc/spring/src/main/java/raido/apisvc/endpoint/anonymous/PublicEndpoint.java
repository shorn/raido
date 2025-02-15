package raido.apisvc.endpoint.anonymous;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import raido.apisvc.spring.bean.MetricRegistry;
import raido.apisvc.util.JvmUtil;
import raido.apisvc.util.Log;

import java.util.Map;

import static raido.apisvc.util.Log.to;

@RestController
public class PublicEndpoint {
  private static final Log log = to(PublicEndpoint.class);
  
  public static final String STATUS_PATH = "/public/status";
  public static final String STATISTICS_PATH = "/public/statistics";
  public static final Map<String, String> STATUS = Map.of("status", "UP");

  private MetricRegistry metricReg;


  public PublicEndpoint(MetricRegistry metricReg) {
    this.metricReg = metricReg;
  }

  /** Be careful with changes to this, it is used by the Auto-scaling group 
  health-check.
  For example, do not implement a DB connection check here - that would just
  result in the ASG cycling EC2 instances constantly, which will do nothing 
  to resolve the DB issue. */ 
  @GetMapping(STATUS_PATH)
  public Map<String, String> warmUp(){
    return STATUS;
  }

  /** pretty sure returning any data like this to caller would be a Bad Idea,
   so just send it to the logs. */ 
  @GetMapping(STATISTICS_PATH)
  public void logStats(){
    JvmUtil.logMemoryInfo(STATUS_PATH);
    metricReg.logConnectionPoolMetrics();
  }

}
