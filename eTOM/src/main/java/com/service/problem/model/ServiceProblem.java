package com.service.problem.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "service_problems")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceProblem {
    @Id
    @JsonProperty("id")
    private String id;
    
    @Column(nullable = false)
    @JsonProperty("title")
    private String title;
    
    @Column(columnDefinition = "TEXT")
    @JsonProperty("description")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("priority")
    private Priority priority;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("severity")
    private Severity severity;
    
    @JsonProperty("category")
    private String category;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JsonProperty("status")
    private Status status;
    
    @Column(name = "created_at")
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
    
    @Column(name = "resolved_at")
    @JsonProperty("resolvedAt")
    private LocalDateTime resolvedAt;
    
    @Column(name = "created_by")
    @JsonProperty("createdBy")
    private String createdBy;
    
    @Column(name = "assigned_to")
    @JsonProperty("assignedTo")
    private String assignedTo;
    
    @Column(name = "related_service_id")
    @JsonProperty("relatedServiceId")
    private String relatedServiceId;
    
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Priority getPriority() { return priority; }
    public Severity getSeverity() { return severity; }
    public String getCategory() { return category; }
    public Status getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public String getCreatedBy() { return createdBy; }
    public String getAssignedTo() { return assignedTo; }
    public String getRelatedServiceId() { return relatedServiceId; }

    public void setId(String id) {
        this.id = id;
    }
    
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setSeverity(Severity severity) { this.severity = severity; }
    public void setCategory(String category) { this.category = category; }
    public void setStatus(Status status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
    public void setRelatedServiceId(String relatedServiceId) { this.relatedServiceId = relatedServiceId; }
    
    public enum Priority {
        LOW, MEDIUM, HIGH, CRITICAL
    }
    
    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
    
    public enum Status {
        NEW, ACKNOWLEDGED, IN_PROGRESS, RESOLVED, CLOSED
    }
} 