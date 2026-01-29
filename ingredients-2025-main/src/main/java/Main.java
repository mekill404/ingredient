import models.*;
import models.Enum.*;
import config.DBConnection;
import java.sql.Connection;
import java.sql.Statement;
import java.time.Instant;

public class Main {
	public static void main(String[] args) {
		DataRetriever dataRetriever = new DataRetriever();
		try (Connection conn = new DBConnection().getConnection();
				Statement stmt = conn.createStatement()) {
			stmt.executeUpdate("DELETE FROM dish_order WHERE id_order = 100");
			stmt.executeUpdate("DELETE FROM \"order\" WHERE id = 100");
			System.out.println(">>> [INFO] Base de données nettoyée. Prêt pour la démo.\n");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		System.out.println("========== TEST 1 : GESTION DES INGRÉDIENTS ==========");
		Ingredient tomate = new Ingredient();
		tomate.setId(10);
		tomate.setName("Tomate");
		tomate.setPrice(500.0);
		tomate.setCategory(CategoryEnum.VEGETABLE);
		dataRetriever.saveIngredient(tomate);

		Ingredient foundIng = dataRetriever.findIngredientById(10);
		System.out.println("Ingrédient sauvegardé et retrouvé : " + foundIng.getName());

		System.out.println("\n========== TEST 2 : GESTION DES PLATS ==========");
		Dish dish = dataRetriever.findDishById(1);
		System.out.println("Plat trouvé : " + dish.getName() + " | Type: " + dish.getDishType());

		dish.setPrice(15000.0);
		dataRetriever.saveDish(dish);
		System.out.println("Prix mis à jour pour " + dish.getName());

		System.out.println("\n========== TEST 3 : GESTION DES COMMANDES ==========");
		Order myOrder = new Order();
		myOrder.setId(100);
		myOrder.setReference("REF-2026-001");
		myOrder.setCreationDatetime(Instant.now());
		myOrder.setType(OrderType.EAT_IN);
		myOrder.setStatus(OrderStatus.CREATED);

		dataRetriever.saveOrder(myOrder);
		System.out.println("Commande créée avec succès !");

		Order foundOrder = dataRetriever.findOrderByReference("REF-2026-001");
		System.out.println("Commande retrouvée : Status = " + foundOrder.getStatus());

		System.out.println("\n========== TEST 4 : RÈGLE MÉTIER (LIVRÉ) ==========");
		foundOrder.setStatus(OrderStatus.READY);
		dataRetriever.saveOrder(foundOrder);
		System.out.println("Statut mis à jour vers READY.");

		foundOrder.setStatus(OrderStatus.DELIVERED);
		dataRetriever.saveOrder(foundOrder);
		System.out.println("Statut mis à jour vers DELIVERED.");

		System.out.println("Tentative de modification d'une commande LIVRÉE...");
		try {
			foundOrder.setType(OrderType.TAKE_AWAY);
			dataRetriever.saveOrder(foundOrder);
			System.out.println("ERREUR : La commande a été modifiée alors qu'elle était livrée !");
		} catch (RuntimeException e) {
			System.out.println("SUCCÈS DU TEST : L'exception a bien été levée : " + e.getMessage());
		}
	}
}