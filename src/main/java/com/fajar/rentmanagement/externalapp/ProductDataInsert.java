package com.fajar.rentmanagement.externalapp;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.fajar.rentmanagement.entity.Product;
import com.fajar.rentmanagement.entity.Unit;

public class ProductDataInsert {

	static Session session;
	static {
		session = HibernateSessions.setSession();
	}
	static final String[] names = new String[] {"-SHELTER" , 
			"Tent with fly sheet, poles, hangers, stakes" , 
			"Tarp" , 
			"Rope" , 
			"Sleeping pad, mats, air mattress, air pump" , 
			"Blanket, comforter, or sleeping bag" , 
			"Pillows" , 
			"-COOKING" , 
			"Pots and pans" , 
			"Cutting board" , 
			"Corkscrew" , 
			"Can opener" , 
			"Multitool or pocket knife" , 
			"Mixing bowls and screen covers" , 
			"Empty food containers" , 
			"Aluminum foil" , 
			"Zip lock bags" , 
			"Cooler" , 
			"Paper plates and bowls, plastic ware" , 
			"Table cloth" , 
			"Folding chairs and table" , 
			"Trash bags " , 
			"Camp stove / smoker / Dutch oven" , 
			"Fuel" , 
			"Lighter" , 
			"Fire extinguisher" , 
			"Campfire permit" , 
			"-CLOTHES" , 
			"T-shirts" , 
			"Shorts, pants" , 
			"Boots, shoes, flip-flops" , 
			"Socks" , 
			"Hat, cap " , 
			"Sweater, jacket " , 
			"Neck gaiter/buff, scarf" , 
			"Underwear" , 
			"Swimsuit " , 
			"Towels, washcloths " , 
			"Club soda to rinse out stains " , 
			"Laundry bags" , 
			"Travel-size packets of laundry soap" };
	static List<Unit> units = new ArrayList<>();
	public static void main(String[] args) {
		try {
			units = session.createCriteria(Unit.class).list();
			insert();
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		System.exit(0);
		
	}
	private static void insert() {
		Transaction tx = session.beginTransaction();
		try {
			String type = names[0];
			for (String name : names) {
				if (name.startsWith("-")) {
					type = name;
					continue;
				}
				Product p = new Product();
				p.setName(name);
				p.setUnit(units.get(0));
				p.setCode(name.replace(" ", ""));
				p.setDescription(type);
				p.setForRent(true);
				
				session.save(p);
				
			}
		tx.commit();
		} catch (Exception e) {
			if (null != tx) {
				tx.rollback();
			}
		}
	}
	
}
