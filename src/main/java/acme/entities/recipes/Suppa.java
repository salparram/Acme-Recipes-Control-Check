package acme.entities.recipes;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.framework.datatypes.Money;
import acme.framework.entities.AbstractEntity;
import acme.roles.Chef;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Suppa extends AbstractEntity{

	// Serialisation identifier ----------------------

		protected static final long serialVersionUID = 1L;
		
		// Attributes  ------------------------------------
		
		@Pattern(regexp="^[a-zA-Z0-9_]{5}-[0-3][0-9]/[01][0-9]/[0-9]{2}$")
		@NotBlank
		@Column(unique=true)
		protected String code;
		
		@Past
		@Temporal(TemporalType.TIMESTAMP)
		@NotNull
		protected Date instantiationMoment; 
		
		@NotBlank
		@Length(max=100)
		protected String title;
		
		@NotBlank
		@Length(max=255)
		protected String description;
		
		@Temporal(TemporalType.TIMESTAMP)
		@NotNull
		protected Date startDate;
		
		@Temporal(TemporalType.TIMESTAMP) 
		@NotNull
		protected Date finishDate;
		
		@Valid
		@NotNull
		protected Money budget;
		
		@URL
		protected String link;
		
		@Valid
		@NotNull
		@ManyToOne(optional=false)
		protected Kitchenware kitchenware;
		
		@Valid
	    @NotNull
	    @ManyToOne
	    protected Chef chef;

}
