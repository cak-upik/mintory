

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.dao;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author martin
 */
public class BaseDaoHibernate <T> {

	@SuppressWarnings("unchecked")
	protected Class domainClass;

        @Autowired
	protected SessionFactory sessionFactory;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public BaseDaoHibernate() {
		this.domainClass = (Class) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@SuppressWarnings("unchecked")
	public T findById(String id) {
		final T domain = (T) sessionFactory
		.getCurrentSession().get(domainClass, id);
		return domain;
	}
	public void save(T domain) {
		sessionFactory
		.getCurrentSession()
		.saveOrUpdate(domain);
	}
	public void delete(T domain) {
		sessionFactory
		.getCurrentSession()
		.delete(domain);
	}

	@SuppressWarnings("unchecked")
	public Long count() {
		List list = sessionFactory
		.getCurrentSession()
		.createQuery(
				"select count(*) from " + domainClass.getName() + " x")
				.list();
		Long count = (Long) list.get(0);
		return count;
	}

	@SuppressWarnings("unchecked")
	public List<T> findAll(Integer startIndex, Integer pageSize) {
		return sessionFactory
		.getCurrentSession()
		.createQuery("from " + domainClass.getName())
		.setFirstResult(startIndex)
		.setMaxResults(pageSize)
		.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> findByExample(T exampleInstance, String... excludeProperty) {
		Criteria crit = sessionFactory
		.getCurrentSession()
		.createCriteria(domainClass);
		Example example =
			Example.create(exampleInstance)
			.enableLike(MatchMode.ANYWHERE)
			.ignoreCase();
		for (String exclude : excludeProperty) {
			example.excludeProperty(exclude);
		}
		crit.add(example);
		return crit.list();
	}
}
