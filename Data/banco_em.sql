-- use em;

CREATE TABLE ano 
( 
 id INT NOT NULL AUTO_INCREMENT,  
 descricao VARCHAR(255) NOT NULL,  
 PRIMARY KEY (id)
);

CREATE TABLE diciplinas 
( 
 id INT NOT NULL AUTO_INCREMENT,  
 descricao VARCHAR(255) NOT NULL,  
 PRIMARY KEY (id),
 idano INT NOT NULL, 
 FOREIGN KEY (idano) REFERENCES ano(id)  
); 

CREATE TABLE conteudo 
( 
 id INT NOT NULL AUTO_INCREMENT,  
 descricao VARCHAR(255) NOT NULL ,  
 PRIMARY KEY (id),
 id_diciplinas INT NOT NULL, 
 FOREIGN KEY (id_diciplinas) REFERENCES diciplinas(id)  
); 

/* ALTER TABLE diciplinas ADD FOREIGN KEY(idano) REFERENCES ano (idano)
ALTER TABLE conteudo ADD FOREIGN KEY(iddiciplinas) REFERENCES diciplinas (iddiciplinas)*/
