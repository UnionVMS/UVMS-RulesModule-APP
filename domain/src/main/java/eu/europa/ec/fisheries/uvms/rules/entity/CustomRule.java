/*
﻿Developed with the contribution of the European Commission - Directorate General for Maritime Affairs and Fisheries
© European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can
redistribute it and/or modify it under the terms of the GNU General Public License as published by the
Free Software Foundation, either version 3 of the License, or any later version. The IFDM Suite is distributed in
the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details. You should have received a
copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.
 */
package eu.europa.ec.fisheries.uvms.rules.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import eu.europa.ec.fisheries.uvms.rules.constant.UvmsConstants;

//@formatter:off
@Entity
@Table(name = "customrule")
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = UvmsConstants.GET_RUNNABLE_CUSTOM_RULES, query = "SELECT r FROM CustomRule r WHERE active = true AND archived = false"), // for rule engine
        @NamedQuery(name = UvmsConstants.LIST_CUSTOM_RULES_BY_USER, query = "SELECT r FROM CustomRule r WHERE archived = false AND (availability = 'GLOBAL' OR availability = 'PUBLIC' OR updatedBy = :updatedBy)"),
        @NamedQuery(name = UvmsConstants.FIND_CUSTOM_RULE_BY_GUID, query = "SELECT r FROM CustomRule r WHERE r.guid = :guid"),
        @NamedQuery(name = UvmsConstants.FIND_CUSTOM_RULE_GUID_FOR_TICKETS, query = "SELECT r.guid FROM CustomRule r LEFT OUTER JOIN r.ruleSubscriptionList s WHERE r.availability = 'GLOBAL' OR (s.owner = :owner AND s.type='TICKET')")
})
//@formatter:on
public class CustomRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "rule_id")
    private Long id;

    @Column(name = "rule_name")
    private String name;

    @Column(name = "rule_guid")
    private String guid;

    @Column(name = "rule_description")
    private String description;

    @Column(name = "rule_availability")
    private String availability;

    @Column(name = "rule_organisation")
    private String organisation;

    @Column(name = "rule_startdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "rule_enddate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "rule_active")
    private Boolean active;

    @Column(name = "rule_archived")
    private Boolean archived;

    @Column(name = "rule_lasttriggered")
    @Temporal(TemporalType.TIMESTAMP)
    private Date triggered;

    @Column(name = "rule_updattim")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    @Column(name = "rule_upuser")
    @NotNull
    private String updatedBy;

    @OneToMany(mappedBy = "customRule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RuleSubscription> ruleSubscriptionList;

    //@OrderBy("order")
    @OneToMany(mappedBy = "customRule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RuleSegment> ruleSegmentList;

    //@OrderBy("order")
    @OneToMany(mappedBy = "customRule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RuleAction> ruleActionList;

    //@OrderBy("start")
    @OneToMany(mappedBy = "customRule", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Interval> intervals;

    public CustomRule() {
        this.guid = UUID.randomUUID().toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Date getTriggered() {
        return triggered;
    }

    public void setTriggered(Date triggered) {
        this.triggered = triggered;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    // @XmlTransient
    public List<RuleSegment> getRuleSegmentList() {
        if (ruleSegmentList == null) {
            ruleSegmentList = new ArrayList<>();
        }
        return ruleSegmentList;
    }

    public void setRuleSegmentList(List<RuleSegment> ruleSegmentList) {
        this.ruleSegmentList = ruleSegmentList;
    }

    // @XmlTransient
    public List<RuleAction> getRuleActionList() {
        if (ruleActionList == null) {
            ruleActionList = new ArrayList<>();
        }
        return ruleActionList;
    }

    public void setRuleActionList(List<RuleAction> ruleActionList) {
        this.ruleActionList = ruleActionList;
    }

    public List<Interval> getIntervals() {
        if (intervals == null) {
            intervals = new ArrayList<>();
        }
        return intervals;
    }

    public void setIntervals(List<Interval> intervals) {
        this.intervals = intervals;
    }

    public List<RuleSubscription> getRuleSubscriptionList() {
        if (ruleSubscriptionList == null) {
            ruleSubscriptionList = new ArrayList<>();
        }
        return ruleSubscriptionList;
    }

    public void setRuleSubscriptionList(List<RuleSubscription> ruleSubscriptionList) {
        this.ruleSubscriptionList = ruleSubscriptionList;
    }

    @Override
    public int hashCode() {
        int result = ruleSegmentList != null ? ruleSegmentList.hashCode() : 0;
        result = 31 * result + (ruleActionList != null ? ruleActionList.hashCode() : 0);
        result = 31 * result + (intervals != null ? intervals.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CustomRule) {
            CustomRule other = (CustomRule) obj;
            return (getRuleSegmentList().size() != other.getRuleSegmentList().size() || !compareRuleSegmentList(other)) && (getRuleActionList().size() != other.getRuleActionList().size() || !compareRuleActionList(other)) && (getIntervals().size() != other.getIntervals().size() || !compareRuleIntervalList(other));
        }
        return false;
    }

    private boolean compareRuleIntervalList(CustomRule other) {
        for (int i = 0; i < getIntervals().size(); i++) {
            Interval a = getIntervals().get(i);
            Interval b = other.getIntervals().get(i);
            if (!a.equals(b)) {
                return true;
            }
        }
        return false;
    }

    private boolean compareRuleActionList(CustomRule other) {
        for (int i = 0; i < getRuleActionList().size(); i++) {
            RuleAction a = getRuleActionList().get(i);
            RuleAction b = other.getRuleActionList().get(i);
            if (!a.equals(b)) {
                return true;
            }
        }
        return false;
    }

    private boolean compareRuleSegmentList(CustomRule other) {
        for (int i = 0; i < getRuleSegmentList().size(); i++) {
            RuleSegment a = getRuleSegmentList().get(i);
            RuleSegment b = other.getRuleSegmentList().get(i);
            if (!a.equals(b)) {
                return true;
            }
        }
        return false;
    }
}