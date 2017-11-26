package PDT.Backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

public class QueryManager {

	private static final QueryManager INSTANCE = new QueryManager();

	public static QueryManager getInstance() {
		return INSTANCE;
	}

	private Connector connector;
	private Connector2 connector2;

	public QueryManager() {
		connector = Connector.getInstance();
		connector2 = Connector2.getInstance();
	}

	public JSONArray getAll() throws SQLException {
		String s = "SELECT ST_AsGeoJSON(ST_Transform(way, 4326)) AS result, name FROM planet_osm_line where highway = 'path'";
		ResultSet result = connector.getStatement().executeQuery(s);
		JSONArray geoJsons = new JSONArray();

		while (result.next()) {

			JSONObject json = new JSONObject();
			json.put("type", "Feature");
			json.put("geometry", new JSONObject(result.getString("result")));
			JSONObject properties = new JSONObject();
			properties.put("stroke", "#AD1700");
			properties.put("name", result.getString("name"));
			json.put("properties", properties);
			// System.out.println(json);
			geoJsons.put(json);
		}
		return geoJsons;
	}

	public JSONArray querry1() throws SQLException {
		String s = "SELECT ST_AsGeoJSON(ST_Transform(l.way, 4326)) AS result, l.name,l.highway "
				+ "FROM planet_osm_line l CROSS join planet_osm_polygon p " + "where p.name = 'Tatranský národný park' "
				+ "and l.highway = 'path' "
				+ "and  st_intersects(ST_Transform(l.way, 4326),ST_Transform(p.way, 4326)) = true ";
		ResultSet result = connector.getStatement().executeQuery(s);
		JSONArray geoJsons = new JSONArray();
		while (result.next()) {

			JSONObject json = new JSONObject();
			json.put("type", "Feature");
			json.put("geometry", new JSONObject(result.getString("result")));
			JSONObject properties = new JSONObject();
			properties.put("stroke", "#000099");
			properties.put("stroke-width", "10");
			properties.put("name", result.getString("name"));
			json.put("properties", properties);
			System.out.println(json);
			geoJsons.put(json);
		}
		return geoJsons;
	}

	public JSONArray querry2() throws SQLException {
		String s = "SELECT ST_AsGeoJSON(ST_Transform(l.way, 4326)) AS result, "
				+ "ST_Length(ST_Transform(l.way, 4326)::geography) as lenght,l.name,l.highway "
				+ "FROM planet_osm_line l CROSS join planet_osm_polygon p " + "where p.name = 'Tatranský národný park' "
				+ "and l.highway = 'path' "
				+ "and st_intersects(ST_Transform(l.way, 4326),ST_Transform(p.way, 4326)) = true";
		ResultSet result = connector.getStatement().executeQuery(s);
		JSONArray geoJsons = new JSONArray();

		while (result.next()) {

			JSONObject json = new JSONObject();
			json.put("type", "Feature");
			json.put("geometry", new JSONObject(result.getString("result")));
			JSONObject properties = new JSONObject();
			properties.put("stroke-width", "10");
			if (result.getString("name") != null)
				properties.put("name", result.getString("name") + " dlžka " + result.getString("lenght"));
			else
				properties.put("name", " dlžka " + result.getString("lenght"));

			if (result.getDouble("lenght") > 10000)
				properties.put("stroke", "#FF0000");
			else {
				properties.put("stroke", Helper.getcolor(result.getDouble("lenght")));
			}
			json.put("properties", properties);
			geoJsons.put(json);
			System.out.println(json);
		}
		return geoJsons;
	}

	public JSONArray querry3() throws SQLException {
		String s = "select ST_AsGeoJSON(ST_Transform(way, 4326))AS result,name from planet_osm_point where tourism is not null";
		ResultSet result = connector.getStatement().executeQuery(s);
		JSONArray geoJsons = new JSONArray();

		while (result.next()) {

			JSONObject json = new JSONObject();
			json.put("type", "Feature");
			json.put("geometry", new JSONObject(result.getString("result")));
			JSONObject properties = new JSONObject();
			properties.put("stroke", "#990000");
			properties.put("stroke-width", "10");
			properties.put("name", result.getString("name"));
			json.put("properties", properties);
			// System.out.println(json);
			geoJsons.put(json);
		}
		return geoJsons;
	}

	public JSONArray querry4(double lon, double lat, int range) throws SQLException {
		String s = "select ST_AsGeoJSON(ST_Transform(way, 4326)) AS result,name,'' as natural,'' as amenity  from planet_osm_line l where highway = 'path' and  ST_Distance( ST_GeomFromText('POINT("
				+ lon + " " + lat + ")', 4326)::geography,ST_Transform(way, 4326)) < " + range * 100
				+ " union select ST_AsGeoJSON(ST_Transform(way, 4326)) as result,name,p.natural,amenity from planet_osm_point p where (p.natural is not null or p.amenity is not null) and ST_Distance( ST_GeomFromText('POINT("
				+ lon + " " + lat + ")', 4326)::geography,ST_Transform(way, 4326)) < " + range * 100 + "";
		// System.out.println(s);
		ResultSet result = connector.getStatement().executeQuery(s);
		JSONArray geoJsons = new JSONArray();
		System.out.println(s);
		while (result.next()) {

			JSONObject json = new JSONObject();
			json.put("type", "Feature");
			json.put("geometry", new JSONObject(result.getString("result")));
			JSONObject properties = new JSONObject();
			properties.put("stroke", "#F6E27F");
			if (result.getString("name") != null)
				properties.put("name", result.getString("name"));
			else {
				properties.put("name", "unknown");
			}
			if (result.getString("amenity") != null) {
				properties.put("marker-color", "#AD1700");
				properties.put("marker-symbol", "cafe");
			} else if (result.getString("natural") != null) {
				properties.put("marker-color", "#00EE00");
				properties.put("marker-symbol", "park");
			}

			json.put("properties", properties);
			geoJsons.put(json);
		}
		return geoJsons;
	}

	public JSONArray querry5(double lat, double lon, int range) throws SQLException {
		String s = "WITH dist AS (SELECT ST_Distance( ST_GeomFromText('POINT(" + lon + " " + lat
				+ ")', 4326)::geography,  ST_Transform(way, 4326)) AS distance,  "
				+ "ST_AsGeoJSON(ST_Transform(way, 4326)) AS result,  name "
				+ "FROM planet_osm_point  where amenity = 'cafe') SELECT * from dist where distance < " + range * 100;
		ResultSet result = connector.getStatement().executeQuery(s);
		JSONArray geoJsons = new JSONArray();
		System.out.println(s);
		while (result.next()) {

			JSONObject json = new JSONObject();
			json.put("type", "Feature");
			json.put("geometry", new JSONObject(result.getString("result")));
			JSONObject properties = new JSONObject();
			properties.put("marker-color", "#AD1700");
			properties.put("marker-symbol", "cafe");
			properties.put("name", result.getString("name"));
			json.put("properties", properties);
			geoJsons.put(json);
		}
		// System.out.println("query 5");
		return geoJsons;
	}

	public JSONArray customSearch(boolean int1, boolean int2, boolean int3, boolean int4, boolean int5, boolean int6,
			boolean setdist, int distance, boolean easy, boolean medium, boolean hard, double lng, double lat)
			throws SQLException {

		String with = "with paths as ( select * from planet_osm_line l where l.highway = 'path' ), "
				+ "naturals as ( select * from planet_osm_point p where p.natural is not null) ";

		String base = "select distinct l.name as lname,ST_AsGeoJSON(ST_Transform(l.way,4326)) as lresult, "
				+ "p.name as pname, ST_AsGeoJSON(ST_Transform(p.way,4326)) as presult "
				+ " from paths l cross join naturals p where "
				+ "ST_DWithin(ST_Transform(l.way,4326)::geography , ST_Transform(p.way,4326)::geography,200) ";

		boolean firstInterast = true;
		String interasts = "and (";

		if (int1 == true) {
			firstInterast = false;
			interasts += "p.natural = 'peak'";
		}
		if (int2 == true) {
			if (firstInterast) {
				firstInterast = false;
				interasts += "p.natural = 'saddle'";
			} else
				interasts += "or p.natural = 'saddle'";
		}
		if (int3 == true) {
			if (firstInterast) {
				firstInterast = false;
				interasts += "p.natural = 'cave'";
			} else
				interasts += "or p.natural = 'cave'";
		}
		if (int4 == true) {
			if (firstInterast) {
				firstInterast = false;
				interasts += "p.natural = 'spring'";
			} else
				interasts += "or p.natural = 'spring'";
		}
		if (int5 == true) {
			if (firstInterast) {
				firstInterast = false;
				interasts += "p.natural = 'valley'";
			} else
				interasts += "or p.natural = 'valley'";
		}
		if (firstInterast)
			interasts = "";
		else
			interasts += " )";
		if (int6 == true) {
			interasts = "";
		}

		String maxDistance = "";
		if (setdist) {
			maxDistance = " and ST_DWithin(ST_GeomFromText('POINT(" + lng + " " + lat + ")',4326)::geography,"
					+ "ST_Transform(l.way,4326)::geography," + distance * 100 + ")";
		}
		String length = "";
		if (easy)
			length = "and ST_Length(ST_Transform(l.way, 4326)::geography) <1000";
		if (medium)
			length = "and ST_Length(ST_Transform(l.way, 4326)::geography) <2500";
		if (hard)
			length = "";

		String querry = with + base + interasts + maxDistance + length;
		System.out.println("query: " + querry);

		ResultSet result = connector2.getStatement().executeQuery(querry);
		JSONArray geoJsons = new JSONArray();
		while (result.next()) {

			JSONObject json = new JSONObject();
			json.put("type", "Feature");
			json.put("geometry", new JSONObject(result.getString("lresult")));
			JSONObject properties = new JSONObject();
			properties.put("stroke", "#F6E27F");
			if (result.getString("lname") != null)
				properties.put("name", result.getString("lname"));
			else {
				properties.put("name", "nezname");
			}
			json.put("properties", properties);
			geoJsons.put(json);

			JSONObject json2 = new JSONObject();
			json2.put("type", "Feature");
			json2.put("geometry", new JSONObject(result.getString("presult")));
			JSONObject properties2 = new JSONObject();
			properties2.put("marker-color", "#00cc00");
			properties2.put("marker-symbol", "park");
			if (result.getString("pname") != null)
				properties2.put("name", result.getString("pname"));
			else {
				properties2.put("name", "unknown name");
			}
			json2.put("properties", properties2);
			geoJsons.put(json2);

		}
		return geoJsons;
	}
}
