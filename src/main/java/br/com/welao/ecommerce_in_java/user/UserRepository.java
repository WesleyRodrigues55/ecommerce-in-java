package br.com.welao.ecommerce_in_java.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    User findByCpf(String cpf);
    User findByCnpj(String cnpj);
    User findByPhone(String phone);
}
