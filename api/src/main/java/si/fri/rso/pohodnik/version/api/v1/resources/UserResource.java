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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;



@ApplicationScoped
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private Logger log = Logger.getLogger(UserResource.class.getName());

    @Inject
    private UserBean UserBean;


    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Get all image metadata.", summary = "Get all metadata")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of image metadata",
                    content = @Content(schema = @Schema(implementation = User.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    public Response getUser() {

        List<User> User = UserBean.getUserFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(User).build();
    }


    @Operation(description = "Get metadata for an image.", summary = "Get metadata for an image")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Image metadata",
                    content = @Content(
                            schema = @Schema(implementation = User.class))
            )})
    @GET
    @Path("/{UserId}")
    public Response getUser(@Parameter(description = "Metadata ID.", required = true)
                                     @PathParam("UserId") Integer UserId) {

        User User = UserBean.getUser(UserId);

        if (User == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(User).build();
    }

    @Operation(description = "Add image metadata.", summary = "Add metadata")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Metadata successfully added."
            ),
            @APIResponse(responseCode = "405", description = "Validation error .")
    })
    @POST
    public Response createUser(@RequestBody(
            description = "DTO object with image metadata.",
            required = true, content = @Content(
            schema = @Schema(implementation = User.class))) User User) {

        if ((User.getfirstName() == null || User.getlastName() == null || User.getskill() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            User = UserBean.createUser(User);
        }

        return Response.status(Response.Status.CONFLICT).entity(User).build();

    }


    @Operation(description = "Update metadata for an image.", summary = "Update metadata")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Metadata successfully updated."
            )
    })
    @PUT
    @Path("{UserId}")
    public Response putUser(@Parameter(description = "Metadata ID.", required = true)
                                     @PathParam("UserId") Integer UserId,
                                     @RequestBody(
                                             description = "DTO object with image metadata.",
                                             required = true, content = @Content(
                                             schema = @Schema(implementation = User.class)))
                                             User User){

        User = UserBean.putUser(UserId, User);

        if (User == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

    @Operation(description = "Delete metadata for an image.", summary = "Delete metadata")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Metadata successfully deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Not found."
            )
    })
    @DELETE
    @Path("{UserId}")
    public Response deleteUser(@Parameter(description = "Metadata ID.", required = true)
                                        @PathParam("UserId") Integer UserId){

        boolean deleted = UserBean.deleteUser(UserId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}