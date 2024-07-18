package com.example.automateelectroluxappliances.repository;

import com.example.automateelectroluxappliances.model.ApplianceRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplianceRuleRepository extends ReactiveMongoRepository<ApplianceRule, String> {
}
