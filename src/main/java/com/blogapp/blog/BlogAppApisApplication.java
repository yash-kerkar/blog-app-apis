package com.blogapp.blog;

import java.util.HashSet;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.asm.Advice.This;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.blogapp.blog.entities.Role;
import com.blogapp.blog.entities.User;
import com.blogapp.blog.repositories.RoleRepository;
import com.blogapp.blog.repositories.UserRepository;
@SpringBootApplication
public class BlogAppApisApplication implements CommandLineRunner{
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(BlogAppApisApplication.class, args);
	}

    @Bean
    ModelMapper mapper() {
        return new ModelMapper();
    }

	@Override
	public void run(String... args) throws Exception {
		Role admin = new Role(1,"ROLE_ADMIN");
		Role user = new Role(2,"ROLE_USER");
		Role savedAdmin = this.roleRepository.save(admin);
		Role savedUser = this.roleRepository.save(user);
		System.out.println(savedAdmin);
		System.out.println(savedUser);
		//Role role1 = this.roleRepository.findByName("ROLE_ADMIN");
		//Role role2 = this.roleRepository.findByName("ROLE_USER");
		User user1 = new User();
		user1.setName("Admin");
		user1.setEmail("admin@gmail.com");
		user1.setPassword(this.passwordEncoder.encode("admin"));
		user1.setAbout("admin");
		Set<Role> roles = new HashSet<>();
		roles.add(savedAdmin);
		roles.add(savedUser);
		user1.setRoles(roles);
		try {
			User savedUser1 = this.userRepository.save(user1);
			System.out.println(savedUser1);
		}catch (Exception e) {
			System.out.println(e);
		}
	}

}
