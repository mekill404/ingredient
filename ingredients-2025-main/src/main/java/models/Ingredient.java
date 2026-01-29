package models;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import models.Enum.CategoryEnum;
import models.Enum.MovementTypeEnum;
import models.Enum.Unit; 

public class Ingredient {
    private Integer id;
    private String name;
    private CategoryEnum category;
    private Double price;
    private List<StockMovement> stockMovementList;

    public Ingredient() {
    }

    public Ingredient(Integer id, String name, CategoryEnum category, Double price,
            List<StockMovement> stockMovementList) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stockMovementList = stockMovementList;
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

	public CategoryEnum getCategory() {
		return category;
	}

	public void setCategory(CategoryEnum category) {
		this.category = category;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public List<StockMovement> getStockMovementList() {
		return stockMovementList;
	}

	public void setStockMovementList(List<StockMovement> stockMovementList) {
		this.stockMovementList = stockMovementList;
	}

	public StockValue getStockValueAt(Instant t) {
        if (this.stockMovementList == null || this.stockMovementList.isEmpty()) {
            return null;
        }

        Map<Unit, List<StockMovement>> unitSet = stockMovementList.stream()
                .collect(Collectors.groupingBy(sm -> sm.getValue().getUnit()));
        
        if (unitSet.keySet().size() > 1) {
            throw new RuntimeException("Erreur : Unités multiples pour un même ingrédient !");
        }

        List<StockMovement> stockMovements = stockMovementList.stream()
                .filter(sm -> !sm.getCreationDatetime().isAfter(t))
                .toList();

        double movementIn = stockMovements.stream()
                .filter(sm -> sm.getType() == MovementTypeEnum.IN) 
                .mapToDouble(sm -> sm.getValue().getQuantity())
                .sum();

        double movementOut = stockMovements.stream()
                .filter(sm -> sm.getType() == MovementTypeEnum.OUT)
                .mapToDouble(sm -> sm.getValue().getQuantity())
                .sum();

        StockValue stockValue = new StockValue();
        stockValue.setQuantity(movementIn - movementOut);
        
        Unit firstUnit = unitSet.keySet().iterator().next();
        stockValue.setUnit(firstUnit);

        return stockValue;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", actualStock=" + getStockValueAt(Instant.now()) + 
                '}';
    }
}