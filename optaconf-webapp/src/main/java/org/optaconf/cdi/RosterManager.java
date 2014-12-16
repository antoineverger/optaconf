package org.optaconf.cdi;

import org.optaconf.domain.NurseRoster;
import org.optaconf.domain.Schedule;
import org.optaplanner.core.api.solver.Solver;

import javax.enterprise.inject.Vetoed;
import java.io.Serializable;

@Vetoed
public class RosterManager implements Serializable {

    private NurseRoster roster;
    private Solver solver;

    public RosterManager() {
    }

    public RosterManager(NurseRoster roster) {
        this.roster = roster;
    }

    public NurseRoster getRoster() {
        return roster;
    }

    public void setRoster(NurseRoster roster) {
        this.roster = roster;
    }

    public Solver getSolver() {
        return solver;
    }

    public void setSolver(Solver solver) {
        this.solver = solver;
    }

}
