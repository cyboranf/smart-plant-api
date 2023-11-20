package com.example.smartplantbuddy.seed;

import com.example.smartplantbuddy.model.Role;
import com.example.smartplantbuddy.repository.RoleRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("seed")
public class RoleSeeder implements DatabaseSeeder {
    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /*
        Role:
        "1": "GUEST"
        "2": "USER"
     */
    /**
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            Role roleGuest = new Role();
            roleGuest.setName("GUEST");

            Role roleUser = new Role();
            roleUser.setName("USER");

            roleRepository.save(roleGuest);
            roleRepository.save(roleUser);
        }
    }
}
