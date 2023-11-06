CREATE TABLE `word` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `word_synonyms`(
  `word_id` bigint(20) NOT NULL,
  `synonyms_id` bigint(20) NOT NULL,
  PRIMARY KEY (`word_id`,`synonyms_id`),
  KEY `FKsnqknvh6wak3py044hcaqup93` (`synonyms_id`),
  CONSTRAINT `FKhvwdh48kyt1ll4gcesg41otj2` FOREIGN KEY (`word_id`) REFERENCES `word` (`id`),
  CONSTRAINT `FKsnqknvh6wak3py044hcaqup93` FOREIGN KEY (`synonyms_id`) REFERENCES `word` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;