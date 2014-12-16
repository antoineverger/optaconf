package org.optaconf.service;

import org.optaconf.cdi.RosterManager;
import org.optaconf.cdi.ScheduleManager;
import org.optaconf.domain.Employee;
import org.optaconf.domain.NurseRoster;
import org.optaconf.domain.Schedule;
import org.optaconf.domain.speaker.Speaker;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/{rosterId}/employee")
public class EmployeeService {

    @Inject
    private RosterManager rosterManager;

    @GET
    @Path("/")
    @Produces("application/json")
    public List<Employee> getEmployeeList(@PathParam("rosterId") Long rosterId) {
        NurseRoster roster = rosterManager.getRoster();
        return roster.getEmployeeList();
    }

}
