package br.com.android.sample.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author rodrigo
 */
@MappedSuperclass
public abstract class AbstractEntity
{
	/**
	 *
	 */
	private static final long serialVersionUID = -3875941859616104733L;

	/*-------------------------------------------------------------------
	 * 		 					ATTRIBUTES
	 *-------------------------------------------------------------------*/
	/**
	 * Atributo id para todas as classes filhas
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	/**
	 *
	 */
	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	protected Calendar created;
	/**
	 *
	 */
	@Temporal(TemporalType.TIMESTAMP)
	protected Calendar updated;

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	/**
	 *
	 */
	public AbstractEntity()
	{
	}

	/**
	 *
	 * @param id
	 */
	public AbstractEntity( Long id )
	{
		this.setId( id );
	}

	/*-------------------------------------------------------------------
	 * 		 					BEHAVIORS
	 *-------------------------------------------------------------------*/
	/**
	 *
	 */
	public boolean isPersisted()
	{
		return this.getId() != null && this.getId() != 0L;
	}

	/**
	 *
	 */
	public boolean isUpdated()
	{
		return this.getUpdated() != null;
	}

	/**
	 *
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( created == null ) ? 0 : created.hashCode() );
		result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
		result = prime * result + ( ( updated == null ) ? 0 : updated.hashCode() );
		return result;
	}

	/**
	 *
	 */
	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj ) return true;
		if ( obj == null ) return false;
		if ( getClass() != obj.getClass() ) return false;
		AbstractEntity other = ( AbstractEntity ) obj;
		if ( created == null )
		{
			if ( other.created != null ) return false;
		}
		else if ( !created.equals( other.created ) ) return false;
		if ( id == null )
		{
			if ( other.id != null ) return false;
		}
		else if ( !id.equals( other.id ) ) return false;
		if ( updated == null )
		{
			if ( other.updated != null ) return false;
		}
		else if ( !updated.equals( other.updated ) ) return false;
		return true;
	}

	/*-------------------------------------------------------------------
	 *				 	    GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
	/**
	 *
	 * @return
	 */
	public Long getId()
	{
		return id;
	}

	public void setId( Long id )
	{
		this.id = id;
	}

	/**
	 * @return the criado
	 */
	public Calendar getCreated()
	{
		return created;
	}

	/**
	 * @param created the criado to set
	 */
	public void setCreated( Calendar created )
	{
		this.created = created;
	}

	/**
	 * @return the updated
	 */
	public Calendar getUpdated()
	{
		return updated;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated( Calendar updated )
	{
		this.updated = updated;
	}
}