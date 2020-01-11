package security;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import facades.UserFacade;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import entities.User;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import errorhandling.AuthenticationException;
import errorhandling.GenericExceptionMapper;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

@OpenAPIDefinition(
        info = @Info(
                title = "examCode",
                version = "0.1",
                description = "Backend of the Sem3 Exam project"
        ),
        tags = {
            @Tag(name = "Login endpoint", description = "API used for logging in users/admins")
        },
        servers = {
            @Server(
                    description = "For Local host testing",
                    url = "http://localhost:8080/examCode"
            ),
            @Server(
                    description = "Server API",
                    url = "https://barfodpraetorius.dk/examCode"
            )

        }
)
@Path("login")
public class LoginEndpoint {

  public static final int TOKEN_EXPIRE_TIME = 1000 * 60 * 30; //30 min
  private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
  public static final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);
  
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(summary = "Log in users/admin for the platform by returning a valid token that makes it possible to communicate data",
            tags = {"Login endpoint"},
            responses = {
             //   @ApiResponse(
             //           content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
                @ApiResponse(responseCode = "200", description = "The requested token was returned and the user is logged in and free to communicate data"),
                @ApiResponse(responseCode = "400", description = "No token was return and no login is possible")})
  public Response login(String jsonString) throws AuthenticationException {
    JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
    String username = json.get("username").getAsString();
    String password = json.get("password").getAsString();

    try {
      User user = USER_FACADE.getVeryfiedUser(username, password);
      String token = createToken(username, user.getRolesAsStrings());
      JsonObject responseJson = new JsonObject();
      responseJson.addProperty("username", username);
      responseJson.addProperty("token", token);
      return Response.ok(new Gson().toJson(responseJson)).build();

    } catch (JOSEException | AuthenticationException ex) {
      if (ex instanceof AuthenticationException) {
        throw (AuthenticationException) ex;
      }
      Logger.getLogger(GenericExceptionMapper.class.getName()).log(Level.SEVERE, null, ex);
    }
    throw new AuthenticationException("Invalid username or password! Please try again");
  }

  private String createToken(String userName, List<String> roles) throws JOSEException {

    StringBuilder res = new StringBuilder();
    for (String string : roles) {
      res.append(string);
      res.append(",");
    }
    String rolesAsString = res.length() > 0 ? res.substring(0, res.length() - 1) : "";
    String issuer = "semesterstartcode-dat3";

    JWSSigner signer = new MACSigner(SharedSecret.getSharedKey());
    Date date = new Date();
    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .subject(userName)
            .claim("username", userName)
            .claim("roles", rolesAsString)
            .claim("issuer", issuer)
            .issueTime(date)
            .expirationTime(new Date(date.getTime() + TOKEN_EXPIRE_TIME))
            .build();
    SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
    signedJWT.sign(signer);
    return signedJWT.serialize();

  }
}
