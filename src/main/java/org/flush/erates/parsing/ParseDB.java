package org.flush.erates.parsing;

import javax.servlet.http.HttpServletRequest;

import org.flush.erates.dto.Rates;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class ParseDB {

	public Query checkDateAndBank(String date, String bank) {
		@SuppressWarnings("deprecation")
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Query query = session.createQuery("from Rates r where r.date = :date and r.bank = :bank");
		query.setParameter("date", date);
		query.setParameter("bank", bank);
		
		session.getTransaction().commit();

		return query;
	}
	
	public void insertToRates(String bank, String rate, String date, Double buy, Double sale) {
		@SuppressWarnings("deprecation")
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		Rates rates = new Rates();
		rates.setBank(bank);
		rates.setRate(rate);
		rates.setDate(date);
		rates.setBuy(buy);
		rates.setSale(sale);
		session.save(rates);
		
		session.getTransaction().commit();
	}
	
	public void getFromRates(HttpServletRequest request, String currency, Rates rates) {
		request.setAttribute(currency + "Rate", rates.getRate());
		request.setAttribute(currency + "Date", rates.getDate());
		request.setAttribute(currency + "Buy", rates.getBuy());
		request.setAttribute(currency + "Sale", rates.getSale());
	}
}
