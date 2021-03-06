package com.example.ShoppingCartDemo.controller;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ShoppingCartDemo.entity.Basket;
import com.example.ShoppingCartDemo.entity.Customer;
import com.example.ShoppingCartDemo.entity.Items;
import com.example.ShoppingCartDemo.entity.ShoppingCart;

@Controller
@RequestMapping("/shopping")
public class HomeController {
	
	ShoppingCart shoppingCart=new ShoppingCart("Big Bazer");
	
	@PostConstruct
	public void addcustomers() {
		
		
			shoppingCart.add_item(new Items("Juice",45.5,120));
	        shoppingCart.add_item(new Items("Shampoo",122.5,520));
	        shoppingCart.add_item(new Items("Biscuits",12.5,320));
	        shoppingCart.add_customer(new Customer("Ishan","0715822454",new Basket()));
	        shoppingCart.add_customer(new Customer("Reshmika","0715822454",new Basket()));
	        shoppingCart.add_customer(new Customer("Ish","0715822454",new Basket()));

	}
	
	@GetMapping("/init")
	public String homepage(Model model) {
		
		model.addAttribute("customers", shoppingCart.getCustomers());
		
		return "home";
	}
	
	
	@GetMapping("/shop")
	public String cust_items(@RequestParam("customer")String name,Model model) {
		
		int pos=shoppingCart.check_cust(name);
		
		if(pos>=0) {
			model.addAttribute("customer",name);
			model.addAttribute("items",shoppingCart.getItems());
			return "cust_list";
		}
		return "redirect:init";
	}
	
	
	@GetMapping("/addcustomer")
	public String add_cust(Model model) {
		
		model.addAttribute("customer", new Customer());
		
		return "cust_form";
	}
	
	@PostMapping("/savecust")
	public String save_cust(@ModelAttribute("customer")Customer customer) {
		if(customer.getName()!=null && customer.getContact()!=null) {
			
			int pos=shoppingCart.check_cust(customer.getName());
			Customer tosave=null;
			
			if(pos>=0) {
				tosave=shoppingCart.getCustomers().get(pos);
				tosave.setName(customer.getName());
				tosave.setContact(customer.getContact());
				
				return "redirect:init";
			}else {
				tosave=customer;
				shoppingCart.add_customer(tosave);
			}
		}
		return "redirect:init";
		
	}
	
	@GetMapping("/additems")
	public String add_item(Model model) {
		model.addAttribute("Item",new Items());
		
		return "item_form";
		
	}
	
	@GetMapping("/items")
	public String listitems(Model model) {
		model.addAttribute("Items",shoppingCart.getItems());
		return "items";
	}
	
	
	@PostMapping("/saveitem")
	public String save_item(@ModelAttribute("Item") Items item) {
		if((item.getName()!=null && item.getPrice()!=0) && (item.getStock() !=0)) {
			
			int pos=shoppingCart.check_item(item.getName());
			
			
			if(pos>=0) {
				Items items=shoppingCart.getItems().get(pos);
				items.setName(item.getName().trim());
				items.setPrice(item.getPrice());
				items.setStock(item.getStock());
				
				return "redirect:items";
			}else {
				
				shoppingCart.add_item(item);
			}
		}
		return "redirect:items";
		
	}
	
	
	
	@PostMapping("/updateitem") 
	public String update_item(@RequestParam("name")String[] name, @ModelAttribute("Item") Items item) {
		if((item.getName()!=null && item.getPrice()!=0) && (item.getStock() !=0)) {
			
			int pos=shoppingCart.check_item(name[0]);
			System.out.println(pos +"=> "+name[0]);
			
			
			if(pos>=0) {
				Items items=shoppingCart.getItems().get(pos);
				items.setName(name[1]);
				items.setPrice(item.getPrice());
				items.setStock(item.getStock());
				System.out.println(item.toString());
				
				return "redirect:items";
			}else {
				
				shoppingCart.add_item(item);
			}
		}
		return "redirect:items";
		
	}
	
	
	
	@GetMapping("/itemupd")
	public String upd_item(@RequestParam("name")String name,Model model) {
		if(name!=null) {
			int pos=shoppingCart.check_item(name);
			if(pos>=0) {
				Items item=shoppingCart.getItems().get(pos);
				model.addAttribute("Item", item);
				return "item_form_update";
			}
		}
		return "redirect:items";
	}
	
	@GetMapping("/itemdel")
	public String del_item(@RequestParam("name")String name) {
		
		if(name!=null) {
			int pos=shoppingCart.check_item(name);
			if(pos>=0) {
				Items item=shoppingCart.getItems().remove(pos);
			
				
			}
		}
		return "redirect:items";
	}
	
	//update customer
	
	@GetMapping("/updcust")
	public String upd_cust(@RequestParam("customer")String name,Model model) {
		if(name!=null)
		{
			int pos=shoppingCart.check_cust(name);
			if(pos>=0) {
				Customer customer=shoppingCart.getCustomers().get(pos);
				model.addAttribute("customer", customer);
				return "cust_update";
			}
		}
		return "init";
	}

	@PostMapping("/updatecust")
	public String savecust(@RequestParam("name")String[] name,@ModelAttribute("customer") Customer customer) {
		if(customer.getName()!=null && customer.getContact()!=null) {
			
			int pos=shoppingCart.check_cust(name[0]);
			if(pos>=0) {
				Customer customer1=shoppingCart.getCustomers().get(pos);
				customer1.setName(name[1]);
				customer1.setContact(customer.getContact());
				return "redirect:init";
			}
		}
		return "redirect:init";
	}
	
	//delete customer
	@GetMapping("/del_cust")
	public String deletecust(@RequestParam("customer")String name) {
		if(name!= null)
		{
			int pos=shoppingCart.check_cust(name);
			if(pos>=0)
			{
				shoppingCart.getCustomers().remove(pos);
				
			}
		}
		return "redirect:init";
	}
}
