package com.service.problem.service;

import com.service.problem.model.ServiceProblem;
import com.service.problem.model.ServiceProblem.Status;
import com.service.problem.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ServiceProblemService {

    public List<ServiceProblem> getAllProblems() {
        System.out.println("ServiceProblemService: Attempting to get all problems.");
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            System.out.println("ServiceProblemService: Hibernate session opened successfully.");
            Query<ServiceProblem> query = session.createQuery("FROM ServiceProblem", ServiceProblem.class);
            System.out.println("ServiceProblemService: HQL query created: FROM ServiceProblem");
            List<ServiceProblem> problems = query.list();
            System.out.println("ServiceProblemService: Query executed. Found " + problems.size() + " problems.");
            return problems;
        } catch (Exception e) {
            System.err.println("ServiceProblemService: Error fetching problems: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error fetching problems", e); // Re-throw as runtime exception
        }
    }

    public ServiceProblem getProblemById(String id) {
        System.out.println("ServiceProblemService: Attempting to get problem by ID: " + id);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ServiceProblem problem = session.get(ServiceProblem.class, id);
            if (problem != null) {
                System.out.println("ServiceProblemService: Found problem with ID: " + id);
            } else {
                System.out.println("ServiceProblemService: Problem with ID " + id + " not found.");
            }
            return problem;
        } catch (Exception e) {
            System.err.println("ServiceProblemService: Error fetching problem by ID: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error fetching problem by ID", e); // Re-throw
        }
    }

    public ServiceProblem createProblem(ServiceProblem problem) {
        System.out.println("ServiceProblemService: Attempting to create problem.");
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            
            // Generate ID if not already set (e.g., by resource layer)
            if (problem.getId() == null || problem.getId().isEmpty()) {
                 problem.setId(UUID.randomUUID().toString());
            }

            // Set creation timestamp if not set
            if (problem.getCreatedAt() == null) {
                problem.setCreatedAt(LocalDateTime.now());
            }
            // Set update timestamp
            problem.setUpdatedAt(LocalDateTime.now());

            session.persist(problem);
            transaction.commit();
            System.out.println("ServiceProblemService: Problem created with ID: " + problem.getId());
            return problem;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("ServiceProblemService: Error creating problem: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error creating problem", e); // Re-throw
        }
    }

    public ServiceProblem updateProblem(String id, ServiceProblem updatedProblem) {
        System.out.println("ServiceProblemService: Attempting to update problem with ID: " + id);
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ServiceProblem existingProblem = session.get(ServiceProblem.class, id);

            if (existingProblem != null) {
                // Update fields. Only update mutable fields.
                existingProblem.setTitle(updatedProblem.getTitle());
                existingProblem.setDescription(updatedProblem.getDescription());
                existingProblem.setPriority(updatedProblem.getPriority());
                existingProblem.setSeverity(updatedProblem.getSeverity());
                existingProblem.setCategory(updatedProblem.getCategory());
                existingProblem.setStatus(updatedProblem.getStatus());
                existingProblem.setAssignedTo(updatedProblem.getAssignedTo());
                existingProblem.setRelatedServiceId(updatedProblem.getRelatedServiceId());
                
                // Update timestamp
                existingProblem.setUpdatedAt(LocalDateTime.now());

                session.merge(existingProblem); // Use merge for updates
                transaction.commit();
                System.out.println("ServiceProblemService: Problem with ID " + id + " updated successfully.");
                return existingProblem;
            } else {
                System.out.println("ServiceProblemService: Problem with ID " + id + " not found for update.");
                return null; // Indicate problem not found
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("ServiceProblemService: Error updating problem: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error updating problem", e); // Re-throw
        }
    }

    public boolean deleteProblem(String id) {
        System.out.println("ServiceProblemService: Attempting to delete problem with ID: " + id);
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ServiceProblem problem = session.get(ServiceProblem.class, id);

            if (problem != null) {
                session.remove(problem); // Use remove for deletion
                transaction.commit();
                System.out.println("ServiceProblemService: Problem with ID " + id + " deleted successfully.");
                return true; // Indicate successful deletion
            } else {
                System.out.println("ServiceProblemService: Problem with ID " + id + " not found for deletion.");
                return false; // Indicate problem not found
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("ServiceProblemService: Error deleting problem: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error deleting problem", e); // Re-throw
        }
    }

    public ServiceProblem acknowledgeProblem(String id) {
        System.out.println("ServiceProblemService: Attempting to acknowledge problem with ID: " + id);
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ServiceProblem problem = session.get(ServiceProblem.class, id);

            if (problem != null) {
                if (problem.getStatus() != Status.ACKNOWLEDGED) {
                    problem.setStatus(Status.ACKNOWLEDGED);
                    problem.setUpdatedAt(LocalDateTime.now());
                    // TODO: Create ProblemAcknowledgement entry
                    session.merge(problem);
                    transaction.commit();
                    System.out.println("ServiceProblemService: Problem with ID " + id + " acknowledged.");
                } else {
                     System.out.println("ServiceProblemService: Problem with ID " + id + " was already acknowledged.");
                }
                return problem;
            } else {
                System.out.println("ServiceProblemService: Problem with ID " + id + " not found for acknowledgment.");
                return null; // Indicate problem not found
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("ServiceProblemService: Error acknowledging problem: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error acknowledging problem", e); // Re-throw
        }
    }
    
     public ServiceProblem unacknowledgeProblem(String id) {
        System.out.println("ServiceProblemService: Attempting to unacknowledge problem with ID: " + id);
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            ServiceProblem problem = session.get(ServiceProblem.class, id);

            if (problem != null) {
                 if (problem.getStatus() == Status.ACKNOWLEDGED) {
                    problem.setStatus(Status.NEW);
                    problem.setUpdatedAt(LocalDateTime.now());
                    // TODO: Create ProblemUnacknowledgement entry
                    session.merge(problem);
                    transaction.commit();
                     System.out.println("ServiceProblemService: Problem with ID " + id + " unacknowledged.");
                } else {
                     System.out.println("ServiceProblemService: Problem with ID " + id + " was not acknowledged.");
                }
                return problem;
            } else {
                System.out.println("ServiceProblemService: Problem with ID " + id + " not found for unacknowledgment.");
                return null; // Indicate problem not found
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("ServiceProblemService: Error unacknowledging problem: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error unacknowledging problem", e); // Re-throw
        }
    }

    public List<ServiceProblem> searchProblems(String status, String priority, String category) {
        System.out.println("ServiceProblemService: Searching problems with status=" + status + ", priority=" + priority + ", category=" + category);
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            StringBuilder hql = new StringBuilder("FROM ServiceProblem sp WHERE 1=1");
            if (status != null && !status.isEmpty()) {
                hql.append(" AND sp.status = :status");
            }
            if (priority != null && !priority.isEmpty()) {
                hql.append(" AND sp.priority = :priority");
            }
            if (category != null && !category.isEmpty()) {
                hql.append(" AND sp.category = :category");
            }

            Query<ServiceProblem> query = session.createQuery(hql.toString(), ServiceProblem.class);

            if (status != null && !status.isEmpty()) {
                try {
                    query.setParameter("status", Status.valueOf(status.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    System.err.println("ServiceProblemService: Invalid status value: " + status);
                    // Depending on desired behavior, re-throw or handle differently
                    throw new IllegalArgumentException("Invalid status value: " + status, e);
                }
            }
            if (priority != null && !priority.isEmpty()) {
                 try {
                    query.setParameter("priority", ServiceProblem.Priority.valueOf(priority.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    System.err.println("ServiceProblemService: Invalid priority value: " + priority);
                     throw new IllegalArgumentException("Invalid priority value: " + priority, e);
                }
            }
            if (category != null && !category.isEmpty()) {
                query.setParameter("category", category);
            }

            List<ServiceProblem> results = query.list();
            System.out.println("ServiceProblemService: Search executed. Found " + results.size() + " problems.");
            return results;
        } catch (IllegalArgumentException e) {
             System.err.println("ServiceProblemService: Invalid search parameter value: " + e.getMessage());
             e.printStackTrace();
             throw e; // Re-throw the specific IllegalArgumentException
        } catch (Exception e) {
            System.err.println("ServiceProblemService: Error searching problems: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error searching problems", e); // Re-throw
        }
    }
} 