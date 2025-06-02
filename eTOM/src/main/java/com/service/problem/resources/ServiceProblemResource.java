package com.service.problem.resources;

import com.service.problem.model.ServiceProblem;
import com.service.problem.service.ServiceProblemService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/problems")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServiceProblemResource {

    private ServiceProblemService problemService = new ServiceProblemService();

    @GET
    public Response getAllProblems() {
        System.out.println("ServiceProblemRescource: Received GET request for all problems.");
        try {
            List<ServiceProblem> problems = problemService.getAllProblems();
            System.out.println("ServiceProblemResource: Returning " + problems.size() + " problems.");
            return Response.ok(problems).build();
        } catch (RuntimeException e) {
            System.err.println("ServiceProblemResource: Error getting all problems: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving problems").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getProblem(@PathParam("id") String id) {
        System.out.println("ServiceProblemResource: Received GET request for problem with ID: " + id);
        try {
            ServiceProblem problem = problemService.getProblemById(id);
            if (problem != null) {
                System.out.println("ServiceProblemResource: Found problem with ID: " + id + ".");
                return Response.ok(problem).build();
            } else {
                System.out.println("ServiceProblemResource: Problem with ID " + id + " not found.");
                return Response.status(Response.Status.NOT_FOUND).entity("Problem not found").build();
            }
        } catch (RuntimeException e) {
             System.err.println("ServiceProblemResource: Error getting problem by ID " + id + ": " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving problem").build();
        }
    }

    @POST
    public Response createProblem(ServiceProblem problem) {
        System.out.println("ServiceProblemResource: Received POST request to create problem.");
        try {
            ServiceProblem createdProblem = problemService.createProblem(problem);
             System.out.println("ServiceProblemResource: Created problem with ID: " + createdProblem.getId());
            return Response.status(Response.Status.CREATED)
                    .entity(createdProblem)
                    .build();
        } catch (RuntimeException e) {
             System.err.println("ServiceProblemResource: Error creating problem: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating problem: " + e.getMessage())
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateProblem(@PathParam("id") String id, ServiceProblem problem) {
         System.out.println("ServiceProblemResource: Received PUT request to update problem with ID: " + id);
        try {
            ServiceProblem updatedProblem = problemService.updateProblem(id, problem);
            if (updatedProblem != null) {
                 System.out.println("ServiceProblemResource: Updated problem with ID: " + id);
                return Response.ok(updatedProblem).build();
            } else {
                 System.out.println("ServiceProblemResource: Problem with ID " + id + " not found for update.");
                return Response.status(Response.Status.NOT_FOUND).entity("Problem not found for update").build();
            }
        } catch (RuntimeException e) {
             System.err.println("ServiceProblemResource: Error updating problem with ID " + id + ": " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error updating problem").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProblem(@PathParam("id") String id) {
         System.out.println("ServiceProblemResource: Received DELETE request for problem with ID: " + id);
        try {
            boolean deleted = problemService.deleteProblem(id);
            if (deleted) {
                 System.out.println("ServiceProblemResource: Deleted problem with ID: " + id);
                return Response.noContent().build();
            } else {
                 System.out.println("ServiceProblemResource: Problem with ID " + id + " not found for deletion.");
                return Response.status(Response.Status.NOT_FOUND).entity("Problem not found for deletion").build();
            }
        } catch (RuntimeException e) {
             System.err.println("ServiceProblemResource: Error deleting problem with ID " + id + ": " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting problem").build();
        }
    }

    @POST
    @Path("/{id}/acknowledge")
    public Response acknowledgeProblem(@PathParam("id") String id) {
        System.out.println("ServiceProblemResource: Received POST request to acknowledge problem with ID: " + id);
        try {
            ServiceProblem acknowledgedProblem = problemService.acknowledgeProblem(id);
             if (acknowledgedProblem != null) {
                 System.out.println("ServiceProblemResource: Acknowledged problem with ID: " + id);
                 return Response.ok(acknowledgedProblem).build();
             } else {
                 System.out.println("ServiceProblemResource: Problem with ID " + id + " not found for acknowledgment.");
                 return Response.status(Response.Status.NOT_FOUND).entity("Problem not found for acknowledgment").build();
             }
        } catch (RuntimeException e) {
            System.err.println("ServiceProblemResource: Error acknowledging problem with ID " + id + ": " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error acknowledging problem").build();
        }
    }

    @POST
    @Path("/{id}/unacknowledge")
    public Response unacknowledgeProblem(@PathParam("id") String id) {
         System.out.println("ServiceProblemResource: Received POST request to unacknowledge problem with ID: " + id);
         try {
            ServiceProblem unacknowledgedProblem = problemService.unacknowledgeProblem(id);
            if (unacknowledgedProblem != null) {
                 System.out.println("ServiceProblemResource: Unacknowledged problem with ID: " + id);
                 return Response.ok(unacknowledgedProblem).build();
            } else {
                 System.out.println("ServiceProblemResource: Problem with ID " + id + " not found for unacknowledgment.");
                 return Response.status(Response.Status.NOT_FOUND).entity("Problem not found for unacknowledgment").build();
            }
        } catch (RuntimeException e) {
            System.err.println("ServiceProblemResource: Error unacknowledging problem with ID " + id + ": " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error unacknowledging problem").build();
        }
    }

    @GET
    @Path("/search")
    public Response searchProblems(
            @QueryParam("status") String status,
            @QueryParam("priority") String priority,
            @QueryParam("category") String category) {
        System.out.println("ServiceProblemResource: Received GET request for search with status=" + status + ", priority=" + priority + ", category=" + category);
        try {
            List<ServiceProblem> results = problemService.searchProblems(status, priority, category);
            System.out.println("ServiceProblemResource: Search found " + results.size() + " problems.");
            return Response.ok(results).build();
        }  catch (IllegalArgumentException e) {
             System.err.println("ServiceProblemResource: Invalid search parameter value: " + e.getMessage());
             return Response.status(Response.Status.BAD_REQUEST).entity("Invalid search parameter: " + e.getMessage()).build();
        }catch (RuntimeException e) {
            System.err.println("ServiceProblemResource: Error searching problems: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error searching problems").build();
        }
    }
} 