package com.silica.info;
import java.util.List;

import org.springframework.stereotype.Service;
import com.silica.info.model.Users;
import com.silica.info.repository.UserRepository;

@Service
public class InfoService {
	private final UserRepository userRepository;
	
	public InfoService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public List<Users> getUserList() {
		return this.userRepository.findList();
	}

	public List<Users> findIdentityUser(String id) {
		return this.userRepository.findIdentityUser(id);
	}
	
	public Users Insert(Users user) {
		return this.userRepository.Insert(user);
	}
	
	public Integer Update(Users user) {
		return this.userRepository.Update(user);
	}
	
	public Integer Delete(Integer id) {
		return userRepository.Delete(id);
	}
}

