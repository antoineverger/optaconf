package org.optaconf.service;

import org.optaconf.bridge.devoxx.DevoxxImporter;
import org.optaconf.cdi.RosterManager;
import org.optaconf.cdi.ScheduleManager;
import org.optaconf.domain.NurseRoster;
import org.optaconf.domain.Schedule;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("/{rosterId}/roster")
public class RosterService {

    @Inject
    private RosterManager rosterManager;

    @Inject
    private SolverFactory solverFactory;

    @Resource(name = "DefaultManagedExecutorService")
    private ManagedExecutorService executor;

    @GET // TODO should be post
    @Path("/solve")
    @Produces("application/json")
    public String solveSchedule(@PathParam("conferenceId") Long conferenceId) {
        Solver oldSolver = rosterManager.getSolver();
        if (oldSolver != null && oldSolver.isSolving()) {
            oldSolver.terminateEarly();
        }
        Solver solver = solverFactory.buildSolver();
        // TODO Use async solving https://developer.jboss.org/message/910391
//        scheduleManager.setSolver(solver);
//        executor.submit(new SolverCallable(solver, scheduleManager.getSchedule()));
//        return "Solved started.";
        solver.solve(rosterManager.getRoster());
        rosterManager.setRoster((NurseRoster) solver.getBestSolution());
        return "Solved. Sorry it took so long";
    }

    @GET
    @Path("/isSolving")
    @Produces("application/json")
    public boolean isSolving(@PathParam("conferenceId") Long conferenceId) {
        Solver solver = rosterManager.getSolver();
        return solver != null && solver.isSolving();
    }

    @GET // TODO should be post
    @Path("/terminateSolving")
    @Produces("application/json")
    public void terminateSolving(@PathParam("conferenceId") Long conferenceId) {
        Solver solver = rosterManager.getSolver();
        if (solver != null) {
            solver.terminateEarly();
        }
    }

    private class SolverCallable implements Runnable {

        private final Solver solver;
        private final NurseRoster roster;

        private SolverCallable(Solver solver, NurseRoster roster) {
            this.solver = solver;
            this.roster = roster;
        }

        public void run() {
            solver.addEventListener(new SolverEventListener() {
                @Override
                public void bestSolutionChanged(BestSolutionChangedEvent bestSolutionChangedEvent) {
                    rosterManager.setRoster((NurseRoster) bestSolutionChangedEvent.getNewBestSolution()); // TODO throws eaten Exception
                }
            });
            solver.solve(roster);
            NurseRoster bestRoster= (NurseRoster) solver.getBestSolution(); // TODO throws eaten Exception
            rosterManager.setRoster(bestRoster);
        }
    }

}
