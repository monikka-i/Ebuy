package io.doll.Model;

public class Fruit {
	private String fruitId;
	private String fruitName;
	private int quantity;
	public String getFruitId() {
		return fruitId;
	}
	public void setFruitId(String fruitId) {
		this.fruitId = fruitId;
	}
	public String getFruitName() {
		return fruitName;
	}
	public void setFruitName(String fruitName) {
		this.fruitName = fruitName;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getPriceOfOneItem() {
		return priceOfOneItem;
	}
	public void setPriceOfOneItem(double priceOfOneItem) {
		this.priceOfOneItem = priceOfOneItem;
	}
	private double priceOfOneItem;
}
