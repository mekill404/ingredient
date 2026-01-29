
import java.sql.*;

import config.DBConnection;
import models.*;
import models.Enum.*;

public class DataRetriever {

	public Order findOrderByReference(String reference) {
		DBConnection dbConnection = new DBConnection();
		try (Connection connection = dbConnection.getConnection()) {
			String sql = "SELECT id, reference, creation_datetime, type, status FROM \"order\" WHERE reference = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, reference);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Order order = new Order();
				order.setId(rs.getInt("id"));
				order.setReference(rs.getString("reference"));
				order.setCreationDatetime(rs.getTimestamp("creation_datetime").toInstant());
				order.setType(OrderType.valueOf(rs.getString("type")));
				order.setStatus(OrderStatus.valueOf(rs.getString("status")));
				return order;
			}
			return null;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Order saveOrder(Order toSave) {
		Order existingOrder = findOrderByReference(toSave.getReference());

		if (existingOrder != null && existingOrder.getStatus() == OrderStatus.DELIVERED) {
			throw new RuntimeException("Une commande livrée ne peut plus être modifiée.");
		}

		String sql = """
				INSERT INTO "order" (id, reference, creation_datetime, type, status)
				VALUES (?, ?, ?, ?::order_type, ?::order_status)
				ON CONFLICT (id) DO UPDATE
				SET type = EXCLUDED.type, status = EXCLUDED.status
				""";

		try (Connection conn = new DBConnection().getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, toSave.getId());
			ps.setString(2, toSave.getReference());
			ps.setTimestamp(3, Timestamp.from(toSave.getCreationDatetime()));
			ps.setString(4, toSave.getType().name());
			ps.setString(5, toSave.getStatus().name());

			ps.executeUpdate();
			return toSave;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Ingredient saveIngredient(Ingredient toSave) {
		String sql = """
				INSERT INTO ingredient (id, name, price, category)
				VALUES (?, ?, ?, ?::ingredient_category)
				ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, price = EXCLUDED.price, category = EXCLUDED.category
				""";
		try (Connection conn = new DBConnection().getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, toSave.getId());
			ps.setString(2, toSave.getName());
			ps.setDouble(3, toSave.getPrice());
			ps.setString(4, toSave.getCategory().name());
			ps.executeUpdate();
			return toSave;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Ingredient findIngredientById(Integer id) {
		try (Connection conn = new DBConnection().getConnection()) {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ingredient WHERE id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Ingredient i = new Ingredient();
				i.setId(rs.getInt("id"));
				i.setName(rs.getString("name"));
				i.setPrice(rs.getDouble("price"));
				i.setCategory(CategoryEnum.valueOf(rs.getString("category")));
				return i;
			}
			throw new RuntimeException("Ingrédient non trouvé");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Dish findDishById(Integer id) {
		try (Connection conn = new DBConnection().getConnection()) {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM dish WHERE id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Dish d = new Dish();
				d.setId(rs.getInt("id"));
				d.setName(rs.getString("name"));
				d.setDishType(DishTypeEnum.valueOf(rs.getString("dish_type")));
				d.setPrice(rs.getDouble("selling_price"));
				return d;
			}
			throw new RuntimeException("Plat non trouvé");
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Dish saveDish(Dish toSave) {
		String sql = """
				INSERT INTO dish (id, name, dish_type, selling_price)
				VALUES (?, ?, ?::dish_type, ?)
				ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name, selling_price = EXCLUDED.selling_price
				""";
		try (Connection conn = new DBConnection().getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, toSave.getId());
			ps.setString(2, toSave.getName());
			ps.setString(3, toSave.getDishType().name());
			ps.setDouble(4, toSave.getPrice());
			ps.executeUpdate();
			return toSave;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}