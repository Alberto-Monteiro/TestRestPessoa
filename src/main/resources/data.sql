DROP TABLE IF EXISTS pessoa;

CREATE TABLE pessoa (
 id INT AUTO_INCREMENT PRIMARY KEY,
 nome VARCHAR(250) NOT NULL,
 cpf VARCHAR(11) NOT NULL,
 data_nascimento DATETIME NOT NULL
);
