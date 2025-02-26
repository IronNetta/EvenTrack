package org.seba.eventrack.bll.services.impls;

import lombok.RequiredArgsConstructor;
import org.seba.eventrack.bll.services.UserService;
import org.seba.eventrack.dal.repositories.UserRepository;
import org.seba.eventrack.dl.entities.User;
import org.seba.eventrack.il.requests.SearchParam;
import org.seba.eventrack.il.specification.SearchSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    @Override
    public Page<User> getUsers(List<SearchParam<User>> searchParams, Pageable pageable) {
        if(searchParams.isEmpty()){
            return userRepository.findAll(pageable);
        }
        return userRepository.findAll(
                Specification.allOf(
                        searchParams.stream()
                                .map(SearchSpecification::search)
                                .toList()
                ),
                pageable
        );
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    @Override
    public User saveUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
