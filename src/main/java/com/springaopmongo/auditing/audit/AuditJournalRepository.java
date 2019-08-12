package com.springaopmongo.auditing.audit;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditJournalRepository extends MongoRepository<AuditJournal, String> { }
