/*
 * Copyright 2010 JBoss Inc
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

package org.optaconf.domain;

import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaconf.common.domain.*;
import org.optaconf.domain.request.DayOffRequest;
import org.optaconf.domain.contract.Contract;
import org.optaconf.domain.request.DayOffRequest;
import org.optaconf.domain.request.DayOnRequest;
import org.optaconf.domain.request.ShiftOffRequest;
import org.optaconf.domain.request.ShiftOnRequest;

@XStreamAlias("Employee")
public class Employee extends org.optaconf.common.domain.AbstractPersistable {

    /**
     * Construct an employee
     * @param id
     * @param name
     * @param code
     * @param contract
     */
    public Employee(Long id, String name, String code, Contract contract) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.contract = contract;
    }

    private String code;
    private String name;
    private Contract contract;

    private Map<ShiftDate, DayOffRequest> dayOffRequestMap;
    private Map<ShiftDate, DayOnRequest> dayOnRequestMap;
    private Map<Shift, ShiftOffRequest> shiftOffRequestMap;
    private Map<Shift, ShiftOnRequest> shiftOnRequestMap;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public int getWeekendLength() {
        return getContract().getWeekendLength();
    }

    public Map<ShiftDate, DayOffRequest> getDayOffRequestMap() {
        return dayOffRequestMap;
    }

    public void setDayOffRequestMap(Map<ShiftDate, DayOffRequest> dayOffRequestMap) {
        this.dayOffRequestMap = dayOffRequestMap;
    }

    public Map<ShiftDate, DayOnRequest> getDayOnRequestMap() {
        return dayOnRequestMap;
    }

    public void setDayOnRequestMap(Map<ShiftDate, DayOnRequest> dayOnRequestMap) {
        this.dayOnRequestMap = dayOnRequestMap;
    }

    public Map<Shift, ShiftOffRequest> getShiftOffRequestMap() {
        return shiftOffRequestMap;
    }

    public void setShiftOffRequestMap(Map<Shift, ShiftOffRequest> shiftOffRequestMap) {
        this.shiftOffRequestMap = shiftOffRequestMap;
    }

    public Map<Shift, ShiftOnRequest> getShiftOnRequestMap() {
        return shiftOnRequestMap;
    }

    public void setShiftOnRequestMap(Map<Shift, ShiftOnRequest> shiftOnRequestMap) {
        this.shiftOnRequestMap = shiftOnRequestMap;
    }

    public String getLabel() {
        return name;
    }

    @Override
    public String toString() {
        return code + "(" + name + ")";
    }

}
