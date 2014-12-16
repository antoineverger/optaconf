package org.optaconf.cdi;

import org.optaconf.domain.*;
import org.optaconf.domain.contract.Contract;
import org.optaconf.domain.contract.ContractLine;
import org.optaconf.domain.contract.PatternContractLine;
import org.optaconf.domain.pattern.FreeBefore2DaysWithAWorkDayPattern;
import org.optaconf.domain.pattern.Pattern;
import org.optaconf.domain.pattern.ShiftType2DaysPattern;
import org.optaconf.domain.pattern.ShiftType3DaysPattern;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DummyRosterProducer implements Serializable {
    @Produces
    @SessionScoped
    public RosterManager createDummyRoster() {
        NurseRoster roster = new NurseRoster();

        List<ShiftType> shiftTypeList = new ArrayList<ShiftType>();
        ShiftType early = new ShiftType(1L, "EARLY", "06:30:00", "14:30:00", false, "Early shift");
        shiftTypeList.add(early);
        ShiftType late = new ShiftType(2L, "LATE", "14:30:00", "22:30:00", false, "Late shift");
        shiftTypeList.add(late);
        ShiftType day = new ShiftType(3L, "DAY", "08:30:00", "16:30:00", false, "Day shift");
        shiftTypeList.add(day);
        ShiftType night = new ShiftType(4L, "NIGHT", "22:30:00", "06:30:00", false, "Night shift");
        shiftTypeList.add(night);
        roster.setShiftTypeList(shiftTypeList);


        List<Pattern> patternList = getPatterns(early, late, day);
        roster.setPatternList(patternList);

        List<Contract> contractList = getContracts();
        roster.setContractList(contractList);

        List<PatternContractLine> patternContractLineList = new ArrayList<PatternContractLine>();
        patternContractLineList.add(new PatternContractLine(1L, contractList.get(0), patternList.get(0)));
        patternContractLineList.add(new PatternContractLine(2L, contractList.get(0), patternList.get(1)));
        patternContractLineList.add(new PatternContractLine(3L, contractList.get(0), patternList.get(2)));


        List<Employee> employeeList = getEmployees(contractList);
        roster.setEmployeeList(employeeList);

        List<Skill> skillList = new ArrayList<Skill>();
        Skill doctorSkill = new Skill("doc", "Doctor");
        skillList.add(doctorSkill);
        roster.setSkillList(skillList);

        List<SkillProficiency> skillProficiencyList = getSkillProficiencies(roster, doctorSkill);
        roster.setSkillProficiencyList(skillProficiencyList);

        return new RosterManager(roster);
    }

    private List<Pattern> getPatterns(ShiftType early, ShiftType late, ShiftType day) {
        List<Pattern> patternList = new ArrayList<>();
        patternList.add(new ShiftType3DaysPattern(1L, "Day-Early-Day", 1, day, early, day));
        patternList.add(new ShiftType2DaysPattern(2L, "Day-Late", 1, day, late));
        patternList.add(new FreeBefore2DaysWithAWorkDayPattern(3L, "Friday-free-before-week-end", 1, DayOfWeek.FRIDAY));
        return patternList;
    }

    private List<SkillProficiency> getSkillProficiencies(NurseRoster roster, Skill doctorSkill) {
        List<SkillProficiency> skillProficiencyList = new ArrayList<SkillProficiency>();
        for (Employee employee : roster.getEmployeeList()) {
            skillProficiencyList.add(new SkillProficiency(1L, employee, doctorSkill));
        }
        return skillProficiencyList;
    }

    private List<Employee> getEmployees(List<Contract> contractList) {
        List<Employee> employeeList = new ArrayList<Employee>();
        employeeList.add(new Employee(1L, "Hansen", "AH", contractList.get(0)));
        employeeList.add(new Employee(2L, "Möller", "CM", contractList.get(0)));
        employeeList.add(new Employee(3L, "Graf", "TB   ", contractList.get(0)));
        return employeeList;
    }

    private List<Contract> getContracts() {
        Contract fullTimeContract = new Contract(1L, "FULLTIME", "Full time", WeekendDefinition.SATURDAY_SUNDAY, new ArrayList<ContractLine>());
        List<Contract> contractList = new ArrayList<Contract>();
        contractList.add(fullTimeContract);
        return contractList;
    }

}
