package PDT.Backend;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.json.JSONArray;
import org.json.JSONObject;

@Path("PDT")
public class Resources {

	QueryManager qm;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "Hello, World!";
	}

	@GET
	@Path("test")
	@Produces(MediaType.APPLICATION_JSON)
	public Response test() {
		JSONArray result = null;
		try {
			result = QueryManager.getInstance().getAll();
		} catch (SQLException e) {
			Logger.getLogger("Resource Manager").log(Level.SEVERE, "could not get DB response", e);
		}

		return addHeader(result.toString());
	}

	@GET
	@Path("querry1")
	@Produces(MediaType.APPLICATION_JSON)
	public Response querry1() {
		JSONArray result = null;
		try {
			result = QueryManager.getInstance().querry1();
		} catch (SQLException e) {
			Logger.getLogger("Resource Manager").log(Level.SEVERE, "could not get DB response", e);
		}

		return addHeader(result.toString());
	}

	@GET
	@Path("querry2")
	@Produces(MediaType.APPLICATION_JSON)
	public Response querry2() {
		JSONArray result = null;
		try {
			result = QueryManager.getInstance().querry2();
		} catch (SQLException e) {
			Logger.getLogger("Resource Manager").log(Level.SEVERE, "could not get DB response", e);
		}

		return addHeader(result.toString());
	}

	@GET
	@Path("querry3")
	@Produces(MediaType.APPLICATION_JSON)
	public Response querry3() {
		JSONArray result = null;
		try {
			result = QueryManager.getInstance().querry3();
		} catch (SQLException e) {
			Logger.getLogger("Resource Manager").log(Level.SEVERE, "could not get DB response", e);
		}

		return addHeader(result.toString());
	}

	@GET
	@Path("querry4/{lon}/{lat}/{range}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response querry4(@PathParam("lon") double lon, @PathParam("lat") double lat, @PathParam("range") int range) {
		System.out.println("!!!!!!!!!!!! " + lon + "   " + lat);
		JSONArray result = null;
		try {
			result = QueryManager.getInstance().querry4(lon, lat, range);
		} catch (SQLException e) {
			Logger.getLogger("Resource Manager").log(Level.SEVERE, "could not get DB response", e);
		}

		return addHeader(result.toString());
	}

	@GET
	@Path("querry5/{lon}/{lat}/{range}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response querry5(@PathParam("lon") double lon, @PathParam("lat") double lat, @PathParam("range") int range) {
		JSONArray result = null;
		System.out.println(lon + "  " + lat);

		try {
			result = QueryManager.getInstance().querry5(lat, lon, range);
		} catch (SQLException e) {
			Logger.getLogger("Resource Manager").log(Level.SEVERE, "could not get DB response", e);
		}

		return addHeader(result.toString());
	}

	@GET
	@Path("paramtest/{lon}/{lat}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response paramtest(@PathParam("lon") double lon, @PathParam("lat") double lat) {
		JSONObject out = new JSONObject();
		out.put("lon", lon);
		out.put("lat", lat);

		return addHeader(out.toString());
	}

	@GET
	@Path("customsearch/{int1}/{int2}/{int3}/{int4}/{int5}/{int6}/{setdist}/{distance}/{easy}/{medium}/{hard}/{lng}/{lat}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response customSearch(@PathParam("int1") boolean int1, @PathParam("int2") boolean int2,
			@PathParam("int3") boolean int3, @PathParam("int4") boolean int4, @PathParam("int5") boolean int5,
			@PathParam("int6") boolean int6, @PathParam("setdist") boolean setdist, @PathParam("distance") int distance,
			@PathParam("easy") boolean easy, @PathParam("medium") boolean medium, @PathParam("hard") boolean hard,
			@PathParam("lng") double lng, @PathParam("lat") double lat) {
		JSONArray result = null;
		try {
			System.out.println("asdasd");
			result = QueryManager.getInstance().customSearch(int1, int2, int3, int4, int5, int6, setdist, distance,
					easy, medium, hard, lng, lat);
		} catch (SQLException e) {
			Logger.getLogger("Resource Manager").log(Level.SEVERE, "could not get DB response", e);
		}
		return addHeader(result.toString());
	}

	private Response addHeader(String s) {
		ResponseBuilder response = Response.status(Status.ACCEPTED);
		response.entity(s);
		response.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
				.header("Access-Control-Allow-Credentials", "true")
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
				.header("Access-Control-Max-Age", "1209600");
		return response.build();
	}
}
