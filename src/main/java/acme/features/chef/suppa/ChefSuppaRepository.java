package acme.features.chef.suppa;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.recipes.Kitchenware;
import acme.entities.recipes.Suppa;
import acme.framework.repositories.AbstractRepository;
import acme.roles.Chef;

@Repository
public interface ChefSuppaRepository extends AbstractRepository{

	@Query("select distinct c from Suppa c where c.code =:code")
	Suppa findOneSuppaByCode(String code);
	
	@Query("select c from Suppa c where c.kitchenware.id = :id")
	Collection<Suppa> findManySuppasByKitchenwareId(int id);
	
	@Query("select p from Suppa p where p.chef.id =:id")
	Collection<Suppa> findManySuppasByChefId(int id);
	
	@Query("select distinct k from Kitchenware k where k.code =:code")
	Kitchenware findOneKitchenwareByCode(String code);
	
	@Query("select c from Suppa c")
	Collection<Suppa> findManySuppas();
	
	@Query("select c from Suppa c where c.id =:id")
	Suppa findOneSuppaById(int id);
	
	@Query("select c from Chef c where c.id =:id")
	Chef findOneChefById(int id);
	
	@Query("select k from Kitchenware k where k.id =:id")
	Kitchenware findOneKitchenwareById(int id);
	
	@Query("select k from Kitchenware k")
	Collection<Kitchenware> findManyKitchenwares();
	
	@Query("select c.kitchenware from Suppa c")
	Collection<Kitchenware> findManyKitchenwaresWithSuppas();
	
	@Query("select p.code from Suppa p where p.kitchenware.id = :id")
	Collection<String> findManySuppaCodesByKitchenwareId(int id);
	
	@Query("select k from Kitchenware k where k.chef.id =:id")
	Collection<Kitchenware> findManyKitchenwaresByChefId(int id);
}
