package br.com.android.sample.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *
 */
@Entity
public class User extends AbstractEntity implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4052986759552589018L;

	/*-------------------------------------------------------------------
	 *				 		     ATTRIBUTES
	 *-------------------------------------------------------------------*/
	// Basic
	/**
	 * 
	 */
	@Column(nullable = false, length = 50)
	private String name;
	/**
	 * 
	 */
	@Column(nullable = false, length = 144, unique = true)
	private String email;
	/**
	 * 
	 */
	@Column(nullable = false)
	private Date dataNacimento;
	/**
	 *
	 */
	@Column(nullable = false)
	private Boolean disabled;
	/**
	 * 
	 */
	@Column(nullable = false, length = 100)
	private String password;
	/**
	 * 
	 */
	@Column(nullable = false)
	@Enumerated(EnumType.ORDINAL)
	private UserRole role;

	/*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
	/**
	 * 
	 */
	public User()
	{
	}

	/**
	 * 
	 * @param id
	 */
	public User( Long id )
	{
		super( id );
	}

	/*-------------------------------------------------------------------
	 *							BEHAVIORS
	 *-------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ( ( email == null ) ? 0 : email.hashCode() );
		result = prime * result + ( ( disabled == null ) ? 0 : disabled.hashCode() );
		result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
		result = prime * result + ( ( dataNacimento == null ) ? 0 : dataNacimento.hashCode() );
		result = prime * result + ( ( password == null ) ? 0 : password.hashCode() );
		result = prime * result + ( ( role == null ) ? 0 : role.hashCode() );
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj ) return true;
		if ( !super.equals( obj ) ) return false;
		if ( getClass() != obj.getClass() ) return false;
		User other = ( User ) obj;
		if ( email == null )
		{
			if ( other.email != null ) return false;
		}
		else if ( !email.equals( other.email ) ) return false;
		if ( disabled == null )
		{
			if ( other.disabled != null ) return false;
		}
		else if ( !disabled.equals( other.disabled ) ) return false;
		if ( name == null )
		{
			if ( other.name != null ) return false;
		}
		else if ( !name.equals( other.name ) ) return false;
		if ( password == null )
		{
			if ( other.password != null ) return false;
		}
		else if ( !password.equals( other.password ) ) return false;
		if ( dataNacimento == null )
		{
			if ( other.dataNacimento != null ) return false;
		}
		else if ( !dataNacimento.equals( other.dataNacimento ) ) return false;

		return true;
	}

	/*-------------------------------------------------------------------
	 *						GETTERS AND SETTERS
	 *-------------------------------------------------------------------*/
	
	/**
	 * @param disabled
	 *            the enabled to set
	 */
	public void setDisabled( Boolean disabled )
	{
		this.disabled = disabled;
	}
	
	/**
	 *
	 */
	public Boolean getDisabled()
	{
		return this.disabled;
	}
	
	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * @param name
	 *            the name to set
	 */
	public void setName( String name )
	{
		this.name = name;
	}

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail( String email )
	{
		this.email = email;
	}

	/**
	 * @return the role
	 */
	public UserRole getRole()
	{
		return role;
	}

	/**
	 * @param userRole
	 *            the role to set
	 */
	public void setRole( UserRole userRole )
	{
		this.role = userRole;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword( String password )
	{
		this.password = password;
	}

	/**
	 * @return the dataNacimento
	 */
	public Date getDataNacimento()
	{
		return dataNacimento;
	}

	/**
	 * @param dataNacimento
	 *            the dataNacimento to set
	 */
	public void setDataNacimento( Date dataNacimento )
	{
		this.dataNacimento = dataNacimento;
	}

}