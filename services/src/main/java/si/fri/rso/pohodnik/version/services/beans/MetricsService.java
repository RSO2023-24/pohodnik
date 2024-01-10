package si.fri.rso.pohodnik.version.services.beans;

import javax.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Gauge;

@ApplicationScoped
public class MetricsService {

    private int activeUsers = 0;
    private int registeredUsers = 0;

    @Counted(name = "incrementActiveUsers", absolute = true, description = "Number of active users increment operations")
    public void incrementActiveUsers() {
        activeUsers++;
    }

    @Gauge(name = "usersActive", absolute = true, unit = "none", description = "Number of active users")
    public int getActiveUsers() {
        return activeUsers;
    }

    @Gauge(name = "usersRegistered", absolute = true, unit = "none", description = "Number of registered users")
    public int getRegisteredUsers() {
        return registeredUsers;
    }

    @Counted(name = "incrementRegisteredUsers", absolute = true, description = "Number of registered users increment operations")
    public void incrementRegisteredUsers() {
        registeredUsers++;
    }
}