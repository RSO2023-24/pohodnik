package si.fri.rso.pohodnik.version.api.v1.resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import si.fri.rso.pohodnik.version.lib.User;
import si.fri.rso.pohodnik.version.services.beans.UserBean;
import com.kumuluz.ee.logs.cdi.Log;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@Log
@ApplicationScoped
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private Logger log = Logger.getLogger(UserResource.class.getName());

    @Inject
    private UserBean userBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    @Operation(summary = "Get list of users", description = "Returns list of all users.")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "List of users",
            content = @Content(schema = @Schema(implementation = User.class, type = SchemaType.ARRAY))),
        @APIResponse(responseCode = "204", description = "No users found")
    })
    public Response getUser() {
        List<User> user = userBean.getUserFilter(uriInfo);
        return Response.status(Response.Status.OK).entity(user).build();
    }

    @GET
    @Path("/{UserId}")
    @Operation(summary = "Get user by ID", description = "Returns user with the given ID.")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "User details",
            content = @Content(schema = @Schema(implementation = User.class))),
        @APIResponse(responseCode = "404", description = "User not found")
    })
    public Response getUser(@Parameter(description = "User ID.", required = true)
                            @PathParam("UserId") Integer UserId) {
        User user = userBean.getUser(UserId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(user).build();
    }

    @POST
    @Operation(summary = "Create a new user", description = "Creates a new user with the given details.")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "User created",
            content = @Content(schema = @Schema(implementation = User.class))),
        @APIResponse(responseCode = "400", description = "Invalid user details provided")
    })
    public Response createUser(@RequestBody(description = "User details", required = true,
            content = @Content(schema = @Schema(implementation = User.class))) User user) {
        if ((user.getfirstName() == null || user.getlastName() == null || user.getskill() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            user = userBean.createUser(user);
        }
        return Response.status(Response.Status.CONFLICT).entity(user).build();
    }

    @PUT
    @Path("{UserId}")
    @Operation(summary = "Update a user", description = "Updates the details of an existing user.")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "User updated",
            content = @Content(schema = @Schema(implementation = User.class))),
        @APIResponse(responseCode = "404", description = "User not found")
    })
    public Response putUser(@Parameter(description = "User ID.", required = true)
                            @PathParam("UserId") Integer UserId,
                            @RequestBody(description = "Updated user details", required = true,
            content = @Content(schema = @Schema(implementation = User.class))) User user){
        user = userBean.putUser(UserId, user);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.NOT_MODIFIED).build();
    }

    @DELETE
    @Path("{UserId}")
    @Operation(summary = "Delete a user", description = "Deletes a user with the given ID.")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "User deleted"),
        @APIResponse(responseCode = "404", description = "User not found")
    })
    public Response deleteUser(@Parameter(description = "User ID.", required = true)
                               @PathParam("UserId") Integer UserId){
        boolean deleted = userBean.deleteUser(UserId);
        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}