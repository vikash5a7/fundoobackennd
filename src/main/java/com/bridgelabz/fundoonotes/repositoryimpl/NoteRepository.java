package com.bridgelabz.fundoonotes.repositoryimpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.Entity.NoteInformation;
import com.bridgelabz.fundoonotes.repository.INoteRepositary;

@Repository
public class NoteRepository implements INoteRepositary {


	@Autowired
	private EntityManager entityManager;
	private static final Logger LOG = LoggerFactory.getLogger(NoteRepository.class);

	/**
	 * saving or Updating notes in database
	 * @param information
	 * @return Note information
	 */
	@Override
	public NoteInformation save(NoteInformation noterInformation) {
		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(noterInformation);
		LOG.info("Node Saved with "+noterInformation.getTitle());
		return noterInformation;

	}

	/**
	 * Finding the NoteInformation using the id
	 *
	 * @param id
	 * @return userInformation
	 */
	@Override
	public NoteInformation findById(long id) {
		LOG.trace("Inside NoteRepository of findById ");
		Session session = entityManager.unwrap(Session.class);
		Query q = session.createQuery("from NoteInformation where id=:id ");
		q.setParameter("id", id);
		return (NoteInformation) q.uniqueResult();

	}

	/**
	 * checking is pinned or not
	 *
	 * @param id
	 * @return true or false
	 */
	@Override
	public boolean isPined(long id) {
		LOG.trace("Checking for the is pinned or not");
		Session session = entityManager.unwrap(Session.class);
		Query q = session.createQuery("SELECT N.isPinned FROM NoteInformation N where id=:id ");
		q.setParameter("id", id);
		int result = q.executeUpdate();
		if (result >= 1) {
			return true;
		} else {
			return false;
		}
		}

	/**
	 * Deleting the note based on there id and userId
	 *
	 * @param id
	 * @param userid
	 * @return true and false
	 */

	@Override
	public boolean deleteNote(long id, long userid) {
		LOG.trace("Inside NoteRepository of deleteNote ");

		Session session = entityManager.unwrap(Session.class);
		String hql = "DELETE FROM NoteInformation " + "WHERE id = :id";
		Query query = session.createQuery(hql);
		query.setParameter("id", id);
		int result = query.executeUpdate();
		if (result >= 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * getting the list of note's information
	 *
	 * @param userid
	 * @return list of notes
	 */

	@SuppressWarnings("unchecked")
	public List<NoteInformation> getNotes(long userid) {
		LOG.trace("Inside NoteRepository of getNotes ");
		Session session = entityManager.unwrap(Session.class);
		return session.createQuery(
				"from NoteInformation where user_id='" + userid + "'" + " and is_trashed=false and is_archieved=false ORDER BY id DESC")
				.getResultList();
	}

	/**
	 * getting the list of trashed...
	 *
	 * @param userid
	 * @return list of trashed note's
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<NoteInformation> getTrashedNotes(long userid) {
		LOG.trace("Inside NoteRepository of getTrashedNotes ");
		Session session = entityManager.unwrap(Session.class);

		return session
				.createQuery("from NoteInformation where noteInformationId='" + userid + "'" + " and is_trashed=true")
				.getResultList();
	}

	/**
	 * getting the archive notes
	 *
	 * @param userid
	 * @return list of notes
	 */

	@SuppressWarnings("unchecked")
	@Transactional
	public List<NoteInformation> getArchiveNotes(long userid) {

		LOG.trace("Inside NoteRepository of getArchiveNotes ");
		Session session = entityManager.unwrap(Session.class);

		return session.createQuery("from NoteInformation where user_Id='" + userid + "'" + " and is_archieved=true"
				+ " and is_trashed=false").getResultList();
	}

	/**
	 * updating the color of notes
	 *
	 * @param id
	 * @param userid
	 * @param colour
	 * @return true or false
	 */

	@Override
	public boolean updateColour(long id, long userid, String colour) {
		LOG.trace("Setting color of notes");
		Session session = entityManager.unwrap(Session.class);
		Query query = session.createQuery("update NoteInformation u set u.colour = :colour" + " where u.id = :id");
		query.setParameter("colour", "red");
		query.setParameter("id", id);
		int result = query.executeUpdate();
		if (result >= 1) {
			return true;
		} else {
			return false;
		}
	}

	public List<NoteInformation> findNoteByTitle(String title) {
		// TODO Auto-generated method stub
		return null;
	}

}
