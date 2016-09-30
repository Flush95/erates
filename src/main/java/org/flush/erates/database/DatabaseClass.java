package org.flush.erates.database;

import java.util.LinkedList;
import java.util.List;

import org.flush.erates.dto.Rates;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;

public class DatabaseClass {
	static SessionFactory sessionFactory = null;
	public static List<Rates> getNeededDateList(String date, String bank) {
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
	
		Criteria criteria = session.createCriteria(Rates.class);
		criteria.add(Restrictions.like("bank", bank));
		criteria.add(Restrictions.like("date", date));
		
		
		final List<Rates> rates = new LinkedList<>();
		for (final Object obj : criteria.list()) {
			rates.add((Rates) obj);
		}
		session.getTransaction().commit();
		return rates;
	}
	
	public static List<Rates> getRate(Rates rate) {
		sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
	
		Criteria criteria = session.createCriteria(Rates.class);
		criteria.add(Restrictions.like("bank", rate.getBank()));
		criteria.add(Restrictions.like("date", rate.getDate()));
		
		if (rate.getRate().length() > 0) {
			criteria.add(Restrictions.like("rate", rate.getRate()));
		}
		
		final List<Rates> rates = new LinkedList<>();
		for (final Object obj : criteria.list()) {
			rates.add((Rates) obj);
		}
		session.getTransaction().commit();
		return rates;
	}
	
	public static void insertToRates(List<Rates> listRates) {
		Rates local = new Rates();
		for (Rates rates: listRates) {
			SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
			Session session = sessionFactory.openSession();
			session.beginTransaction();
			
			local.setBank(rates.getBank());
			local.setRate(rates.getRate());
			local.setDate(rates.getDate());
			local.setBuy(rates.getBuy());
			local.setSale(rates.getSale());
			
			session.save(local);
			session.getTransaction().commit();

		}	
	}
	
	public static void insertSingleToRates(Rates rateObj) {
		Rates local = new Rates();
		
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		local.setBank(rateObj.getBank());
		local.setRate(rateObj.getRate());
		local.setDate(rateObj.getDate());
		local.setBuy(rateObj.getBuy());
		local.setSale(rateObj.getSale());
			
		session.save(local);
		session.getTransaction().commit();	
	}
}
