package com.ebuy.service;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

/**
 * @author Monikka
 *
 */
@Path("/ebuy")
@Produces(MediaType.APPLICATION_JSON)
public class EbuyService {
	private final String template;
	private final String defaultName;
	private final AtomicLong counter;

	public EbuyService(String template, String defaultName) {
		this.template = template;
		this.defaultName = defaultName;
		this.counter = new AtomicLong();
	}
	
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getVersion() {
		return "Welcome to eBuy! You are currently using version 0.0.1";
	}
	
	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String register(String request) {
		MongoClient mongo = null;
		try {
			mongo = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		DB db = mongo.getDB("Ebuy");
		
		//get current user id
		DBCollection seqNumber = db.getCollection("seqNumber");
		DBCursor user_seq_cursor = seqNumber.find();
		BasicDBObject bo = (BasicDBObject) user_seq_cursor.next();
		int userId=bo.getInt("userSeqNum");
		bo.remove("_id");
		
		DBCollection customer = db.getCollection("customer");
		DBObject dbObject = new BasicDBObject();
	
		JSONObject obj=new JSONObject(request);
		dbObject.put("userId",userId+1);
		dbObject.put("userName",obj.getString("userName"));
		dbObject.put("password",obj.getString("password"));
		dbObject.put("type",obj.getString("type"));
		dbObject.put("emailId",obj.getString("emailId"));
		
		customer.insert(dbObject);
		
		//insert the updated user id
		BasicDBObject searchQuery = new BasicDBObject();
		searchQuery.put("userSeqNum", userId);
		bo.put("userSeqNum", userId+1);
		seqNumber.update(searchQuery, bo);
		
		return "{\r\n\"outputCode\":200,\r\n\"message\":\"Registration succesful\"\r\n}";
	}
	
	@POST
	@Path("/customer")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String getCustomerDetails(String request) {
		MongoClient mongo = null;
		try {
			mongo = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		JSONObject requestObj = new JSONObject(request);
		DB db = mongo.getDB("Ebuy");
		DBCollection catalogue = db.getCollection("customer");
		BasicDBObject document = new BasicDBObject();
		document.put("userId", requestObj.getString("userId"));
		ArrayList<BasicDBObject> list = new ArrayList<BasicDBObject>();
		DBCursor cursor = catalogue.find(document);

		while (cursor.hasNext()) {
			BasicDBObject b = (BasicDBObject) cursor.next();
			b.remove("_id");
			list.add(b);
		}
		return list.toString();
	}
	
	@POST
	@Path("/additems")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String addItems(String request) {
		MongoClient mongo = null;
		try {
			mongo = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		DB db = mongo.getDB("Ebuy");
		
		DBCollection catalogue = db.getCollection("catalogue");
		DBObject dbObject = new BasicDBObject();
	
		JSONObject obj=new JSONObject(request);
		DBCollection customer = db.getCollection("customer");
		DBObject document = new BasicDBObject();
		document.put("userId", obj.getInt("userId"));
		DBCursor cursor = customer.find(document);
		while (cursor.hasNext()) {
			BasicDBObject b = (BasicDBObject) cursor.next();
			String type=b.getString("type");
			if(type.equals("admin")) {
				dbObject.put("productId",obj.getInt("productId"));
				dbObject.put("productName",obj.getString("productName"));
				dbObject.put("price",obj.getString("price"));
				dbObject.put("quantity",obj.getInt("quantity"));
				catalogue.insert(dbObject);
				
				return "{\r\n\"outputCode\":200,\r\n\"message\":\"Product added succesfully\"\r\n}";
			}
			return "{\r\n\"outputCode\":404,\r\n\"message\":\"You must be an admin to add items\"\r\n}";
		}
		return "{\r\n\"outputCode\":404,\r\n\"message\":\"User not found. Please register\"\r\n}";
	}
	
	@GET
	@Path("/catalogue")
	@Produces(MediaType.APPLICATION_JSON)
	public String showCatalogue() {
		MongoClient mongo = null;
		try {
			mongo = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		DB db = mongo.getDB("Ebuy");
		DBCollection catalogue = db.getCollection("catalogue");

		ArrayList<BasicDBObject> list = new ArrayList<BasicDBObject>();
		DBCursor cursor = catalogue.find();

		while (cursor.hasNext()) {
			BasicDBObject b = (BasicDBObject) cursor.next();
			b.remove("_id");
			list.add(b);
		}
		return list.toString();
	}
	
	@POST
	@Path("/product/purchase")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String makeOrder(String request) {
		MongoClient mongo = null;
		try {
			mongo = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		JSONObject requestObj = new JSONObject(request);
		DB db = mongo.getDB("Ebuy");
		DBCollection customer = db.getCollection("customer");
		BasicDBObject document = new BasicDBObject();
		document.put("userId", requestObj.getInt("userId"));
		DBCursor cursor = customer.find(document);

		if (cursor.hasNext()) {
			DBCollection catalogue = db.getCollection("catalogue");
			BasicDBObject product = new BasicDBObject();
			product.put("productId", requestObj.getInt("productId"));
			DBCursor product_cursor = catalogue.find(product);
			if (product_cursor.hasNext()) {
				BasicDBObject b = (BasicDBObject) product_cursor.next();
				int quantity=b.getInt("quantity");
				if(quantity>0) {
					int productId=b.getInt("productId");
					b.remove("_id");
					BasicDBObject searchQuery = new BasicDBObject();
					searchQuery.put("productId", productId);
					b.put("quantity", quantity-1);
					catalogue.update(searchQuery, b);
					
					//update order catalogue
					DBCollection order = db.getCollection("order");
					DBObject dbObject = new BasicDBObject();
					
					DBCollection seqNumber = db.getCollection("seqNumber");
					DBCursor order_seq_cursor = seqNumber.find();
					BasicDBObject bo = (BasicDBObject) order_seq_cursor.next();
					int orderId=bo.getInt("orderSeqNum");
					bo.remove("_id");					
					dbObject.put("orderId",orderId+1);
					
					BasicDBObject cust = (BasicDBObject) cursor.next();
					dbObject.put("userId",cust.getInt("userId"));
					
					dbObject.put("productId",productId);
					
					order.insert(dbObject);
					
					return "{\r\n\"outputCode\":200,\r\n\"message\":\"Success\"\r\n}";
				}
				return "{\r\n\"outputCode\":404,\r\n\"message\":\"Product not available\"\r\n}";
			}
			return "{\r\n\"outputCode\":404,\r\n\"message\":\"Product Id not found. Shop again with valid product id\"\r\n}";
		}
		return "{\r\n\"outputCode\":404,\r\n\"message\":\"User not found. Please register\"\r\n}";
	}
	
	@POST
	@Path("/product/cancel")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String cancelOrder(String request) {
		MongoClient mongo = null;
		try {
			mongo = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		JSONObject requestObj = new JSONObject(request);
		DB db = mongo.getDB("Ebuy");
		DBCollection customer = db.getCollection("order");
		BasicDBObject document = new BasicDBObject();
		document.put("userId", requestObj.getInt("userId"));
		DBCursor cursor = customer.find(document);

		if (cursor.hasNext()) {
			DBCollection myorder = db.getCollection("order");
			BasicDBObject order = new BasicDBObject();
			order.put("orderId", requestObj.getInt("orderId"));
			DBCursor order_cursor = myorder.find(order);
			if (order_cursor.hasNext()) {
				BasicDBObject b = (BasicDBObject) order_cursor.next();
				int orderId=b.getInt("orderId");
				BasicDBObject searchQuery = new BasicDBObject();
				searchQuery.put("orderId", orderId);
				myorder.remove(searchQuery);
				return "{\r\n\"outputCode\":200,\r\n\"message\":\"Order cancelled successfully\"\r\n}";
			}
			return "{\r\n\"outputCode\":404,\r\n\"message\":\"Order Id for this user not found.\"\r\n}";
		}
		return "{\r\n\"outputCode\":404,\r\n\"message\":\"Sorry, No purchase history for this user exists.\"\r\n}";
	}
}
