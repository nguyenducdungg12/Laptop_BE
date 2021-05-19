package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.demo.model.ProductModel;

public interface ProductRepo extends MongoRepository<ProductModel, String> {
	@Query("{'category' : ?0 , 'type' : ?1}")
	List<ProductModel> findByCategoryAndType(String category,String type);
	@Query("{'category' : ?0 , 'type' : ?1,newprice : {'$lt' : ?2, '$gt' : ?3}}")
	List<ProductModel> findByCategoryAndTypeRangeSort(String category,String type,long max,long min, Sort sort);
	@Query("{'category':{'$ne':'?0'} , 'type' : ?1,newprice : {'$lt' : ?2, '$gt' : ?3}}")
	List<ProductModel> findByCategoryAndTypeRangeSortPK(String category,String type,long max,long min, Sort sort);
	@Query("{'category':{'$ne':'?0'} , 'type' : ?1,newprice : {'$gt' : ?2}}")
	List<ProductModel> findByCategoryAndTypeRangeSortGreatthan(String category,String type,long min,Sort sort);
	@Query("{'category' : ?0 , 'type' : ?1,newprice : {'$gt' : ?2}}")
	List<ProductModel> findByCategoryAndTypeSort(String category,String type,long min, Sort sort);
	@Query("{'title':{'$regex':'?0','$options':'i'},'newprice' : {'$gt' : ?1}}")
	List<ProductModel> findByTitleSort(String search,long min,Sort sort);
	@Query("{'title':{'$regex':'?0','$options':'i'},'newprice' : {'$lt' : ?1, '$gt' : ?2}}")
	List<ProductModel> findByTitleRangeSort(String search,Sort sort, long max,long min);
	@Query("{'newprice' : {'$lt' : ?0, '$gt' : ?1}}")
	List<ProductModel> findByNewpriceRangeSort(Sort sort, long max, long min);;
	@Query("{'newprice' : {'$gt' : ?0}}")
	List<ProductModel> findByNewpriceRangeSortGreatthan(Sort sort,long min);

}
