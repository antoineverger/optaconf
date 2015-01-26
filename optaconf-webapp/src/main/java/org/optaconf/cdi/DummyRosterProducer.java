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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@ApplicationScoped
public class DummyRosterProducer implements Serializable {

    protected HashMap<String, ShiftDate> shiftDateMap;

    @Produces
    @SessionScoped
    public RosterManager createDummyRoster (){
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

        this.generateShiftDateList(roster, new Date(14,1,1), new Date(14,2,1));

//        List<Shift> shiftList = new ArrayList<Shift>();
//        shiftList.add(new Shift(1L, ));
        return new RosterManager(roster);
    }

    private void generateShiftDateList(NurseRoster nurseRoster, Date startDate, Date endDate) {
        System.out.println(startDate);
        System.out.println(endDate);
        // Mimic JSR-310 LocalDate
        TimeZone LOCAL_TIMEZONE = TimeZone.getTimeZone("GMT");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(LOCAL_TIMEZONE);
        calendar.clear();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setCalendar(calendar);

        calendar.setTime(startDate);
        int startDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int startYear = calendar.get(Calendar.YEAR);
        calendar.setTime(endDate);
        int endDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int endYear = calendar.get(Calendar.YEAR);
        int maxDayIndex = endDayOfYear - startDayOfYear;
        if (startYear > endYear) {
            throw new IllegalStateException("The startYear (" + startYear
                    + " must be before endYear (" + endYear + ").");
        }
        if (startYear < endYear) {
            int tmpYear = startYear;
            calendar.setTime(startDate);
            while (tmpYear < endYear) {
                maxDayIndex += calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
                calendar.add(Calendar.YEAR, 1);
                tmpYear++;
            }
        }
        int shiftDateSize = maxDayIndex + 1;
        List<ShiftDate> shiftDateList = new ArrayList<ShiftDate>(shiftDateSize);
        shiftDateMap = new HashMap<String, ShiftDate>(shiftDateSize);
        long id = 0L;
        int dayIndex = 0;
        calendar.setTime(startDate);
        for (int i = 0; i < shiftDateSize; i++) {
            ShiftDate shiftDate = new ShiftDate();
            shiftDate.setId(id);
            shiftDate.setDayIndex(dayIndex);
            String dateString = dateFormat.format(calendar.getTime());
            shiftDate.setDateString(dateString);
            shiftDate.setDayOfWeek(DayOfWeek.valueOfCalendar(calendar.get(Calendar.DAY_OF_WEEK)));
            shiftDate.setShiftList(new ArrayList<Shift>());
            System.out.println(shiftDate);
            shiftDateList.add(shiftDate);
            shiftDateMap.put(dateString, shiftDate);
            id++;
            dayIndex++;
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        nurseRoster.setShiftDateList(shiftDateList);
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
