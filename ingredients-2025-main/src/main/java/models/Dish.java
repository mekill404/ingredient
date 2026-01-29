package models;

import java.util.ArrayList;
import java.util.List;

import models.Enum.DishTypeEnum;

public class Dish {
	private Integer id;
	private String name;
	private Double price;
	private DishTypeEnum type;
	private List<DishIngredient> dish_ingredients = new ArrayList<>();

	public Dish() {

	}

	public Double getDishCost() {
		double total_coast;
		int i;

		total_coast = 0.0;
		i = 0;
		while (i < this.dish_ingredients.size()) {
			DishIngredient current_dish_ingredient;
			Double ingredient_price;
			Double quantity_needed;

			current_dish_ingredient = this.dish_ingredients.get(i);
			ingredient_price = current_dish_ingredient.getIngredient().getPrice();
			quantity_needed = current_dish_ingredient.getQuantity();
			total_coast = total_coast + (ingredient_price * quantity_needed);
			i = i + 1;
		}
		return total_coast;
	}

	public Double getGrossMargin() {
		if (this.price == null) {
			return 0.0;
		}
		return this.price - this.getDishCost();
	}

	public void setDishIngredients(List<DishIngredient> ingredients) {
		if (ingredients != null) {
			this.dish_ingredients = ingredients;

			int i = 0;
			while (i < this.dish_ingredients.size()) {
				this.dish_ingredients.get(i).setDish(this);
				i = i + 1;
			}
		}
	}

	public List<DishIngredient> getDishIngredients() {
		return this.dish_ingredients;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public DishTypeEnum getDishType() {
		return type;
	}

	public void setDishType(DishTypeEnum dishType) {
		this.type = dishType;
	}

	@Override
	public String toString() {
		return "Plat: " + name + " | Prix: " + price + " | Coût: " + getDishCost() + " | Marge: " + getGrossMargin();
	}
}
