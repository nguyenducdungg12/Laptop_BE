package com.example.demo.Service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.ProductsResponse;
import com.example.demo.Repository.ProductRepo;
import com.example.demo.Service.ProductService;
import com.example.demo.model.ProductModel;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	ProductRepo Products;
	@Autowired
	ProductsResponse productResponse;
	@Override
	public ProductsResponse getAllProduct(int page,int sort,long max,long min) {
		List<ProductModel> ListProduct;
		if(sort!=0){
			Sort sorts = sort==1 ?  Sort.by(Sort.Direction.ASC,"newprice") : Sort.by(Sort.Direction.DESC,"newprice");
			if(max!=0) {
				ListProduct = Products.findByNewpriceRangeSort(sorts,max, min);
			}
			else {			
				ListProduct = Products.findByNewpriceRangeSortGreatthan(sorts,min);
			}
		}
		else {
			if(max!=0) {
				ListProduct= Products.findByNewpriceRangeSort(null,max, min);

			}
			else {
				ListProduct= Products.findByNewpriceRangeSortGreatthan(null,min);
				}
		}
		if(page==1) {
			if(ListProduct.size()<page*15) {
				productResponse.setListProducts(ListProduct);
			}
			else {
				
				productResponse.setListProducts(ListProduct.subList(0, page*15));
			}			
		}
		else {
			if(ListProduct.size()<page*15) {
				if((page+1)*3-ListProduct.size()>15) {
					productResponse.setListProducts(Collections.emptyList());
				}
				else {
					productResponse.setListProducts(ListProduct.subList((page-1)*15, ListProduct.size()));
				}
			}
			else {				
				productResponse.setListProducts(ListProduct.subList((page-1)*15, page*15));
			}
		}
		productResponse.setTotalProduct(ListProduct.size());
		productResponse.setTotalPage((productResponse.getTotalProduct()/15)+1);
		productResponse.setProductPerPage(15);
		return productResponse;
		
	}
	public ProductModel addProduct(ProductModel product) {
		product.setCreateBy(new Date());
		product.setUpdatedBy(new Date());
		return Products.save(product);
	}
	@Override
	public Optional<ProductModel> findProductById(String id) {
		return Products.findById(id);
	}
	@Override
	public ProductsResponse getProductByCategory(String category,int sort,long max,long min, String Type, int page) {
		List<ProductModel> ListProduct;
		if(!category.equals("PK")) {
			if(sort!=0){
				Sort sorts = sort==1 ?  Sort.by(Sort.Direction.ASC,"newprice") : Sort.by(Sort.Direction.DESC,"newprice");
				if(max!=0) {
					ListProduct = Products.findByCategoryAndTypeRangeSort(category,Type,max, min,sorts);
				}
				else {			
					ListProduct = Products.findByCategoryAndTypeSort(category,Type,min,sorts);
				}
			}
			else {
				if(max!=0) {
					ListProduct= Products.findByCategoryAndTypeRangeSort(category,Type,max,min,null);

				}
				else {
					ListProduct= Products.findByCategoryAndTypeSort(category,Type,min,null);
					}
				}
		}
		else {
			if(sort!=0){
				Sort sorts = sort==1 ?  Sort.by(Sort.Direction.ASC,"newprice") : Sort.by(Sort.Direction.DESC,"newprice");
				if(max!=0) {
					ListProduct = Products.findByCategoryAndTypeRangeSortPK("laptop",Type,max, min,sorts);
				}
				else {			
					ListProduct = Products.findByCategoryAndTypeRangeSortGreatthan("laptop",Type,min,sorts);
				}
			}
			else {
				if(max!=0) {
					ListProduct= Products.findByCategoryAndTypeRangeSortPK("laptop",Type,max,min,null);

				}
				else {
					ListProduct= Products.findByCategoryAndTypeRangeSortGreatthan("laptop",Type,min,null);
					}
				}
		}
			if(page==1) {
				if(ListProduct.size()<page*15) {
					productResponse.setListProducts(ListProduct);
				}
				else {					
					productResponse.setListProducts(ListProduct.subList(0, page*15-1));
				}
			}
			else {
				if(ListProduct.size()<page*15) {
					if((page+1)*15-ListProduct.size()>15) {
						productResponse.setListProducts(Collections.emptyList());
					}
					else {
						productResponse.setListProducts(ListProduct.subList((page-1)*15, ListProduct.size()));
					}
				}
				else {					
					productResponse.setListProducts(ListProduct.subList((page-1)*15, page*15-1));
				}
			}
			productResponse.setTotalProduct(ListProduct.size());
			productResponse.setTotalPage((productResponse.getTotalProduct()/15)+1);
			productResponse.setProductPerPage(15);
			return productResponse;
	}
	public ProductsResponse getProductByTitle(String search,int page,int sort,long max,long min) {
		List<ProductModel> ListProduct;
		if(sort!=0){
			Sort sorts =sort==1 ?  Sort.by(Sort.Direction.ASC,"newprice") : Sort.by(Sort.Direction.DESC,"newprice");
			if(max!=0) {
				ListProduct = Products.findByTitleRangeSort(search,sorts,max, min);
			}else {
				ListProduct = Products.findByTitleSort(search, min, sorts);
			}
		}
		else {
			if(max!=0) {
				ListProduct= Products.findByTitleRangeSort(search,null, max, min);
			}
			else {
				ListProduct= Products.findByTitleSort(search, min,null);
				}
		}
		if(page==1) {
			if(ListProduct.size()<page*15) {
				productResponse.setListProducts(ListProduct);
			}
			else {				
				productResponse.setListProducts(ListProduct.subList(0, page*15));
			}
		}
		else {
			if(ListProduct.size()<page*15) {
				if((page+1)*15-ListProduct.size()>15) {
					productResponse.setListProducts(Collections.emptyList());
				}
				else {
					productResponse.setListProducts(ListProduct.subList((page-1)*15, ListProduct.size()));
				}
			}
			else {				
				productResponse.setListProducts(ListProduct.subList((page-1)*15, page*15));
			}
		}
		productResponse.setTotalProduct(ListProduct.size());
		productResponse.setTotalPage((productResponse.getTotalProduct()/15)+1);
		productResponse.setProductPerPage(15);
		return productResponse;
	}


}
