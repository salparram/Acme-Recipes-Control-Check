package acme.features.chef.pimpam;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.recipes.Kitchenware;
import acme.entities.recipes.Pimpam;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Chef;

@Repository
public interface ChefPimpamRepository extends AbstractRepository{

	@Query("select distinct c from Pimpam c where c.code =:code")
	Pimpam findOnePimpamByCode(String code);
	
	@Query("select c from Pimpam c where c.kitchenware.id = :id")
	Collection<Pimpam> findManyPimpamsByKitchenwareId(int id);
	
	@Query("select p from Pimpam p where p.chef.id =:id")
	Collection<Pimpam> findManyPimpamsByChefId(int id);
	
	@Query("select distinct k from Kitchenware k where k.code =:code")
	Kitchenware findOneKitchenwareByCode(String code);
	
	@Query("select c from Pimpam c")
	Collection<Pimpam> findManyPimpams();
	
	@Query("select c from Pimpam c where c.id =:id")
	Pimpam findOnePimpamById(int id);
	
	@Query("select c from Chef c where c.id =:id")
	Chef findOneChefById(int id);
	
	@Query("select k from Kitchenware k where k.id =:id")
	Kitchenware findOneKitchenwareById(int id);
	
	@Query("select k from Kitchenware k")
	Collection<Kitchenware> findManyKitchenwares();
	
	@Query("select c.kitchenware from Pimpam c")
	Collection<Kitchenware> findManyKitchenwaresWithPimpams();
	
	@Query("select p.code from Pimpam p where p.kitchenware.id = :id")
	Collection<String> findManyPimpamCodesByKitchenwareId(int id);
	
	@Query("select k from Kitchenware k where k.chef.id =:id")
	Collection<Kitchenware> findManyKitchenwaresByChefId(int id);
}
