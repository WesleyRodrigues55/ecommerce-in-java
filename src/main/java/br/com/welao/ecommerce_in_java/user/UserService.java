package br.com.welao.ecommerce_in_java.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean isEmailAvailable(String email) {
        return userRepository.findByEmail(email) == null;
    }

    public boolean isPhoneAvailable(String phone) {
        return userRepository.findByPhone(phone) == null;
    }

    public boolean isCpfOrCnpjAvailable(UserDTO userDTO) {
        if ("FÍSICA".equals(userDTO.getTypePerson())) {
            return userRepository.findByCpf(userDTO.getCpf()) == null;
        } else if ("JURÍDICA".equals(userDTO.getTypePerson())) {
            return userRepository.findByCnpj(userDTO.getCnpj()) == null;
        }
        return true;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

}
