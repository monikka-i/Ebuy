/**
 * 
 */
package io.doll.Dollar.resource;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import io.doll.Dollar.Exception.InvalidRequestException;
import io.doll.Dollar.Response.OutputResponse;
import io.doll.Model.Fruit;
import io.doll.Model.UserModel;

/**
 * @author AR364900
 *
 */
@Path("/hello-world")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

	private final String template;
	private final String defaultName;
	private final AtomicLong counter;

	public HelloWorldResource(String template, String defaultName) {
		this.template = template;
		this.defaultName = defaultName;
		this.counter = new AtomicLong();
	}

	@Path("/get")
	@GET
	@Timed
	@Produces(MediaType.APPLICATION_JSON)
	public String sayHello(@QueryParam("name") String name) {
		// final String value = String.format(template,
		// name.orElse(defaultName));

		return "Dollar is up and running";

	}

	@POST
	@Path("/getAllItems")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllItems(@Valid UserModel request) {

		MongoClient mongo = null;
		try {
			mongo = new MongoClient();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DB db = mongo.getDB("Fruitpanda");
		DBCollection catalogue = db.getCollection("u_fp_catalogue");

		List<BasicDBObject> list = new ArrayList<BasicDBObject>();

		DBCursor cursor = catalogue.find();

		while (cursor.hasNext()) {
			BasicDBObject b = (BasicDBObject) cursor.next();
			b.remove("_id");
			list.add(b);
		}
		return list.toString();

		/*
		 * Gson g = new Gson(); Fruit f = g.fromJson(str, Fruit.class); String
		 * response = g.toJson(f);
		 */
	}

	@POST
	@Path("/getItem")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Object getItem(String request) {
		OutputResponse response = new OutputResponse();
		JSONObject requestObj = new JSONObject(request);
		MongoClient mongo = null;
		List<BasicDBObject> list = null;
		try {
			if (!requestObj.has("fruitId"))
				throw new InvalidRequestException("fruit id is missing");
			mongo = new MongoClient();
			DB db = mongo.getDB("Fruitpanda");
			DBCollection catalogue = db.getCollection("u_fp_catalogue");
			BasicDBObject document = new BasicDBObject();
			document.put("fruitId", requestObj.getString("fruitId"));
			list = new ArrayList<BasicDBObject>();
			DBCursor cursor = catalogue.find(document);

			while (cursor.hasNext()) {
				BasicDBObject b = (BasicDBObject) cursor.next();
				b.remove("_id");
				list.add(b);
			}
			response.setOutputCode(200);
			response.setOutputStatus("success");
			response.setMessage("success");
			response.setData(list.toString());
		} catch (UnknownHostException e) {
			return new OutputResponse(401, "failure", "connection failed", "");
		} catch (InvalidRequestException e) {
			return new OutputResponse(402, "failure", e.getMessage(), "");
		} catch (Exception e) {
			return response;
		}
		return response;
		/*
		 * Gson g = new Gson(); Fruit f = g.fromJson(str, Fruit.class); String
		 * response = g.toJson(f);
		 */
	}

}
