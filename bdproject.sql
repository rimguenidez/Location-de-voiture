
CREATE TABLE voiture (
    immatriculation VARCHAR(20) PRIMARY KEY,
    marque VARCHAR(50),
    modele VARCHAR(50),
    etat ENUM('en_marche', 'en_panne') DEFAULT 'en_marche',
    disponible BOOLEAN DEFAULT TRUE,
    couleur VARCHAR(20)
);


CREATE TABLE client (
    id_client INT PRIMARY KEY,
    nom VARCHAR(50),
    prenom VARCHAR(50),
    email VARCHAR(100),
    telephone VARCHAR(20)
);


CREATE TABLE location (
    id_location INT PRIMARY KEY AUTO_INCREMENT,
    immatriculation VARCHAR(20),
    id_client INT,
    date_debut DATE,
    date_fin DATE,
    retour_effectif DATE,
    penalite DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (immatriculation) REFERENCES voiture(immatriculation),
    FOREIGN KEY (id_client) REFERENCES client(id_client)
);


CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('client', 'admin') NOT NULL,  
    email VARCHAR(100) UNIQUE NOT NULL,
    telephone VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
);

