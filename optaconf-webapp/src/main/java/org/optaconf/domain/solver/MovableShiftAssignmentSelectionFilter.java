/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaconf.domain.solver;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionFilter;
import org.optaplanner.core.impl.score.director.ScoreDirector;
import org.optaconf.domain.NurseRoster;
import org.optaconf.domain.ShiftAssignment;
import org.optaconf.domain.ShiftDate;

public class MovableShiftAssignmentSelectionFilter implements SelectionFilter<ShiftAssignment> {

    public boolean accept(ScoreDirector scoreDirector, ShiftAssignment shiftAssignment) {
        NurseRoster nurseRoster = (NurseRoster) scoreDirector.getWorkingSolution();
        return accept(nurseRoster, shiftAssignment);
    }

    public boolean accept(NurseRoster nurseRoster, ShiftAssignment shiftAssignment) {
        ShiftDate shiftDate = shiftAssignment.getShift().getShiftDate();
        return nurseRoster.getNurseRosterParametrization().isInPlanningWindow(shiftDate);
    }

}
