package org.flush.erates.database;

import java.util.LinkedList;
import java.util.List;

import org.flush.erates.date.DateLogic;
import org.flush.erates.dto.Rates;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DatabaseClass {
	
	public static List<Rates> getNeededDateList(String date, String bank) {
		@SuppressWarnings("deprecation")
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Rates r where r.date = :date and r.bank = :bank");
		query.setParameter("date", date);
		query.setParameter("bank", bank);
		
		final List<Rates> rates = new LinkedList<>();
		for (final Object obj : query.list()) {
			rates.add((Rates) obj);
		}
		return rates;
	}
	
	public static void insertToRates(List<Rates> listRates) {
		Rates local = new Rates();
		for (Rates rates: listRates) {
			@SuppressWarnings("deprecation")
			SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			local.setBank(rates.getBank());
			local.setRate(rates.getRate());
			local.setDate(DateLogic.formatPbDate(rates.getDate()));
			local.setBuy(rates.getBuy());
			local.setSale(rates.getSale());
			
			session.save(local);
			session.getTransaction().commit();
		}	
	}
}
